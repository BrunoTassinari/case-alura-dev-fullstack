package br.com.alura.projeto.user;

import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.user.dto.NewStudentDTO;
import br.com.alura.projeto.user.dto.UserListItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newStudent__should_return_bad_request_when_password_is_blank() throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setEmail("test@test.com");
        newStudentDTO.setName("Charles");
        newStudentDTO.setPassword("");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_bad_request_when_email_is_blank() throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setEmail("");
        newStudentDTO.setName("Charles");
        newStudentDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setEmail("Charles");
        newStudentDTO.setName("Charles");
        newStudentDTO.setPassword("mudar123");

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
    }

    @Test
    void newStudent__should_return_conflict_when_email_already_exists() throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setEmail("charles@alura.com.br");
        newStudentDTO.setName("Charles");
        newStudentDTO.setPassword("mudar123");

        doThrow(new DataConflictException("Email already exists")).when(userService).createUser(any(NewStudentDTO.class));

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void newStudent__should_return_created_when_user_request_is_valid() throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setEmail("charles@alura.com.br");
        newStudentDTO.setName("Charles");
        newStudentDTO.setPassword("mudar123");

        when(userService.existsByEmail(newStudentDTO.getEmail())).thenReturn(false);

        mockMvc.perform(post("/user/newStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void listAllUsers__should_list_all_users() throws Exception {
        User user1 = new User("User 1", "user1@test.com", Role.STUDENT,"mudar123");
        User user2 = new User("User 2", "user2@test.com",Role.STUDENT,"mudar123");

        UserListItemDTO userDto1 = new UserListItemDTO(user1);
        UserListItemDTO userDto2 = new UserListItemDTO(user2);


        when(userService.listUsers()).thenReturn(Arrays.asList(userDto1, userDto2));

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

}