package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryForm;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    private User instructor;
    private Category category;
    private Course course;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        instructor = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "password");
        category = new Category("Programming", "programming", "#6BD1FF", 1);
        course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");
        ReflectionTestUtils.setField(course, "id", 1L);
    }

    @Test
    void inactivateCourse__should_inactivate_course_request_is_valid() throws Exception{
        String courseCode = "java-basico";
        Course course = new Course("Java Básico", courseCode, instructor, category, "Curso de introdução");

        mockMvc.perform(post("/course/{code}/inactive", courseCode))
                .andExpect(status().isOk());
    }

    @Test
    void inactivateCourse__should_return_not_found_when_course_not_exists() throws Exception{
        String courseCode = "java-intermediario";
        String expectedErrorMessage = "Course not found";

        doThrow(new ResourceNotFoundException("Course not found"))
                .when(courseService)
                .inactivateCourse(courseCode);

        mockMvc.perform(post("/course/{code}/inactive", courseCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void inactivateCourse__should_return_conflict_when_course_already_inactivated() throws Exception{
        String courseCode = "java-basico";
        String expectedErrorMessage = "Course already inactive";
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");
        course.inactivate();

        doThrow(new DataConflictException(expectedErrorMessage))
                .when(courseService)
                .inactivateCourse(courseCode);

        mockMvc.perform(post("/course/{code}/inactive", courseCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void updateCourse__should_update_category_when_data_is_valid_and_redirect() throws Exception {
        mockMvc.perform(post("/admin/course/edit/{id}", course.getId())
                        .param("name", "DevOps 2025")
                        .param("description", "Novo curso")
                        .param("active", "false"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void updateCourse__should_return_form_with_errors_when_data_is_invalid() throws Exception {
        mockMvc.perform(post("/admin/course/edit/{id}", course.getId())
                        .param("name", "")
                        .param("description", "Novo curso"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/edit/{id}"))
                .andExpect(model().hasErrors());
    }

    @Test
    void updateCourse__should_return_not_found_when_id_does_not_exist() throws Exception {
        Long nonExistentId = 3L;

        doThrow(new ResourceNotFoundException("Course not found")).when(courseService).updateCourse(any(CourseForm.class));

        mockMvc.perform(post("/admin/course/edit/{id}", nonExistentId)
                        .param("name", "Swift")
                        .param("color", "#000000")
                        .param("order", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/edit/{id}"));
    }
}
