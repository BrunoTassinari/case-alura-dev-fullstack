package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    private User instructor;
    private Category category;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserRepository  userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        instructor = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "password");
        userRepository.save(instructor);

        category = new Category("Programming", "programming", "#6BD1FF", 1);
        categoryRepository.save(category);
    }

    @Test
    void inactivateCourse__should_inactivate_course_request_is_valid() throws Exception{
        String courseCode = "java-basico";
        Course course = new Course("Java Básico", courseCode, instructor, category, "Curso de introdução");
        courseRepository.save(course);

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
        courseRepository.save(course);

        doThrow(new DataConflictException(expectedErrorMessage))
                .when(courseService)
                .inactivateCourse(courseCode);

        mockMvc.perform(post("/course/{code}/inactive", courseCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
}
