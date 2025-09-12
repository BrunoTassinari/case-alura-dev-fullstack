package br.com.alura.projeto.course;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService, CourseService courseService1) {

        this.courseService = courseService1;
    }

    @GetMapping("/admin/courses")
    public String list(@Valid NewCourseForm form) {
        // TODO: Implementar a Questão 1 - Listagem de Cursos aqui...

        return "";
    }

    @GetMapping("/admin/course/new")
    public String create(NewCourseForm form, Model model) {
        model.addAttribute("newCourseForm", form);
        courseService.populateForm(model);
        return "admin/course/newForm";
    }

    @Transactional
    @PostMapping("/admin/course/new")
    public String save(@Valid NewCourseForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            courseService.populateForm(model);
            return "admin/course/newForm";
        }

       courseService.createCourse(form, result);

        // TODO Outra maneira de validacao para as regras de negocio
        if (result.hasErrors()) {
            courseService.populateForm(model);
            return "admin/course/newForm";
        }

        return "redirect:/admin/courses";
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> updateStatus(@PathVariable("code") String courseCode) {
        // TODO: Implementar a Questão 2 - Inativação de Curso aqui...

        return ResponseEntity.ok().build();
    }
}
