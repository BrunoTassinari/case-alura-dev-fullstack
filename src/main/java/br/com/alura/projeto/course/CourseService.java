package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryOptionDTO;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.InstructorOptionDTO;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createCourse(NewCourseForm form, BindingResult result) {
        if (courseRepository.existsByCode(form.getCode())) {
            result.rejectValue("code", "code.exists", "Código já utilizado");
            return;
        }

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida"));

        User user = userRepository.findById(form.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instrutor inválido"));

        Course course = form.toModel(user, category);
        courseRepository.save(course);
    }

    public void populateForm(Model model) {
        List<CategoryOptionDTO> categories = categoryRepository.findAll().stream()
                .map(c -> new CategoryOptionDTO(c.getId(), c.getName())).toList();

        List<InstructorOptionDTO> instructors = userRepository.findAllInstructors().stream()
                .map(u -> new InstructorOptionDTO(u.getId(), u.getName() + " - " + u.getEmail())).toList();

        model.addAttribute("categories", categories);
        model.addAttribute("instructors", instructors);
    }
}
