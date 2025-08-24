package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.ProductDataJpaRepository;
import az.gadzhieva.springwork.product.dto.product.ProductRequestDto;
import az.gadzhieva.springwork.product.dto.product.ProductResponseDto;
import az.gadzhieva.springwork.product.error.ProductNotFoundException;
import az.gadzhieva.springwork.product.mapper.ProductMapper;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
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
public class ProductService {
    private final ProductDataJpaRepository productDataJpaRepository;
    private final FirmService firmService;
    private final UserProvider userProvider;


    public ProductResponseDto getProduct(Long id) {
        log.debug("[DEBUG] Searching for product with id={}", id);

        Product product = findProductByIdAndUser(id);

        ProductResponseDto productResponseDto = ProductMapper.entityToResponse(product);
        log.debug("[DEBUG] Product mapped to ProductResponseDto");

        log.info("[GET] Product with id={} was successfully retrieved.", id);
        return productResponseDto;
    }

    public List<ProductResponseDto> getAllProducts() {
        log.debug("[DEBUG] Fetching all products from database...");
        User user = userProvider.getCurrentUser();
        List<Product> products= userProvider.isAdmin(user)?
                productDataJpaRepository.findAll() :
                productDataJpaRepository.findAllByUser(user);
        log.info("[GET] Retrieved {} product(s) from database.", products.size());

        List<ProductResponseDto> response = products.stream()
                .map(ProductMapper::entityToResponse)
                .collect(Collectors.toList());
        log.debug("[DEBUG] Mapped {} product(s) to ProductResponseDto list.", response.size());

        return response;
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"create a product.");
        log.debug("[DEBUG] Creating new product.");
        Firm firm = firmService.findFirmByIdAndUser(productRequestDto.getFirmId());

        Product product = ProductMapper.requestToEntity(productRequestDto, firm);
        log.debug("[DEBUG] ProductRequestDto map to Product.");

        User user = userProvider.getCurrentUser();
        product.setUser(user);

        product = productDataJpaRepository.save(product);
        log.debug("[DEBUG] Created and saved product: id={}, name={}", product.getId(), product.getName());

        ProductResponseDto productResponseDto = ProductMapper.entityToResponse(product);
        log.debug("[DEBUG] Product mapped to ProductResponseDto.");

        return productResponseDto;
    }

    public void deleteProduct(Long id, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"delete a product.");

        log.debug("[DELETE] Deleting product with id={}", id);

        Product product = findProductByIdAndUser(id);

        productDataJpaRepository.delete(product);
        log.info("[DELETE] Product with id={} is deleted.", id);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"update a product.");

        log.debug("[DEBUG] Updating product with id={}", id);

        Product product = findProductByIdAndUser(id);
        Firm firm = firmService.findFirmByIdAndUser(productRequestDto.getFirmId());

        ProductMapper.updateEntity(productRequestDto, product, firm);
        product = productDataJpaRepository.save(product);
        log.info("[UPDATE] Product with id={} is updated.", id);

        return ProductMapper.entityToResponse(product);
    }


    Product findProductByBarcode(Long barcode) {
        User user = userProvider.getCurrentUser();
        Product product = (userProvider.isAdmin(user)?
                productDataJpaRepository.findByBarcode(barcode):
                productDataJpaRepository.findByBarcodeAndUser(barcode, user)).
                orElseThrow(() -> {
                    log.error("[ERROR] Product with barcode={} is not found", barcode);
                    return new ProductNotFoundException("Product not found.");
                });
        log.debug("[DEBUG] Product found with barcode={}, name={}", barcode, product.getName());

        return product;
    }

    Product findProductByIdAndUser(Long id) {
        User user = userProvider.getCurrentUser();
        Product product = (userProvider.isAdmin(user)?
                productDataJpaRepository.findById(id):
                productDataJpaRepository.findByIdAndUser(id, user)).
                orElseThrow(() -> {
                    log.error("[ERROR] Product with id={} is not found for current user.", id);
                    return new ProductNotFoundException("Product not found.");
                });
        log.debug("[DEBUG] Product found with id={}, name={}", product.getId(), product.getName());

        return product;
    }


}
