package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.dto.invoice.InvoiceProductRequestDto;
import az.gadzhieva.springwork.product.dto.invoice.InvoiceRequestDto;
import az.gadzhieva.springwork.product.mapper.InvoiceMapper;
import az.gadzhieva.springwork.product.security.UserProvider;
import az.gadzhieva.springwork.product.service.FirmService;
import az.gadzhieva.springwork.product.service.InvoiceService;
import az.gadzhieva.springwork.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final FirmService firmService;
    private final ProductService productService;
    private final UserProvider userProvider;

    //methods for show
    @GetMapping
    public String getAllInvoice(Model model){
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "invoice/showAllInvoices";
    }
    @GetMapping("/get/{id}")
    public String getInvoiceById(@PathVariable Long id,Model model){
        model.addAttribute("invoice",invoiceService.getInvoiceById(id));
        return "invoice/showInvoiceById";
    }

    //methods for created
    @GetMapping("/new")
    public String createInvoiceForm(Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the invoice creation form");

        model.addAttribute("newInvoice", new InvoiceRequestDto());
        model.addAttribute("firms", firmService.getAllFirms());
        model.addAttribute("products", productService.getAllProducts());

        return "invoice/postInvoiceForm";
    }
    @PostMapping
    public String createInvoice(@Valid @ModelAttribute ("newInvoice") InvoiceRequestDto invoiceRequestDto,
                                BindingResult bindingResult, Model model, HttpSession httpSession){
        invoiceRequestDto.setItems(invoiceService.filterEmptyItems(invoiceRequestDto.getItems()));

        if(bindingResult.hasErrors()){
            model.addAttribute("firms", firmService.getAllFirms());
            model.addAttribute("products", productService.getAllProducts());
            List<InvoiceProductRequestDto> items = invoiceRequestDto.getItems();
            if (items.size() < 7) {
                int missing = 7 - items.size();
                for (int i = 0; i < missing; i++) {
                    items.add(new InvoiceProductRequestDto());
                }
            }

            return "invoice/postInvoiceForm";
        }
        invoiceService.createInvoice(invoiceRequestDto,httpSession);
        return "redirect:/invoice";
    }

    //method for deleted
    @DeleteMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id, HttpSession httpSession){
        invoiceService.deleteInvoice(id,httpSession);
        return "redirect:/invoice";
    }

    //methods for updated
    @GetMapping("/edit/{id}")
    public String updateInvoiceForm(@PathVariable Long id, Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the firm updated form");

        InvoiceRequestDto invoiceRequestDto =
                InvoiceMapper.responseToRequest(invoiceService.getInvoiceById(id));
        model.addAttribute("editInvoice",invoiceRequestDto);
        model.addAttribute("invoiceId",id);
        model.addAttribute("firms", firmService.getAllFirms());
        model.addAttribute("products", productService.getAllProducts());

        return "invoice/editInvoiceForm";
    }
    @PatchMapping("/{id}")
    public String updateInvoice(@Valid @ModelAttribute ("editInvoice") InvoiceRequestDto invoiceRequestDto,
                                BindingResult bindingResult, @PathVariable Long id,
                                Model model, HttpSession httpSession){
        invoiceRequestDto.setItems(invoiceService.filterEmptyItems(invoiceRequestDto.getItems()));

        if(bindingResult.hasErrors()){
            model.addAttribute("invoiceId",id);
            model.addAttribute("firms", firmService.getAllFirms());
            model.addAttribute("products", productService.getAllProducts());
            List<InvoiceProductRequestDto> items = invoiceRequestDto.getItems();
            if (items.size() < 7) {
                int missing = 7 - items.size();
                for (int i = 0; i < missing; i++) {
                    items.add(new InvoiceProductRequestDto());
                }
            }
            return "invoice/editInvoiceForm";
        }
        invoiceService.updateInvoice(id,invoiceRequestDto,httpSession);
        return "redirect:/invoice";
    }


}
