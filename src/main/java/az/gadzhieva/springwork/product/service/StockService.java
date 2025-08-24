package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.ProductDataJpaRepository;
import az.gadzhieva.springwork.product.error.ProductNotFoundException;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final ProductDataJpaRepository productDataJpaRepository;
    private final ProductService productService;
    private final UserProvider userProvider;


    void revertStock(Invoice invoice, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"revert stock.");
        log.info("[REVERT STOCK] Starting stock revert for invoice id={}", invoice.getId());
        User user = userProvider.getCurrentUser();
        for (InvoiceProduct item : invoice.getItems()) {
            productDataJpaRepository.findByBarcodeAndUser(item.getProductBarcode(), user)
                    .ifPresent(product -> {
                        product.setStock(product.getStock() - item.getQuantity());
                        productDataJpaRepository.save(product);
                    });
        }
        log.info("[REVERT STOCK] Finished stock revert for invoice id={}", invoice.getId());
    }

    void updateProductsFromInvoice(Invoice invoice, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update a product.");
        log.info("[UPDATE STOCK] Starting stock update from invoice id={}", invoice.getId());
        for (InvoiceProduct invoiceProduct : invoice.getItems()) {
            Product product = productService.findProductByBarcode(invoiceProduct.getProductBarcode());
            invoiceProduct.setProductName(product.getName());

            product.setOldPurchasePrice(product.getPurchasePrice());
            product.setOldSellingPrice(product.getSellingPrice());

            product.setPurchasePrice(invoiceProduct.getPurchasePrice());
            product.setSellingPrice(invoiceProduct.getSellingPrice());
            product.setStock(invoiceProduct.getQuantity() + product.getStock());

            productDataJpaRepository.save(product);
        }
        log.info("[UPDATE STOCK] Finished stock update from invoice id={}", invoice.getId());
    }

    void validateInvoiceProducts(Invoice invoice) {
        for (InvoiceProduct item : invoice.getItems()) {
            Long barcode = item.getProductBarcode();
            if (barcode == null || !productDataJpaRepository.existsByBarcodeAndUser(barcode, invoice.getUser())) {
                log.warn("[VALIDATION] Product not found: {}", barcode);
                throw new ProductNotFoundException("Product with barcode '" + barcode + "' not found.");
            }
        }
    }
    void revertPricesProductForDeleted(Invoice invoice){
       for(InvoiceProduct invoiceProduct: invoice.getItems()){
           Product product=productService.findProductByBarcode(invoiceProduct.getProductBarcode());
           product.setPurchasePrice(product.getOldPurchasePrice());
           product.setSellingPrice(product.getOldSellingPrice());
       }
    }

}
