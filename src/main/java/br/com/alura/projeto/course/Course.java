package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.exceptions.BusinessException;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10)
    @Pattern(regexp = "^[a-z-]*$")
    @Column(unique = true)
    private String code;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "instructorId")
    private User instructor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Lob
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime inactivatedAt;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Deprecated
    public Course() {}

    public Course(String name, String code, User user, Category category, String description) {
        validate(name, code, user, category);

        this.name = name;
        this.code = code;
        this.instructor = user;
        this.category = category;
        this.description = description;
    }

    private void validate(String name, String code, User user, Category category) {
        requireNonNull(name, "Name cannot be null");
        requireNonNull(code, "Code cannot be null");
        requireNonNull(user, "Instructor cannot be null");
        requireNonNull(category, "Category cannot be null");

        if (!user.isInstructor()) {
            throw new BusinessException("User must be an instructor to create a course");
        }
    }

    public void inactivate() {
        if (status == Status.INACTIVE) {
            throw new DataConflictException("Course already inactive");
        }

        this.status = Status.INACTIVE;
        this.inactivatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public User getInstructor() {
        return instructor;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
