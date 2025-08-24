package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductResponseDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceResponseDto;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.model.Product;
import az.gadzhieva.springwork.product.model.invoice.Invoice;
import az.gadzhieva.springwork.product.model.invoice.InvoiceProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceResponseDto entityToResponse(Invoice invoice) {
        List<InvoiceProductResponseDto> newList = invoice.getItems() == null ? new ArrayList<>() :
                invoice.getItems().stream().map(InvoiceProductMapper::entityToResponse).collect(Collectors.toList());

        return InvoiceResponseDto.builder().
                id(invoice.getId()).
                date(invoice.getDate()).
                firmId(invoice.getFirm().getId()).
                firmName(invoice.getFirm().getName()).
                sum(invoice.getSum()).
                note(invoice.getNote()).
                items(newList).
                build();
    }
    public static InvoiceRequestDto responseToRequest(InvoiceResponseDto responseDto) {
        return InvoiceRequestDto.builder()
                .id(responseDto.getId())
                .date(responseDto.getDate())
                .firmId(responseDto.getFirmId())
                .note(responseDto.getNote())
                .items(responseDto.getItems().stream()
                        .map(InvoiceProductMapper::responseToRequest)
                        .collect(Collectors.toList()))
                .build();
    }
    public static void updateEntity(InvoiceRequestDto invoiceRequestDto, Invoice invoice,
                                    Firm firm){

        invoice.setDate(invoiceRequestDto.getDate());
        invoice.setFirm(firm);
        invoice.setNote(invoiceRequestDto.getNote());

        List<InvoiceProduct> updatedItems = new ArrayList<>();

        for (InvoiceProductRequestDto dto : invoiceRequestDto.getItems()) {
            if (dto.getId() == null) {
                // Новая строка
                updatedItems.add(InvoiceProductMapper.requestToEntity(dto, invoice));
            } else {
                // Старая строка
                InvoiceProduct existing = invoice.getItems().stream()
                        .filter(item -> item.getId().equals(dto.getId()))
                        .findFirst().orElse(null);

                if (existing != null) {
                    InvoiceProductMapper.updateEntity(dto, existing);
                    updatedItems.add(existing);
                }
            }
        }

        invoice.setItems(updatedItems); // Это важно: обновляем список целиком
        BigDecimal totalSum = updatedItems.stream()
                .map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setSum(totalSum);



    }


}
