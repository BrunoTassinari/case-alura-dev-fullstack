package br.com.alura.projeto.course;
import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import br.com.alura.projeto.validation.OnCreate;
import br.com.alura.projeto.validation.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService1) {

        this.courseService = courseService1;
    }

    @GetMapping("/admin/courses")
    public String list(Model model) {
        List<CourseDTO> list = courseService.findAllCourses();

        model.addAttribute("courses", list);

        return "admin/course/list";
    }

    @GetMapping("/admin/course/new")
    public String create(CourseForm form, Model model) {
        model.addAttribute("newCourseForm", form);
        courseService.populateForm(model);
        return "admin/course/form";
    }

    @PostMapping("/admin/course/new")
    public String save(@Validated(OnCreate.class) CourseForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return create(form, model);
        }

        try {
            courseService.createCourse(form);
        } catch (ResourceNotFoundException | DataConflictException ex) {
            result.reject("business.error", ex.getMessage());
            return create(form, model);
        }

        return "redirect:/admin/courses";
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> updateStatus(@PathVariable("code") String courseCode) {
        courseService.inactivateCourse(courseCode);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/course/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Course course = courseService.getById(id);
        model.addAttribute("courseForm", new CourseForm(course));
        courseService.populateForm(model);
        return "admin/course/form";
    }

    @PostMapping("/admin/course/edit/{id}")
    public String update(@PathVariable Long id, @Validated(OnUpdate.class) CourseForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            courseService.populateForm(model);
            model.addAttribute("courseForm", form);
            return "admin/course/form";
        }

        try {
            courseService.updateCourse(form);
        } catch (ResourceNotFoundException | DataConflictException ex) {
            result.reject("business.error", ex.getMessage());
            model.addAttribute("courseForm", form);
            return "admin/course/form";
        }

        return "redirect:/admin/courses";
    }
}
