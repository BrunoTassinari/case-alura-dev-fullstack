package br.com.alura.projeto.category;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryWithCoursesDTO> getCourseCategoriesForLoginPage() {
        List<Category> categories = categoryRepository.findCategoriesWithCourses();

        return categories.stream().map(this::mapToCategoryWithCoursesDTO).toList();
    }

    public List<CategoryOptionDTO> getCategoryOptions() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryOptionDTO(c.getId(), c.getName())).toList();
    }

    @Transactional
    public void updateCategory(CategoryForm form) {
        Category category = getById(form.getId());

        category.updateInfo(form.getName(), form.getColor(), form.getOrder());
        categoryRepository.save(category);
    }

    private CategoryWithCoursesDTO mapToCategoryWithCoursesDTO(Category category) {
        String courseNames = category.getCourses().stream()
                .map(Course::getName)
                .collect(Collectors.joining(", "));

        return new CategoryWithCoursesDTO(category.getName(), category.getColor(), courseNames);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category getByCode(String code) {
        return categoryRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
