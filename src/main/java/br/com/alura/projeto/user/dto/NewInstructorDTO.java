package br.com.alura.projeto.user.dto;

import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;

public class NewInstructorDTO extends NewUserDTO{
    @Override
    public User toModel() {
       return new User(this.getName(), this.getEmail(), Role.INSTRUCTOR, this.getPassword());
    }
}
