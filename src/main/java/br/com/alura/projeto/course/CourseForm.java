package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class CourseForm {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(groups = OnCreate.class, message = "O código é obrigatório")
    @Length(groups = OnCreate.class, min = 4, max = 10, message = "O código deve ter entre 4 e 10 caracteres")
    @Pattern(groups = OnCreate.class, regexp = "^[a-z-]*$", message = "O código deve conter apenas letras minúsculas e hífens")
    private String code;

    @NotNull(message = "O instrutor é obrigatório")
    private Long instructorId;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoryId;

    private String description;

    private boolean active = true;

    @Deprecated
    public CourseForm() {}

    public CourseForm(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.code = course.getCode();
        this.instructorId = course.getInstructor().getId();
        this.categoryId = course.getCategory().getId();
        this.description = course.getDescription();
        this.active = course.getStatus() == Status.ACTIVE;
    }

    public Course toModel(User user, Category category) {
        return new Course(name, code, user, category, description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
