package br.com.alura.projeto.category;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    private Category category;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "programming", "#6BD1FF", 1);
        categoryRepository.save(category);
    }

    @Test
    void updateInfoCategory_should_update_category_when_data_is_valid() throws Exception {
        String categoryCode = "programming";

        mockMvc.perform(post("/admin/category/edit/{code}", categoryCode)
                        .param("name", "DevOps Novo")
                        .param("color", "#000000")
                        .param("order", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void updateInfoCategory_should_return_form_with_errors_when_data_is_invalid() throws Exception {
        String categoryCode = "programming";

        mockMvc.perform(post("/admin/category/edit/{code}", categoryCode)
                        .param("name", "")
                        .param("color", "#000000")
                        .param("order", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/edit/{code}"))
                .andExpect(model().hasErrors());
    }

    @Test
    void updateInfoCategory_should_return_not_found_when_code_does_not_exist() throws Exception {
        String nonExistentCode = "swift";

        when(categoryRepository.findByCode(nonExistentCode)).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/category/edit/{code}", nonExistentCode)
                        .param("name", "Swift")
                        .param("color", "#000000")
                        .param("order", "1"))
                .andExpect(status().isNotFound());
    }
}
