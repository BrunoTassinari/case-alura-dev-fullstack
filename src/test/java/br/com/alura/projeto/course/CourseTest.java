package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.exceptions.BusinessException;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.util.EncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static br.com.alura.projeto.user.Role.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CourseTest {
    private Category category;
    private User instructor;
    private User student;

    @BeforeEach
    void setUp() {
        category = new Category("Programação", "programacao", "Azul", 1);
        instructor = new User("Ana", "ana@email.com", Role.INSTRUCTOR, "123456");
        student = new User("Bia", "bia@email.com", Role.STUDENT, "123456");
    }

    @Test
    void new_course__should_be_created_with_default_states() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        assertThat(course.getName()).isEqualTo("Java Básico");
        assertThat(course.getCode()).isEqualTo("java-basico");
        assertThat(course.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(course.getInactivatedAt()).isNull();
        assertThat(course.getInstructor()).isEqualTo(instructor);
    }

    @Test
    void new_course__should_throw_exception_when_name_is_null() {
        assertThatThrownBy(() -> {
            new Course(null, "java-basico", instructor, category, "Descrição");
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Name cannot be null");
    }

    @Test
    void new_course__should_throw_exception_when_code_is_null() {
        assertThatThrownBy(() -> {
            new Course("Java Básico", null, instructor, category, "Descrição");
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Code cannot be null");
    }

    @Test
    void newCourse__should_throw_exception_when_instructor_is_null() {
        assertThatThrownBy(() -> {
            new Course("Java Básico", "java-basico", null, category, "Descrição");
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Instructor cannot be null");
    }

    @Test
    void newCourse__should_throw_exception_when_category_is_null() {
        assertThatThrownBy(() -> {
            new Course("Java Básico", "java-basico", instructor, null, "Descrição");
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Category cannot be null");
    }

    @Test
    void new_course__should_not_allow_student_as_instructor() {
        assertThatThrownBy(() -> {
            new Course("Java Básico", "java-basico", student, category, "Curso de introdução");
        }).isInstanceOf(BusinessException.class)
                .hasMessage("User must be an instructor to create a course");
    }

    @Test
    void inactive_course__should_inactivate_an_active_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");
        assertThat(course.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(course.getInactivatedAt()).isNull();

        course.inactivate();

        assertThat(course.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(course.getInactivatedAt()).isNotNull();
    }

    @Test
    void inactive_course__should_throw_exception_when_trying_to_inactivate_an_already_inactive_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        assertThatThrownBy(() -> {
            course.inactivate();
            course.inactivate();
        }).isInstanceOf(DataConflictException.class).hasMessage("Course already inactive");
    }
}
