package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.employee.EmployeeRequestDto;
import az.gadzhieva.springwork.product.dto.employee.EmployeeResponseDto;
import az.gadzhieva.springwork.product.model.Employee;

public class EmployeeMapper {
    public static void updateEntity(EmployeeRequestDto employeeRequestDto, Employee employee) {
        employee.setFirstName(employeeRequestDto.getFirstName());
        employee.setLastName(employeeRequestDto.getLastName());
        employee.setPosition(employeeRequestDto.getPosition());
        employee.setBirthDate(employeeRequestDto.getBirthDate());
        employee.setPassportNumber(employeeRequestDto.getPassportNumber());
        employee.setHireDate(employeeRequestDto.getHireDate());
        employee.setHireOrderNumber(employeeRequestDto.getHireOrderNumber());
        employee.setEmail(employeeRequestDto.getEmail());
        employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
        employee.setWorkplace(employeeRequestDto.getWorkplace());
        employee.setDepartment(employeeRequestDto.getDepartment());
        employee.setAddress(employeeRequestDto.getAddress());
        employee.setNotes(employeeRequestDto.getNotes());
    }

    public static Employee requestToEntity(EmployeeRequestDto employeeRequestDto) {
        return Employee.builder().
                firstName(employeeRequestDto.getFirstName()).
                lastName(employeeRequestDto.getLastName()).
                position(employeeRequestDto.getPosition()).
                birthDate(employeeRequestDto.getBirthDate()).
                passportNumber(employeeRequestDto.getPassportNumber()).
                hireDate(employeeRequestDto.getHireDate()).
                hireOrderNumber(employeeRequestDto.getHireOrderNumber()).
                email(employeeRequestDto.getEmail()).
                phoneNumber(employeeRequestDto.getPhoneNumber()).
                workplace(employeeRequestDto.getWorkplace()).
                department(employeeRequestDto.getDepartment()).
                address(employeeRequestDto.getAddress()).
                notes(employeeRequestDto.getNotes()).
                build();
    }

    public static EmployeeRequestDto responseToRequest(EmployeeResponseDto employeeResponseDto){
        return EmployeeRequestDto.builder().
                firstName(employeeResponseDto.getFirstName()).
                lastName(employeeResponseDto.getLastName()).
                position(employeeResponseDto.getPosition()).
                birthDate(employeeResponseDto.getBirthDate()).
                passportNumber(employeeResponseDto.getPassportNumber()).
                hireDate(employeeResponseDto.getHireDate()).
                hireOrderNumber(employeeResponseDto.getHireOrderNumber()).
                email(employeeResponseDto.getEmail()).
                phoneNumber(employeeResponseDto.getPhoneNumber()).
                workplace(employeeResponseDto.getWorkplace()).
                department(employeeResponseDto.getDepartment()).
                address(employeeResponseDto.getAddress()).
                notes(employeeResponseDto.getNotes()).
                build();
    }
    public static EmployeeResponseDto entityToResponse(Employee employee) {
        return EmployeeResponseDto.builder().
                id(employee.getId()).
                firstName(employee.getFirstName()).
                lastName(employee.getLastName()).
                position(employee.getPosition()).
                birthDate(employee.getBirthDate()).
                passportNumber(employee.getPassportNumber()).
                hireDate(employee.getHireDate()).
                hireOrderNumber(employee.getHireOrderNumber()).
                email(employee.getEmail()).
                phoneNumber(employee.getPhoneNumber()).
                workplace(employee.getWorkplace()).
                department(employee.getDepartment()).
                address(employee.getAddress()).
                notes(employee.getNotes()).
                build();
    }
}
