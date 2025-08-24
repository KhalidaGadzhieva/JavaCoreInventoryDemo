package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.EmployeeDataJpaRepository;
import az.gadzhieva.springwork.product.dto.employee.EmployeeRequestDto;
import az.gadzhieva.springwork.product.dto.employee.EmployeeResponseDto;
import az.gadzhieva.springwork.product.error.EmployeeNotFoundException;
import az.gadzhieva.springwork.product.mapper.EmployeeMapper;
import az.gadzhieva.springwork.product.model.Employee;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.security.UserProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeDataJpaRepository employeeDataJpaRepository;
    private final UserProvider userProvider;


    public EmployeeResponseDto getEmployeeById(Long id) {
        log.debug("[DEBUG] Searching for employee with id={}", id);
        Employee employee = findEmployeeByIdAndUser(id);
        EmployeeResponseDto employeeResponseDto = EmployeeMapper.entityToResponse(employee);
        log.debug("[DEBUG] Employee mapped to EmployeeResponseDto");

        log.info("[GET] Employee with id={} was successfully retrieved.", id);

        return employeeResponseDto;
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        log.debug("[DEBUG] Fetching all employees from database...");

        User currentUser = userProvider.getCurrentUser();// достаём текущего юзера

        List<Employee> employees= userProvider.isAdmin(currentUser)?
                employeeDataJpaRepository.findAll() :
                employeeDataJpaRepository.findAllByUser(currentUser);
        log.info("[GET] Retrieved {} employee(s) from database.", employees.size());

        List<EmployeeResponseDto> response = employees.stream()
                .map(EmployeeMapper::entityToResponse)
                .collect(Collectors.toList());

        log.debug("[DEBUG] Mapped {} employee(s) to EmployeeResponseDto list.", response.size());
        return response;
    }

    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto, HttpSession httpSession)  {
        userProvider.checkIfAdminThenThrow(httpSession,"create an employee.");

        log.debug("[DEBUG] Creating new employee.");
        User currentUser = userProvider.getCurrentUser();         // достаём текущего юзера


        Employee employee = EmployeeMapper.requestToEntity(employeeRequestDto);

        employee.setUser(currentUser); // привязываем к текущему юзеру

        log.debug("[DEBUG] EmployeeRequestDto map to Employee.");
        employee = employeeDataJpaRepository.save(employee);
        log.info("[CREATE] Create employee with id={}", employee.getId());
        log.debug("[DEBUG] Saved employee: id={}, name={}", employee.getId(), employee.getFirstName());

        EmployeeResponseDto employeeResponseDto = EmployeeMapper.entityToResponse(employee);
        log.debug("[DEBUG] Employee mapped to EmployeeResponseDto.");
        return employeeResponseDto;

    }

    public void deleteEmployee(Long id, HttpSession httpSession)  {
        log.debug("[DELETE] Deleting employee with id={}", id);

      userProvider.checkIfAdminThenThrow(httpSession,"delete an employee.");

        Employee employee = findEmployeeByIdAndUser(id);
        employeeDataJpaRepository.delete(employee);
        log.info("[DELETE] Employee with id={} is deleted.", id);
    }

    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto, HttpSession httpSession) {
        log.debug("[DEBUG] Updating employee with id={}", id);

        userProvider.checkIfAdminThenThrow(httpSession,"update an employee.");

        Employee employee = findEmployeeByIdAndUser(id);
        EmployeeMapper.updateEntity(employeeRequestDto, employee);

        employee = employeeDataJpaRepository.save(employee);
        log.info("[UPDATE] Employee with id={} is updated.", id);
        return EmployeeMapper.entityToResponse(employee);
    }

    Employee findEmployeeByIdAndUser(Long id) {
        User currentUser = userProvider.getCurrentUser();         // достаём текущего юзера
        Employee employee=(userProvider.isAdmin(currentUser)?
                 employeeDataJpaRepository.findById(id) :
                employeeDataJpaRepository.findByIdAndUser(id, currentUser))
                        .orElseThrow(() -> {
                        log.error("[ERROR] Employee with id={} is not found for current user.", id);
                        return new EmployeeNotFoundException("Employee with id=" + id + " is not found.");
                    });

        log.debug("[DEBUG] Employee found with id={}, name={}", employee.getId(), employee.getFirstName());
        return employee;
    }


}
