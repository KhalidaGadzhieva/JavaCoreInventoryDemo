package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductResponseDto;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;

public class InvoiceProductMapper {
    public static InvoiceProduct requestToEntity(InvoiceProductRequestDto invoiceProductRequestDto, Invoice invoice) {
        return InvoiceProduct.builder().
                invoice(invoice).
                productBarcode(invoiceProductRequestDto.getProductBarcode()).
                productName(invoiceProductRequestDto.getProductName()).
                purchasePrice(invoiceProductRequestDto.getPurchasePrice()).
                sellingPrice(invoiceProductRequestDto.getSellingPrice()).
                quantity(invoiceProductRequestDto.getQuantity()).
                build();
    }
    public static InvoiceProductResponseDto entityToResponse(InvoiceProduct invoiceProduct) {
        return InvoiceProductResponseDto.builder().
                invoiceId(invoiceProduct.getInvoice().getId()).
                productBarcode(invoiceProduct.getProductBarcode()).
                productName(invoiceProduct.getProductName()).
                purchasePrice(invoiceProduct.getPurchasePrice()).
                sellingPrice(invoiceProduct.getSellingPrice()).
                quantity(invoiceProduct.getQuantity()).
                build();
    }
    public static void updateEntity(InvoiceProductRequestDto invoiceProductRequestDto, InvoiceProduct invoiceProduct){
        invoiceProduct.setPurchasePrice(invoiceProductRequestDto.getPurchasePrice());
        invoiceProduct.setSellingPrice(invoiceProductRequestDto.getSellingPrice());
        invoiceProduct.setQuantity(invoiceProductRequestDto.getQuantity());
    }
    public static InvoiceProductRequestDto responseToRequest(InvoiceProductResponseDto responseDto) {
        return InvoiceProductRequestDto.builder()
                .id(responseDto.getId())
                .productBarcode(responseDto.getProductBarcode())
                .productName(responseDto.getProductName())
                .purchasePrice(responseDto.getPurchasePrice())
                .sellingPrice(responseDto.getSellingPrice())
                .quantity(responseDto.getQuantity())
                .build();
    }

}
