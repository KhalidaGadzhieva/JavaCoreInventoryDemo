package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.dto.firm.FirmRequestDto;
import az.gadzhieva.springwork.product.dto.firm.FirmResponseDto;
import az.gadzhieva.springwork.product.mapper.FirmMapper;
import az.gadzhieva.springwork.product.model.Firm;
import az.gadzhieva.springwork.product.security.UserProvider;
import az.gadzhieva.springwork.product.service.FirmService;
import az.gadzhieva.springwork.product.service.InvoiceService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/firm")
@RequiredArgsConstructor
public class FirmViewController {
    private final FirmService firmService;
    private final InvoiceService invoiceService;
    private final UserProvider userProvider;

    //methods for show
    @GetMapping
    public String getAllFirms(Model model){
        model.addAttribute("firmList",firmService.getAllFirms());
        return "firm/showFirmAll";
    }
    @GetMapping("/get/{id}")
    public String showFirmById(@PathVariable Long id, Model model){
        model.addAttribute("firm",firmService.getFirmById(id));
        return "firm/showFirmById";
    }

    //methods for created
    @GetMapping("/new")
    public String postForm(Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the firm creation form");

        model.addAttribute("newFirm",new FirmRequestDto());
        return "firm/postFirm";
    }

    @PostMapping()
    public String postFirma(@Valid @ModelAttribute("newFirm") FirmRequestDto firmRequestDto,
                            BindingResult bindingResult, HttpSession httpSession){
        if(bindingResult.hasErrors()){
            return "firm/postFirm";
        }
        firmService.createFirm(firmRequestDto, httpSession);
        return "redirect:/firm";
    }

    //method for deleted
    @DeleteMapping("/delete/{id}")
    public String deleteFirm(@PathVariable Long id, HttpSession httpSession){
        firmService.deleteFirm(id, httpSession);
        return "redirect:/firm";
    }

    //method for updated
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable Long id, Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the firm updated form");

        FirmRequestDto firmRequestDto =
                FirmMapper.responseToRequest(firmService.getFirmById(id));
        model.addAttribute("updatedFirm", firmRequestDto);
        model.addAttribute("firmId",id);
        return "firm/editFirm";
    }
    @PatchMapping("/{id}")
    public String updateFirm(@PathVariable Long id, Model model,
                             @Valid @ModelAttribute ("updatedFirm") FirmRequestDto firmRequestDto,
                             BindingResult bindingResult, HttpSession httpSession){
        firmRequestDto.setId(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("firmId",id);
            return "firm/editFirm";
        }
        firmService.updateFirm(id,firmRequestDto, httpSession);
        return "redirect:/firm";
    }

    @GetMapping("/{id}/invoices")
    public String viewInvoicesForFirm(@PathVariable Long id, Model model) {
        FirmResponseDto firmResponseDto = firmService.getFirmById(id);
        FirmRequestDto firmRequestDto = FirmMapper.responseToRequest(firmResponseDto);
        Firm firm =FirmMapper.requestToEntity(firmRequestDto);
        model.addAttribute("firm", firm);
        model.addAttribute("invoices", invoiceService.getInvoicesForFirm(id));
        return "invoice/showAllInvoices"; // или как ты назовёшь шаблон
    }



}
