package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.FirmDataJpaRepository;
import az.gadzhieva.springwork.product.dataJPA.InvoiceDataJpaRepository;
import az.gadzhieva.springwork.product.dataJPA.ProductDataJpaRepository;
import az.gadzhieva.springwork.product.dto.firm.FirmRequestDto;
import az.gadzhieva.springwork.product.dto.firm.FirmResponseDto;
import az.gadzhieva.springwork.product.error.FirmCanNotDelete;
import az.gadzhieva.springwork.product.error.FirmNotFoundException;
import az.gadzhieva.springwork.product.mapper.FirmMapper;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FirmService {

    private final FirmDataJpaRepository firmDataJpaRepository;
    private final InvoiceDataJpaRepository invoiceDataJpaRepository;
    private final ProductDataJpaRepository productDataJpaRepository;
    private final UserProvider userProvider;


    public FirmResponseDto createFirm(FirmRequestDto firmRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"create a firm.");
        log.debug("[DEBUG] Creating new firm.");

        Firm firm = FirmMapper.requestToEntity(firmRequestDto);
        log.debug("[DEBUG] FirmRequestDto map to Firm.");
        User currentUser = userProvider.getCurrentUser();         // достаём текущего юзера
        firm.setUser(currentUser);

        firm = firmDataJpaRepository.save(firm);
        log.debug("[DEBUG] Created and saved firm: id={}, name={}", firm.getId(), firm.getName());

        FirmResponseDto firmResponseDto = FirmMapper.entityToResponse(firm);
        log.debug("[DEBUG] Firm mapped to FirmResponseDto.");

        return firmResponseDto;
    }

    public FirmResponseDto getFirmById(Long id) {
        log.debug("[DEBUG] Searching for firm with id={}", id);

        Firm firm = findFirmByIdAndUser(id);

        FirmResponseDto firmResponseDto = FirmMapper.entityToResponse(firm);
        log.debug("[DEBUG] Firm mapped to FirmResponseDto");

        log.info("[GET] Firm with id={} was successfully retrieved.", id);
        return firmResponseDto;
    }


    public List<FirmResponseDto> getAllFirms() {
        log.debug("[DEBUG] Fetching all firms from database...");
        User currentUser = userProvider.getCurrentUser();// достаём текущего юзера
        List<Firm> firms= userProvider.isAdmin(currentUser)?
                firmDataJpaRepository.findAll():
                firmDataJpaRepository.findAllByUser(currentUser);
        log.info("[GET] Retrieved {} firm(s) from database.", firms.size());


        log.info("[GET] Retrieved {} firm(s) from database.", firms.size());

        List<FirmResponseDto> response = firms.stream()
                .map(FirmMapper::entityToResponse)
                .collect(Collectors.toList());
        log.debug("[DEBUG] Mapped {} firm(s) to FirmResponseDto list.", response.size());

        return response;
    }


    public void deleteFirm(Long id, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"delete a firm.");
        log.debug("[DELETE] Deleting firm with id={}", id);

        Firm firm = findFirmByIdAndUser(id);
        User user = userProvider.getCurrentUser();

        if (!productDataJpaRepository.findByFirmAndUser(firm, user).isEmpty()) {
            log.error("[WARN] Firm with id={} has products and cannot be deleted .", id);
            throw new FirmCanNotDelete("Firm has products and cannot be deleted.");
        }
        if (!invoiceDataJpaRepository.findByFirmAndUser(firm, user).isEmpty()) {
            log.error("[WARN] Firm with id={} has invoices and cannot be deleted.", id);
            throw new FirmCanNotDelete("Firm has invoices and cannot be deleted.");
        }
        firmDataJpaRepository.delete(firm);
        log.info("[DELETE] Firm with id={} is deleted.", id);
    }

    public FirmResponseDto updateFirm(Long id, FirmRequestDto firmRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update a firm.");
        log.debug("[DEBUG] Updating firm with id={}", id);

        Firm firm = findFirmByIdAndUser(id);

        FirmMapper.updateEntity(firmRequestDto, firm);
        firm = firmDataJpaRepository.save(firm);
        log.info("[UPDATE] Firm with id={} is updated.", id);

        return FirmMapper.entityToResponse(firm);
    }

    public void updateFirmFromInvoice(Invoice invoice, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update a firm.");
        Firm firm = invoice.getFirm();
        Long invoiceId = invoice.getId();
        Long firmId = firm.getId();

        log.debug("[DEBUG] Updating firm (id={}) from invoice (id={})...", firmId, invoiceId);

        boolean alreadyHasInvoice = firm.getItems().stream()
                .anyMatch(inv -> inv.getId() != null && inv.getId().equals(invoiceId));

        if (!alreadyHasInvoice) {
            firm.getItems().add(invoice);
            log.info("[UPDATE] Invoice with id={} added to firm (id={}) invoice list.", invoiceId, firmId);
        } else {
            log.debug("[DEBUG] Invoice with id={} already exists in firm (id={}) invoice list.", invoiceId, firmId);
        }

        firm.setOutstandingDebt(firm.getOutstandingDebt().add(invoice.getSum()));
        log.info("[UPDATE] Firm (id={}) outstanding debt increased by {}. New debt: {}.",
                firmId, invoice.getSum(), firm.getOutstandingDebt());

        firmDataJpaRepository.save(firm);
        log.debug("[DEBUG] Firm (id={}) saved successfully after invoice update.", firmId);
    }


    Firm findFirmByIdAndUser(Long id) {
        User currentUser = userProvider.getCurrentUser();         // достаём текущего юзера
        Firm firm = (userProvider.isAdmin(currentUser) ?
                firmDataJpaRepository.findById(id) :
                firmDataJpaRepository.findByIdAndUser(id, currentUser))
                .orElseThrow(() -> {
                    log.error("[ERROR] Firm with id={} is not found for current user.", id);
                    return new FirmNotFoundException("Firm is not found");
                });
        log.debug("[DEBUG] Firm found with id={}, name={}", firm.getId(), firm.getName());

        return firm;
    }


}
