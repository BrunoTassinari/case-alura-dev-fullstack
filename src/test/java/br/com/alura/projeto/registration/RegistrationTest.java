package br.com.alura.projeto.registration;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class RegistrationTest {
    private Course course;
    private User student;

    @BeforeEach
    void setUp() {
        Category category = new Category("Programação", "programacao", "#6BD1FF", 1);
        User instructor = new User("Ana", "ana@email.com", Role.INSTRUCTOR, "123456");

        course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");
        student = new User("Ana", "ana@email.com", Role.STUDENT, "123456");
    }

    @Test
    void newCourse__should_be_created() {
        Registration registration = new Registration(student, course);

        assertThat(registration.getCourse()).isNotNull();
        assertThat(registration.getStudent()).isNotNull();
        assertThat(registration.getRegistrationDate()).isNotNull();
        assertThat(registration.getStudent()).isEqualTo(student);
        assertThat(registration.getCourse()).isEqualTo(course);
    }

    @Test
    void newRegistration__should_throw_exception_when_student_is_null() {
        assertThatThrownBy(() -> {
            new Registration(null, course);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Student cannot be null");
    }

    @Test
    void newRegistration__should_throw_exception_when_course_is_null() {
        assertThatThrownBy(() -> {
            new Registration(student, null);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Course cannot be null");
    }

}
