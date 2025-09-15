package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseService;
import br.com.alura.projeto.exceptions.BusinessException;
import br.com.alura.projeto.registration.dto.NewRegistrationDTO;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final CourseService courseService;

    public RegistrationService(RegistrationRepository registrationRepository, UserService userService, CourseService courseService) {
        this.registrationRepository = registrationRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Transactional
    public void createRegistration(NewRegistrationDTO dto) {
        User user = userService.getByEmail(dto.getStudentEmail());

        if(!user.isStudent()) {
            throw new BusinessException("User must be an student to registration into a course");
        }

        Course course = courseService.getByCode(dto.getCourseCode());

        if(!course.isActive()){
            throw new BusinessException("Course must be active to registration into a course");
        }

        if (registrationRepository.existsByStudentAndCourse(user, course)) {
            throw new BusinessException("Student is already registered for this course.");
        }

        Registration registration = new Registration(user, course);
        registrationRepository.save(registration);
    }

    public List<RegistrationReportItem> generateReport() {
        return registrationRepository.findCourseRegistrationReport();
    }
}
