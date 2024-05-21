package com.hcmute.fastfoodsystem.controller;


import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import com.hcmute.fastfoodsystem.service.CategoryService;
import com.hcmute.fastfoodsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
    @GetMapping("/list-products")
    public String getProducts(Model model) {
        List<Product> products = productService.getAllProduct();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products", products);
        model.addAttribute("category", categories);
        return "admin/products";
    }
    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProductById(id);
        redirectAttributes.addAttribute("success", "Product deleted successfully!");
        return "redirect:/admin/list-products";
    }

    @PostMapping("/deleteSelectedProducts")
    public String deleteSelectedProducts(@RequestParam("selectedProductIds") List<Long> selectedProductIds, RedirectAttributes redirectAttributes) {
        productService.deleteProductsByIds(selectedProductIds);
        redirectAttributes.addAttribute("success", "Selected products deleted successfully!");
        return "redirect:/admin/list-products";
    }

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("productDto") ProductDto productDto,
                             @RequestParam("imageProduct") MultipartFile imageProduct,
                             RedirectAttributes redirectAttributes) throws IOException {
        String fileName = StringUtils.cleanPath(imageProduct.getOriginalFilename());
        if (!imageProduct.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageProduct.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            productDto.setImage("/img/" + fileName);
        } else {
            productDto.setImage("/img/image_default.png");
        }
        Product product = productService.addProduct(productDto);
        redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        return "redirect:/admin/list-products";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductDto productDto = new ProductDto(product);
            model.addAttribute("productDto", productDto);
            model.addAttribute("productId", id);
            List<Category> categories = categoryService.getAllCategory();
            model.addAttribute("categories", categories);
            return "admin/edit-product";
        } else {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/admin/list-products";
        }
    }

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes) throws IOException {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/admin/list-products";
        }
        Product existingProduct = optionalProduct.get();
        if (!imageProduct.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageProduct.getOriginalFilename());

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageProduct.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            productDto.setImage("/img/" + fileName);
        }  else {
            productDto.setImage(existingProduct.getImage());
        }
        productService.updateProduct(existingProduct, productDto);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        return "redirect:/admin/list-products";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String newCategory, RedirectAttributes redirectAttributes) {
        Category category = new Category();
        category.setCategory(newCategory);
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Category added successfully");
        return "redirect:/admin/list-products";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        List<Product> products = productService.getProductsByCategory(id);
        if (!products.isEmpty()) {
            redirectAttributes.addFlashAttribute("confirmDelete", "This category contains products. Are you sure you want to delete it?");
            redirectAttributes.addFlashAttribute("categoryIdToDelete", id);
            return "redirect:/admin/list-products";
        }
        categoryService.deleteCategoryById(id);

        redirectAttributes.addFlashAttribute("success", "Category deleted successfully");
        return "redirect:/admin/list-products";
    }

    @PostMapping("/deleteCategoryConfirmed/{id}")
    public String deleteCategoryConfirmed(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        List<Product> products = productService.getProductsByCategory(id);
        for (Product product : products) {
            productService.deleteProductById(product.getId());
        }
        categoryService.deleteCategoryById(id);
        redirectAttributes.addFlashAttribute("success", "Category and its products deleted successfully");
        return "redirect:/admin/list-products";
    }
}
