package br.com.alura.projeto.category;
import br.com.alura.projeto.category.dto.CategoryDTO;
import br.com.alura.projeto.category.dto.CategoryOptionDTO;
import br.com.alura.projeto.category.dto.CategoryWithCoursesDTO;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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

    public List<CategoryDTO> listCategories() {
        return categoryRepository.findAll()
                .stream().sorted(Comparator.comparing(Category::getOrder))
                .map(CategoryDTO::new).toList();
    }

    @Transactional
    public void createCategory(CategoryForm form) {
        if (categoryRepository.existsByCode(form.getCode())) {
            throw new DataConflictException("Code already exists");
        }

        categoryRepository.save(form.toModel());
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
}
