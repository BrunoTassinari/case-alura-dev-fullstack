package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;


@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "studentId", "courseId" }) })
@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "studentId")
    private User student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    private final LocalDateTime registrationDate = LocalDateTime.now();

    @Deprecated
    public Registration() {}

    public Registration(User student, Course course) {
        validate(student, course);

        this.student = student;
        this.course = course;
    }

    private void validate(User student, Course course) {
        requireNonNull(student, "Student cannot be null");
        requireNonNull(course, "Course cannot be null");
    }

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}
