package br.com.alura.projeto.category;

import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import br.com.alura.projeto.validation.OnCreate;
import br.com.alura.projeto.validation.OnUpdate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String list(Model model) {
        List<CategoryDTO> list = categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();

        model.addAttribute("categories", list);

        return "admin/category/list";
    }

    @GetMapping("/admin/category/new")
    public String create(CategoryForm newCategory, Model model) {
        return "admin/category/form";
    }

    @Transactional
    @PostMapping("/admin/category/new")
    public String save(@Validated(OnCreate.class) CategoryForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return create(form, model);
        }

        if (categoryRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }

        categoryRepository.save(form.toModel());
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);

        model.addAttribute("categoryForm", new CategoryForm(category));
        return "admin/category/form";
    }

    @PostMapping("/admin/category/edit/{id}")
    public String update(@PathVariable Long id, @Validated(OnUpdate.class) CategoryForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categoryForm", form);
            return "admin/category/edit/{id}";
        }

        try {
            categoryService.updateCategory(form);
        } catch (ResourceNotFoundException ex) {
            result.reject("business.error", ex.getMessage());
            model.addAttribute("categoryForm", form);
            return "admin/category/edit/{id}";
        }

        return "redirect:/admin/categories";
    }
}
