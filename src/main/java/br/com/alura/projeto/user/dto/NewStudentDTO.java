package br.com.alura.projeto.user.dto;
import br.com.alura.projeto.user.User;

import static br.com.alura.projeto.user.Role.STUDENT;

public class NewStudentDTO extends NewUserDTO {
    public User toModel() {
        return new User(this.getName(), this.getEmail(), STUDENT, this.getEmail());
    }
}
