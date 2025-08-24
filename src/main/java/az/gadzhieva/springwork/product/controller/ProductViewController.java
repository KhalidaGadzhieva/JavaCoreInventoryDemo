package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.dto.product.ProductRequestDto;
import az.gadzhieva.springwork.product.mapper.ProductMapper;
import az.gadzhieva.springwork.product.model.Category;
import az.gadzhieva.springwork.product.model.Unit;
import az.gadzhieva.springwork.product.security.UserProvider;
import az.gadzhieva.springwork.product.service.FirmService;
import az.gadzhieva.springwork.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductViewController {
    private final ProductService productService;
    private final FirmService firmService;
    private final UserProvider userProvider;


    //method for shows
    @GetMapping
    public String getAllProducts(Model model){
        model.addAttribute("productList",productService.getAllProducts());
        return "product/showProductAll";
    }
    @GetMapping("/get/{id}")
    public String getProductById(@PathVariable Long id, Model model){
        model.addAttribute("product",productService.getProduct(id));
        return "product/showProductById";
    }

    //method for created
    @GetMapping("/new")
    public String createProductForm(Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the product creation form");

        model.addAttribute("newProduct", new ProductRequestDto());
        model.addAttribute("categories", Category.values());
        model.addAttribute("unites", Unit.values());
        model.addAttribute("firms", firmService.getAllFirms());
        return "product/postProduct";
    }
    @PostMapping
    public String createProduct(@Valid @ModelAttribute ("newProduct") ProductRequestDto productRequestDto,
                                BindingResult bindingResult, Model model, HttpSession httpSession){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", Category.values());
            model.addAttribute("unites", Unit.values());
            model.addAttribute("firms", firmService.getAllFirms());
            return "product/postProduct";
        }
        productService.createProduct(productRequestDto,httpSession);
        return "redirect:/product";
    }

    //method for deleted
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession httpSession){
        productService.deleteProduct(id,httpSession);
        return "redirect:/product";
    }

    //method for updated
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, HttpSession httpSession){
        userProvider.checkIfAdminThenThrow(httpSession,"access the firm updated form");

        ProductRequestDto productRequestDto =
                ProductMapper.responseToRequest(productService.getProduct(id));
        model.addAttribute("updatedProduct", productRequestDto);
        model.addAttribute("productId",id);
        model.addAttribute("categories", Category.values());
        model.addAttribute("unites", Unit.values());
        model.addAttribute("firms", firmService.getAllFirms());
       return "product/editProduct";
    }
    @PatchMapping("/{id}")
    public String editProduct(@Valid @ModelAttribute ("updatedProduct")ProductRequestDto productRequestDto,
                              BindingResult bindingResult, Model model, @PathVariable Long id, HttpSession httpSession){
        productRequestDto.setId(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("productId",id);
            model.addAttribute("categories", Category.values());
            model.addAttribute("unites", Unit.values());
            model.addAttribute("firms", firmService.getAllFirms());
            return "product/editProduct";
        }
        productService.updateProduct(id, productRequestDto,httpSession);
        return "redirect:/product";
    }
}
