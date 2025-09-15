package br.com.alura.projeto.user;

import br.com.alura.projeto.user.dto.NewInstructorDTO;
import br.com.alura.projeto.user.dto.NewStudentDTO;
import br.com.alura.projeto.user.dto.UserListItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/newStudent")
    public ResponseEntity newStudent(@RequestBody @Valid NewStudentDTO newStudent) {
        userService.createUser(newStudent);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/newInstructor")
    public ResponseEntity newInstructor(@RequestBody @Valid NewInstructorDTO newInstructor) {
        userService.createUser(newInstructor);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public List<UserListItemDTO> listAllUsers() {
        List<UserListItemDTO> list = userService.listUsers();
        return list;
    }

}
