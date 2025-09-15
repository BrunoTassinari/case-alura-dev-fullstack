package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void newCategory__should_be_created_when_data_is_valid() throws Exception {
        String createdCode = "code";

        mockMvc.perform(post("/admin/category/new")
                        .param("name", "Nova Categoria")
                        .param("code", createdCode)
                        .param("color", "#FF0000")
                        .param("order", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        boolean exists = categoryRepository.existsByCode(createdCode);
        assertThat(exists).isTrue();
    }

    @Test
    void newCategory__should_return_form_with_errors_when_data_is_invalid() throws Exception {
        String createdCode = "code";

        mockMvc.perform(post("/admin/category/new")
                        .param("name", "")
                        .param("code", createdCode))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("categoryForm", "name"));
    }

    @Test
    void newCategory__should_return_form_with_errors_when_code_already_exists() throws Exception {
        String createdCode = "code";
        Category existingCategory = new Category("Programacao", createdCode, "#0000FF", 1);
        categoryRepository.save(existingCategory);

        mockMvc.perform(post("/admin/category/new")
                        .param("name", "Devops")
                        .param("code", createdCode)
                        .param("color", "#FF0000")
                        .param("order", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/form"))
                .andExpect(model().attributeHasErrors("categoryForm"))
                .andExpect(model().attributeErrorCount("categoryForm", 1));
    }

    @Test
    void updateCategory_should_update_category_when_data_is_valid() throws Exception {
        String createdCode = "code";
        Category categoryToUpdate = new Category("Nome Antigo", createdCode, "#111111", 1);
        categoryRepository.save(categoryToUpdate);

        mockMvc.perform(post("/admin/category/edit/" + categoryToUpdate.getId())
                        .param("name", "Nome Novo e Atualizado")
                        .param("code", "other")
                        .param("color", "#222222")
                        .param("order", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        Category updatedCategory = categoryRepository.findById(categoryToUpdate.getId()).orElseThrow();
        assertThat(updatedCategory.getName()).isEqualTo("Nome Novo e Atualizado");
        assertThat(updatedCategory.getColor()).isEqualTo("#222222");
        assertThat(updatedCategory.getOrder()).isEqualTo(5);

        assertThat(updatedCategory.getCode()).isEqualTo(createdCode);
    }
}
