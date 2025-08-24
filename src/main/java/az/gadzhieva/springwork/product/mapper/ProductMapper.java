package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.product.ProductRequestDto;
import az.gadzhieva.springwork.product.dto.product.ProductResponseDto;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;

public class ProductMapper {
    public static ProductResponseDto entityToResponse(Product product){
        return ProductResponseDto.builder().
                id(product.getId()).
                name(product.getName()).
                barcode(product.getBarcode()).
                firmId(product.getFirm().getId()).
                firmName(product.getFirm().getName()).
                category(product.getCategory()).
                purchasePrice(product.getPurchasePrice()).
                sellingPrice(product.getSellingPrice()).
                stock(product.getStock()).
                unit(product.getUnit()).
                build();
    }

    public static void updateEntity(ProductRequestDto productRequestDto, Product product, Firm firm){
        product.setName(productRequestDto.getName());
        product.setBarcode(productRequestDto.getBarcode());
        product.setCategory(productRequestDto.getCategory());
        product.setFirm(firm);
        product.setUnit(productRequestDto.getUnit());
    }
    public static ProductRequestDto responseToRequest(ProductResponseDto productResponseDto) {
        return ProductRequestDto.builder().
                name(productResponseDto.getName()).
                barcode(productResponseDto.getBarcode()).
                firmId(productResponseDto.getFirmId()).
                category(productResponseDto.getCategory()).
                unit(productResponseDto.getUnit()).
                build();
    }
    public static Product requestToEntity(ProductRequestDto productRequestDto, Firm firm){
        return Product.builder().
                name(productRequestDto.getName()).
                barcode(productRequestDto.getBarcode()).
                firm(firm).
                category(productRequestDto.getCategory()).
                unit(productRequestDto.getUnit()).
                build();
    }
}
