package br.com.alura.projeto.category;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "programming", "#6BD1FF", 1);
        ReflectionTestUtils.setField(category, "id", 1L);
    }

    @Test
    void updateInfoCategory__should_update_category_when_data_is_valid_and_redirect() throws Exception {
        mockMvc.perform(post("/admin/category/edit/{id}", category.getId())
                        .param("name", "DevOps Novo")
                        .param("color", "#000000")
                        .param("order", "2"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void updateInfoCategory__should_return_form_with_errors_when_data_is_invalid() throws Exception {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        mockMvc.perform(post("/admin/category/edit/{id}", category.getId())
                        .param("name", "")
                        .param("color", "#000000")
                        .param("order", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    void updateInfoCategory__should_return_not_found_when_id_does_not_exist() throws Exception {
        Long nonExistentId = 3L;

        doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).updateCategory(any(CategoryForm.class));

        mockMvc.perform(post("/admin/category/edit/{id}", nonExistentId)
                        .param("name", "Swift")
                        .param("color", "#000000")
                        .param("order", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"));
    }
}
