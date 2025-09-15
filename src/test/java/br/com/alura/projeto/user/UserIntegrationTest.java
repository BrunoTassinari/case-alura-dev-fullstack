package br.com.alura.projeto.user;

import br.com.alura.projeto.user.dto.NewInstructorDTO;
import br.com.alura.projeto.user.dto.NewStudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void newStudent__should_be_created_when_data_is_valid() throws Exception {
        NewStudentDTO newStudent = new NewStudentDTO();
        newStudent.setName("Novo Aluno");
        newStudent.setEmail("aluno.novo@email.com");
        newStudent.setPassword("senha123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated());

        User createdUser = userRepository.findByEmail("aluno.novo@email.com").orElseThrow();
        assertThat(createdUser.getName()).isEqualTo("Novo Aluno");
        assertThat(createdUser.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void newStudent__should_return_conflict_when_email_already_exists() throws Exception {
        User userCreated = new User("Usuário Existente", "existente@email.com", Role.STUDENT, "123");
        userRepository.save(userCreated);

        NewStudentDTO duplicateStudent = new NewStudentDTO();
        duplicateStudent.setName("Outro Aluno");
        duplicateStudent.setEmail("existente@email.com");
        duplicateStudent.setPassword("senha456");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateStudent)))
                .andExpect(status().isConflict());
    }

    @Test
    void newInstructor__should_be_created_when_data_is_valid() throws Exception {
        NewInstructorDTO newInstructor = new NewInstructorDTO();
        newInstructor.setName("Novo Instrutor");
        newInstructor.setEmail("instrutor.novo@email.com");
        newInstructor.setPassword("senha789");

        mockMvc.perform(post("/user/newInstructor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInstructor)))
                .andExpect(status().isCreated());

        User createdUser = userRepository.findByEmail("instrutor.novo@email.com").orElseThrow();
        assertThat(createdUser.getName()).isEqualTo("Novo Instrutor");
        assertThat(createdUser.getRole()).isEqualTo(Role.INSTRUCTOR);
    }

    @Test
    void newUser__should_return_bad_request_when_dto_is_invalid() throws Exception {
        NewStudentDTO invalidStudent = new NewStudentDTO();
        invalidStudent.setName("");
        invalidStudent.setEmail("email-invalido");
        invalidStudent.setPassword("123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());
    }
}