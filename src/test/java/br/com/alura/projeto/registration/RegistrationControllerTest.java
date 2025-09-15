package br.com.alura.projeto.registration;

import br.com.alura.projeto.exceptions.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void newRegistration__should_create_new_registration() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("helena.student@example.com");
        newRegistrationDTO.setCourseCode("dotnet");

        doNothing().when(registrationService).createRegistration(any(NewRegistrationDTO.class));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void newRegistration__should_return_bad_request_when_user_is_not_student() throws Exception {
        String errorMessage = "User must be an student to registration into a course";
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("helena.student@example.com");
        newRegistrationDTO.setCourseCode("dotnet");

        doThrow(new BusinessException(errorMessage)).when(registrationService).createRegistration(any(NewRegistrationDTO.class));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }


    @Test
    void newRegistration__should_return_bad_request_when_course_is_not_active() throws Exception {
        String errorMessage = "Course must be active to registration into a course";
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("helena.student@example.com");
        newRegistrationDTO.setCourseCode("dotnet");

        doThrow(new BusinessException(errorMessage)).when(registrationService).createRegistration(any(NewRegistrationDTO.class));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void newRegistration__should_return_bad_request_when_user_is_already_registered_into_course() throws Exception {
        String errorMessage = "Student is already registered for this course.";
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("helena.student@example.com");
        newRegistrationDTO.setCourseCode("dotnet");

        doThrow(new BusinessException(errorMessage)).when(registrationService).createRegistration(any(NewRegistrationDTO.class));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
