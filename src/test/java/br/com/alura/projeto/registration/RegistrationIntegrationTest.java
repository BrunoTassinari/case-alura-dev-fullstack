package br.com.alura.projeto.registration;

import br.com.alura.projeto.registration.dto.NewRegistrationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private User student;
    private User instructor;
    private Course activeCourse;
    private Course inactiveCourse;

    @BeforeEach
    void setUp() {
        this.student = new User("Aluno Teste", "aluno@email.com", Role.STUDENT, "123456");
        userRepository.save(student);

        this.instructor = new User("Instrutor Teste", "instrutor@email.com", Role.INSTRUCTOR,"123456");
        userRepository.save(instructor);

        Category category = new Category("Testes", "testes", "#333333", 1);
        categoryRepository.save(category);

        this.activeCourse = new Course("Curso Ativo", "ativo", instructor, category, "Curso Ativo");
        courseRepository.save(activeCourse);

        this.inactiveCourse = new Course("Curso Inativo", "inativo", instructor, category, "Curso Inativo");
        inactiveCourse.inactivate();
        courseRepository.save(inactiveCourse);
    }

    @Test
    void newRegistration__should_be_created_when_data_is_valid() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail(student.getEmail());
        newRegistration.setCourseCode(activeCourse.getCode());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isCreated());

        boolean exists = registrationRepository.existsByStudentAndCourse(student, activeCourse);
        assertThat(exists).isTrue();
    }

    @Test
    void newRegistration__should_return_bad_request_when_dto_is_invalid() throws Exception {
        NewRegistrationDTO invalidRegistration = new NewRegistrationDTO();
        invalidRegistration.setStudentEmail("email-invalido");
        invalidRegistration.setCourseCode(null);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegistration)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newRegistration__should_return_bad_request_when_registration_already_exists() throws Exception {
        registrationRepository.save(new Registration(student, activeCourse));

        NewRegistrationDTO duplicateRegistration = new NewRegistrationDTO();
        duplicateRegistration.setStudentEmail(student.getEmail());
        duplicateRegistration.setCourseCode(activeCourse.getCode());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRegistration)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newRegistration__should_return_bad_request_when_course_is_inactive() throws Exception {
        NewRegistrationDTO registrationForInactiveCourse = new NewRegistrationDTO();
        registrationForInactiveCourse.setStudentEmail(student.getEmail());
        registrationForInactiveCourse.setCourseCode(inactiveCourse.getCode());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationForInactiveCourse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newRegistration__should_return_bad_request_when_user_is_not_a_student() throws Exception {
        NewRegistrationDTO registrationWithInstructor = new NewRegistrationDTO();
        registrationWithInstructor.setStudentEmail(instructor.getEmail());
        registrationWithInstructor.setCourseCode(activeCourse.getCode());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationWithInstructor)))
                .andExpect(status().isBadRequest());
    }
}