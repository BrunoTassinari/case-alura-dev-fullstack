package br.com.alura.projeto.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User instructor;
    private Category category;

    @BeforeEach
    void setUp() {
        this.instructor = new User("Instrutor", "instrutor@email.com", Role.INSTRUCTOR, "123456");
        userRepository.save(instructor);

        this.category = new Category("Programação", "program", "#0000FF", 1);
        categoryRepository.save(category);
    }

    @Test
    void newCourse__should_be_created_when_data_is_valid() throws Exception {
        mockMvc.perform(post("/admin/course/new")
                        .param("name", "Curso de Java")
                        .param("code", "java-oo")
                        .param("instructorId", instructor.getId().toString())
                        .param("categoryId", category.getId().toString())
                        .param("description", "Aprenda Java do zero"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        assertThat(courseRepository.existsByCode("java-oo")).isTrue();
    }

    @Test
    void newCourse__should_return_form_when_code_is_invalid_pattern() throws Exception {
        mockMvc.perform(post("/admin/course/new")
                        .param("name", "Curso de Java")
                        .param("code", "Java12")
                        .param("instructorId", instructor.getId().toString())
                        .param("categoryId", category.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("courseForm", "code"));
    }

    @Test
    void newCourse__should_return_form_when_code_already_exists() throws Exception {
        String code = "created";

        Course existingCourse = new Course("Curso Existente", code, instructor, category, "Curso de Java");
        courseRepository.save(existingCourse);

        mockMvc.perform(post("/admin/course/new")
                        .param("name", "Outro Curso")
                        .param("code", code)
                        .param("instructorId", instructor.getId().toString())
                        .param("categoryId", category.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/form"))
                .andExpect(model().attributeHasErrors("courseForm"));
    }

    @Test
    void newCourse__should_return_form_when_user_is_not_instructor() throws Exception {
        User student = new User("Estudante", "student@email.com", Role.STUDENT, "123456");
        userRepository.save(student);

        mockMvc.perform(post("/admin/course/new")
                        .param("name", "Curso Inválido")
                        .param("code", "code")
                        .param("instructorId", student.getId().toString())
                        .param("categoryId", category.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/form"))
                .andExpect(model().attributeHasErrors("courseForm"));
    }

    @Test
    void updateCourse__should_be_updated_when_data_is_valid() throws Exception {
        String code = "created";

        Course courseToUpdate = new Course("Nome Antigo", code, instructor, category, "Descricao antiga");
        courseRepository.save(courseToUpdate);

        Category newCategory = new Category("DevOps", "devops", "#FFFFFF", 4);
        categoryRepository.save(newCategory);

        String newName = "Nome novo";
        String newCode = "updated";
        String newDescription = "Descricao nova";

        mockMvc.perform(post("/admin/course/edit/" + courseToUpdate.getId())
                        .param("name", newName)
                        .param("code", newCode)
                        .param("categoryId", newCategory.getId().toString())
                        .param("instructorId", instructor.getId().toString())
                        .param("description", newDescription)
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        Course updatedCourse = courseRepository.findById(courseToUpdate.getId()).orElseThrow();
        assertThat(updatedCourse.getName()).isEqualTo(newName);
        assertThat(updatedCourse.getCategory().getName()).isEqualTo(newCategory.getName());
        assertThat(updatedCourse.getDescription()).isEqualTo(newDescription);

        assertThat(updatedCourse.getCode()).isNotEqualTo(newCode);
        assertThat(updatedCourse.getCode()).isEqualTo(code);
    }
}