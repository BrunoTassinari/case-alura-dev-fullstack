package br.com.alura.projeto.login;

import br.com.alura.projeto.category.CategoryService;
import br.com.alura.projeto.category.dto.CategoryWithCoursesDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    private final CategoryService categoryService;

    public LoginController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<CategoryWithCoursesDTO> categories = categoryService.getCourseCategoriesForLoginPage();
        model.addAttribute("categories", categories);

        return "login";
    }
}
