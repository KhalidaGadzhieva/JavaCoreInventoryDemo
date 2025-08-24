package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.InvoiceProductDataJpaRepository;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductResponseDto;
import az.gadzhieva.springwork.product.error.InvoiceProductNotFoundException;
import az.gadzhieva.springwork.product.mapper.InvoiceProductMapper;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceProductService {
    private final InvoiceProductDataJpaRepository invoiceProductDataJpaRepository;
    private final ProductService productService;
    private final UserProvider userProvider;


    public InvoiceProduct createEntityWithProductName(InvoiceProductRequestDto dto, Invoice invoice, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"create an invoice product.");
        Product product = productService.findProductByBarcode(dto.getProductBarcode());
        InvoiceProduct invoiceProduct = InvoiceProductMapper.requestToEntity(dto, invoice);
        invoiceProduct.setProductName(product.getName());
        User user = userProvider.getCurrentUser();
        invoiceProduct.setUser(user);
        return invoiceProduct;
    }


    public InvoiceProductResponseDto createInvoiceProduct(InvoiceProductRequestDto invoiceProductRequestDto, Invoice invoice, HttpSession httpSession) {
userProvider.checkIfAdminThenThrow(httpSession,"create an invoice product.");
        Product product = productService.findProductByBarcode(invoiceProductRequestDto.getProductBarcode());
        User user = userProvider.getCurrentUser();
        InvoiceProduct invoiceProduct = InvoiceProductMapper.requestToEntity(invoiceProductRequestDto, invoice);
        invoiceProduct.setUser(user);
        return InvoiceProductMapper.entityToResponse(invoiceProductDataJpaRepository.save(invoiceProduct));
    }

    public InvoiceProductResponseDto getInvoiceProductById(Long id) {
        InvoiceProduct invoiceProduct = findInvoiceProductByIdAndUser(id);
        return InvoiceProductMapper.entityToResponse(invoiceProduct);
    }

    public List<InvoiceProductResponseDto> getAllInvoiceProduct() {
        User user = userProvider.getCurrentUser();
        List<InvoiceProduct> invoiceProducts= userProvider.isAdmin(user)?
                invoiceProductDataJpaRepository.findAll():
                invoiceProductDataJpaRepository.findAllByUser(user);
        return invoiceProducts.stream().
                map(InvoiceProductMapper::entityToResponse).collect(Collectors.toList());
    }

    public void deleteInvoiceProduct(Long id, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"delete an invoice product.");
        InvoiceProduct invoiceProduct = findInvoiceProductByIdAndUser(id);
        invoiceProductDataJpaRepository.delete(invoiceProduct);
    }

    public InvoiceProductResponseDto updateInvoiceProduct(Long id, InvoiceProductRequestDto invoiceProductRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update an invoice product.");
        InvoiceProduct invoiceProduct = findInvoiceProductByIdAndUser(id);
        InvoiceProductMapper.updateEntity(invoiceProductRequestDto, invoiceProduct);
        invoiceProduct = invoiceProductDataJpaRepository.save(invoiceProduct);
        return InvoiceProductMapper.entityToResponse(invoiceProduct);
    }

    InvoiceProduct findInvoiceProductByIdAndUser(Long id) {
        User user = userProvider.getCurrentUser();
        InvoiceProduct invoiceProduct = (userProvider.isAdmin(user)?
                invoiceProductDataJpaRepository.findById(id):
                invoiceProductDataJpaRepository.findByIdAndUser(id, user)).
                orElseThrow(() -> new InvoiceProductNotFoundException("Invoice Product is not found."));
        return invoiceProduct;
    }
}
