package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class NewCourseForm {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O código é obrigatório")
    @Length(min = 4, max = 10, message = "O código deve ter entre 4 e 10 caracteres")
    @Pattern(regexp = "^[a-z-]*$", message = "O código deve conter apenas letras minúsculas e hífens")
    private String code;

    @NotNull(message = "O instrutor é obrigatório")
    private Long instructorId;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoryId;

    private String description;

    public Course toModel(User user, Category category) {
        return new Course(name, code, user, category, description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
