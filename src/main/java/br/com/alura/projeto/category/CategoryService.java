package br.com.alura.projeto.category;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;


    public CategoryService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryWithCoursesDTO> getCourseCategoriesForLoginPage() {
        List<Category> categories = categoryRepository.findCategoriesWithCourses();

        return categories.stream().map(this::mapToCategoryWithCoursesDTO).toList();
    }

    private CategoryWithCoursesDTO mapToCategoryWithCoursesDTO(Category category) {
        String courseNames = category.getCourses().stream()
                .map(Course::getName)
                .collect(Collectors.joining(", "));

        return new CategoryWithCoursesDTO(category.getName(), category.getColor(), courseNames);
    }
}
