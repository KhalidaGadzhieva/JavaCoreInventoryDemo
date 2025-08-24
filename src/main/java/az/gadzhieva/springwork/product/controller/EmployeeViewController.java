package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.dto.employee.EmployeeRequestDto;
import az.gadzhieva.springwork.product.mapper.EmployeeMapper;
import az.gadzhieva.springwork.product.security.UserProvider;
import az.gadzhieva.springwork.product.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeViewController {
    private final EmployeeService employeeService;
    private final UserProvider userProvider;

    //methods for show
    @GetMapping
    public String getAllEmployees(Model model){
        model.addAttribute("employeeList", employeeService.getAllEmployees());
        return "employee/showAllEmployee";
    }
    @GetMapping("/get/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model){
        model.addAttribute("employee",employeeService.getEmployeeById(id));
        return "employee/showEmployeeById";
    }

    //methods for created
    @GetMapping("/new")
    public String postEmployeeForm(Model model, HttpSession httpSession) {
        userProvider.checkIfAdminThenThrow(httpSession,"access the employee creation form");

        model.addAttribute("newEmployee", new EmployeeRequestDto());
        return "employee/postEmployeeForm";
    }
    @PostMapping
    public String postEmployee(@Valid @ModelAttribute ("newEmployee") EmployeeRequestDto employeeRequestDto,
                               BindingResult bindingResult, HttpSession httpSession){
        if(bindingResult.hasErrors()){
            return "employee/postEmployeeForm";
        }

        employeeService.createEmployee(employeeRequestDto,httpSession);
        return "redirect:/employee";
    }

    //method for deleted
    @DeleteMapping("/delete/{id}")
    public String deleteEmployeeById(@PathVariable Long id, HttpSession httpSession){
        employeeService.deleteEmployee(id,httpSession);
        return "redirect:/employee";
    }

    //methods for updated
    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the firm updated form");

        EmployeeRequestDto employeeRequestDto =
                EmployeeMapper.responseToRequest(employeeService.getEmployeeById(id));
        model.addAttribute("updatedEmployee",employeeRequestDto);
        model.addAttribute("EmployeeId",id);
        return "employee/editEmployeeForm";
    }
    @PatchMapping("/{id}")
    public String editEmployee(@Valid @ModelAttribute ("updatedEmployee") EmployeeRequestDto employeeRequestDto,
                               BindingResult bindingResult,@PathVariable Long id, Model model,
                               HttpSession httpSession){
        if(bindingResult.hasErrors()){
            model.addAttribute("EmployeeId",id);
            return "employee/editEmployeeForm";
        }
        employeeService.updateEmployee(id,employeeRequestDto, httpSession);
        return "redirect:/employee";
    }
}
