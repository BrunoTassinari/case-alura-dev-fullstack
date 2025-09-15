package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryService;
import br.com.alura.projeto.course.dto.CourseDTO;
import br.com.alura.projeto.exceptions.BusinessException;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public CourseService(CourseRepository courseRepository, UserService userService, CategoryService categoryService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional
    public void createCourse(CourseForm form) {
        if (courseRepository.existsByCode(form.getCode())) {
            throw new DataConflictException("Code already exists");
        }

        User user = userService.getById(form.getInstructorId());

        if(!user.isInstructor()) {
            throw new BusinessException("User must be an instructor to create a course");
        }

        Category category = categoryService.getById(form.getCategoryId());

        Course course = form.toModel(user, category);
        courseRepository.save(course);
    }

    @Transactional
    public void updateCourse(CourseForm form) {
        Course course = this.getById(form.getId());
        Category category = categoryService.getById(form.getCategoryId());
        User user = userService.getById(form.getInstructorId());

        boolean isCurrentlyActive = course.getStatus() == Status.ACTIVE;

        if (form.isActive() && !isCurrentlyActive) {
            course.activate();
        } else if (!form.isActive() && isCurrentlyActive) {
            course.inactivate();
        }

        course.updateInfo(form.getName(), user, category, form.getDescription());
        courseRepository.save(course);
    }

    public List<CourseDTO> findAllCourses(){
       return courseRepository.findAll()
               .stream()
               .map(CourseDTO::new).toList();
    }

    public void populateForm(Model model) {
        model.addAttribute("categories", categoryService.getCategoryOptions());
        model.addAttribute("instructors", userService.getInstructorsOptions());
    }

    @Transactional
    public void inactivateCourse(String code) {
        Course courseToInactivate = getByCode(code);

        courseToInactivate.inactivate();
        courseRepository.save(courseToInactivate);
    }

    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public Course getByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }
}
