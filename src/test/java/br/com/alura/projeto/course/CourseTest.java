package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static br.com.alura.projeto.user.Role.INSTRUCTOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CourseTest {
    private Category category;
    private User instructor;

    @BeforeEach
    void setUp() {
        category = new Category("Programação", "programacao", "#6BD1FF", 1);
        instructor = new User("Ana", "ana@email.com", Role.INSTRUCTOR, "123456");
    }

    @Test
    void newCourse__should_be_created() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        assertThat(course.getName()).isEqualTo("Java Básico");
        assertThat(course.getCode()).isEqualTo("java-basico");
        assertThat(course.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(course.getInactivatedAt()).isNull();
        assertThat(course.getInstructor()).isEqualTo(instructor);
    }

    @Test
    void newCourse__should_throw_exception_when_name_is_null() {
        assertThatThrownBy(() -> {
            new Course(null, "java-basico", instructor, category, "Descrição");
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Name cannot be null");
    }

    @Test
    void newCourse__should_throw_exception_when_code_is_null() {
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
    void inactivateCourse__should_inactivate_an_active_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");
        assertThat(course.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(course.getInactivatedAt()).isNull();

        course.inactivate();

        assertThat(course.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(course.getInactivatedAt()).isNotNull();
    }

    @Test
    void inactivateCourse__should_throw_exception_when_trying_to_inactivate_an_already_inactive_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        assertThatThrownBy(() -> {
            course.inactivate();
            course.inactivate();
        }).isInstanceOf(DataConflictException.class).hasMessage("Course already inactive");
    }

    @Test
    void activateCourse__should_activate_an_inactive_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        course.inactivate();
        assertThat(course.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(course.getInactivatedAt()).isNotNull();

        course.activate();
        assertThat(course.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(course.getInactivatedAt()).isNull();
    }

    @Test
    void activateCourse__should_throw_exception_when_trying_to_activate_an_already_active_course() {
        Course course = new Course("Java Básico", "java-basico", instructor, category, "Curso de introdução");

        assertThatThrownBy(() -> {
            course.activate();
            course.activate();
        }).isInstanceOf(DataConflictException.class).hasMessage("Course already active");
    }

    @Test
    void updateInfoCourse__should_update_fields_correctly() {
        String courseCode = "linux";
        Course course = new Course("Linux - Ubuntu", courseCode, instructor, category, "Curso de Ubuntu" );

        String newName = "Linux - Fedora";
        User newInstructor = new User("Caio", "caio@alura.com.br", INSTRUCTOR, "mudar123");
        Category newCategory = new Category("Devops-Infra", "devops-if", "#FF0000", 1);
        String newDescription = "Curso de Fedora";

        course.updateInfo(newName, newInstructor, newCategory, newDescription);

        assertThat(course.getName()).isEqualTo(newName);
        assertThat(course.getCategory()).isEqualTo(newCategory);
        assertThat(course.getInstructor()).isEqualTo(newInstructor);
        assertThat(course.getDescription()).isEqualTo(newDescription);
        assertThat(course.getCode()).isEqualTo(courseCode);
    }
}
