package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.InvoiceDataJpaRepository;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceResponseDto;
import az.gadzhieva.springwork.product.error.InvoiceNotFoundException;
import az.gadzhieva.springwork.product.mapper.InvoiceMapper;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceDataJpaRepository invoiceDataJpaRepository;
    private final FirmService firmService;
    private final StockService stockService;
    private final UserProvider userProvider;

    public InvoiceResponseDto createInvoice(InvoiceRequestDto invoiceRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"create an invoice.");
        log.debug("[DEBUG] Creating new invoice for firmId={}", invoiceRequestDto.getFirmId());

        Firm firm = firmService.findFirmByIdAndUser(invoiceRequestDto.getFirmId());

        log.debug("[DEBUG] Firm found: id={}, name={}", firm.getId(), firm.getName());

        Invoice invoice = new Invoice();

        InvoiceMapper.updateEntity(invoiceRequestDto, invoice, firm);
        log.debug("[DEBUG] InvoiceRequestDto mapped to Invoice entity.");

        User user = userProvider.getCurrentUser();
        invoice.setUser(user);

        stockService.validateInvoiceProducts(invoice);

        invoice = invoiceDataJpaRepository.save(invoice);
        log.info("[CREATE] Invoice created with id={} for firmId={}", invoice.getId(), firm.getId());

        stockService.updateProductsFromInvoice(invoice,httpSession);
        log.debug("[DEBUG] Products updated based on invoice id={}", invoice.getId());

        firmService.updateFirmFromInvoice(invoice,httpSession);
        log.debug("[DEBUG] Firm updated based on invoice id={}", invoice.getId());

        InvoiceResponseDto response = InvoiceMapper.entityToResponse(invoice);
        log.debug("[DEBUG] Invoice entity mapped to InvoiceResponseDto with id={}", invoice.getId());

        return response;
    }

    public InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto invoiceRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update an invoice.");

        log.debug("[DEBUG] Updating invoice with id={}", id);

        Invoice invoice = findInvoiceByIdAndUser(id);

        Firm oldFirm = invoice.getFirm();
        log.debug("[DEBUG] Adjusting outstanding debt for old firm with id={}. Old debt: {}", oldFirm.getId(), oldFirm.getOutstandingDebt());

        oldFirm.setOutstandingDebt(oldFirm.getOutstandingDebt().add(invoice.getSum().negate()));
        log.info("[UPDATE] Old firm's (id={}) outstanding debt decreased by {}. New debt: {}", oldFirm.getId(), invoice.getSum(), oldFirm.getOutstandingDebt());

        Firm newFirm = firmService.findFirmByIdAndUser(invoiceRequestDto.getFirmId());
        log.debug("[DEBUG] New firm found with id={}, name={}", newFirm.getId(), newFirm.getName());

        stockService.revertStock(invoice,httpSession);
        log.info("[UPDATE] Reverted stock based on old invoice id={}", invoice.getId());

        InvoiceMapper.updateEntity(invoiceRequestDto, invoice, newFirm);
        log.debug("[DEBUG] Invoice entity updated from InvoiceRequestDto with new firm id={}", newFirm.getId());

        User user = userProvider.getCurrentUser();
        invoice.setUser(user);

        stockService.validateInvoiceProducts(invoice);

        invoice = invoiceDataJpaRepository.save(invoice);
        log.info("[UPDATE] Invoice with id={} saved after update.", invoice.getId());

        stockService.updateProductsFromInvoice(invoice,httpSession);
        log.info("[UPDATE] Updated products stock based on invoice id={}", invoice.getId());

        firmService.updateFirmFromInvoice(invoice,httpSession);
        log.info("[UPDATE] Updated firm data based on invoice id={}", invoice.getId());

        InvoiceResponseDto response = InvoiceMapper.entityToResponse(invoice);
        log.debug("[DEBUG] Invoice entity mapped to InvoiceResponseDto for invoice id={}", invoice.getId());

        return response;
    }


    public InvoiceResponseDto getInvoiceById(Long id) {
        log.debug("[DEBUG] Searching for invoice with id={}", id);

        Invoice invoice = findInvoiceByIdAndUser(id);
        InvoiceResponseDto invoiceResponseDto = InvoiceMapper.entityToResponse(invoice);
        log.debug("[DEBUG] Invoice mapped to InvoiceResponseDto");

        log.info("[GET] Invoice with id={} was successfully retrieved.", id);

        return invoiceResponseDto;
    }

    public List<InvoiceResponseDto> getAllInvoices() {
        log.debug("[DEBUG] Fetching all invoices from database...");

        User user = userProvider.getCurrentUser();

        List<Invoice> invoices= userProvider.isAdmin(user)?
                invoiceDataJpaRepository.findAll() :
                invoiceDataJpaRepository.findAllByUser(user);
        log.info("[GET] Retrieved {} invoice(s) from database.", invoices.size());
        List<InvoiceResponseDto> response = invoices.stream()
                .map(InvoiceMapper::entityToResponse)
                .collect(Collectors.toList());

        log.debug("[DEBUG] Mapped {} invoice(s) to InvoiceResponseDto list.", response.size());
        return response;
    }
    public List<InvoiceResponseDto> getInvoicesForFirm(Long id) {
        log.debug("[DEBUG] Fetching invoices for firm with id={}", id);
        User user = userProvider.getCurrentUser();
        List<Invoice> invoices = userProvider.isAdmin(user)?
                invoiceDataJpaRepository.findByFirmId(id):
                invoiceDataJpaRepository.findByFirmIdAndUser(id,user);
        log.info("[GET] Retrieved {} invoice(s) for firm with id={}", invoices.size(), id);

        List<InvoiceResponseDto> response = invoices.stream()
                .map(InvoiceMapper::entityToResponse)
                .collect(Collectors.toList());

        log.debug("[DEBUG] Mapped {} invoice(s) to InvoiceResponseDto list.", response.size());

        return response;
    }

    public void deleteInvoice(Long id, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"delete an invoice.");

        log.debug("[DELETE] Deleting invoice with id={}", id);
        Invoice invoice = findInvoiceByIdAndUser(id);

        stockService.revertStock(invoice,httpSession);// ⬅️ Вычитаем товар со склада
        log.info("[UPDATE] Reverted stock for products related to invoice id={}", invoice.getId());

        stockService.revertPricesProductForDeleted(invoice);

        Firm firm = invoice.getFirm();
        log.debug("[DEBUG] Current outstanding debt for firm id={} is {}", firm.getId(), firm.getOutstandingDebt());

        firm.setOutstandingDebt(firm.getOutstandingDebt().add((invoice.getSum().negate())));
        log.info("[UPDATE] Adjusted outstanding debt for firm id={}. New debt: {}", firm.getId(), firm.getOutstandingDebt());

        invoiceDataJpaRepository.delete(invoice);
        log.info("[DELETE] Invoice with id={} is deleted.", id);





    }


    public List<InvoiceProductRequestDto> filterEmptyItems(List<InvoiceProductRequestDto> items) {
        return items.stream()
                .filter(item -> {
                    boolean allFieldsEmpty = item.getProductBarcode() == null
                            && (item.getProductName() == null || item.getProductName().isBlank())
                            && item.getPurchasePrice() == null
                            && item.getSellingPrice() == null
                            && item.getQuantity() == null;
                    return !allFieldsEmpty;
                })
                .collect(Collectors.toList());


    }

     Invoice findInvoiceByIdAndUser(Long id) {
        User user = userProvider.getCurrentUser();
        Invoice invoice = (userProvider.isAdmin(user)?
                invoiceDataJpaRepository.findById(id):
                invoiceDataJpaRepository.findByIdAndUser(id,user)).
                orElseThrow(() -> {
                    log.error("[ERROR] Invoice with id={} is not found for current user.", id);
                    return new InvoiceNotFoundException("Invoice not found");
                });
        log.debug("[DEBUG] Found invoice with id={}, sum={}", invoice.getId(), invoice.getSum());
        return invoice;
    }


}

