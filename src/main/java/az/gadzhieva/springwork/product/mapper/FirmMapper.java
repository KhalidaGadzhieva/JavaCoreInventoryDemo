package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.firm.FirmRequestDto;
import az.gadzhieva.springwork.product.dto.firm.FirmResponseDto;
import az.gadzhieva.springwork.product.model.Firm;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FirmMapper {
    public static void updateEntity(FirmRequestDto firmRequestDto, Firm firm) {
        firm.setName(firmRequestDto.getName());
        firm.setInn(firmRequestDto.getInn());
        firm.setMobileNumber(firmRequestDto.getMobileNumber());
        firm.setEmail(firmRequestDto.getEmail());
        firm.setAddress(firmRequestDto.getAddress());
    }

    public static FirmRequestDto responseToRequest(FirmResponseDto firmResponseDto) {
        return FirmRequestDto.builder().
                name(firmResponseDto.getName()).
                inn(firmResponseDto.getInn()).
                address(firmResponseDto.getAddress()).
                mobileNumber(firmResponseDto.getMobileNumber()).
                email(firmResponseDto.getEmail()).
                build();
    }

    public static Firm requestToEntity(FirmRequestDto firmRequestDto) {
        return Firm.builder().
                name(firmRequestDto.getName()).
                inn(firmRequestDto.getInn()).
                email(firmRequestDto.getEmail()).
                mobileNumber(firmRequestDto.getMobileNumber()).
                address(firmRequestDto.getAddress()).build();
    }
    public static FirmResponseDto entityToResponse(Firm firm){
        return FirmResponseDto.builder().
                id(firm.getId()).
                email(firm.getEmail()).
                name(firm.getName()).
                inn(firm.getInn()).
                mobileNumber(firm.getMobileNumber()).
                address(firm.getAddress()).
                outstandingDebt(firm.getOutstandingDebt()).
                items(firm.getItems() == null ? new ArrayList<>() :
                        firm.getItems().stream().map(InvoiceMapper::entityToResponse).collect(Collectors.toList())).
                build();

    }
}
