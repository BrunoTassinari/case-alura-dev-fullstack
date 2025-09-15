package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    boolean existsByStudentAndCourse(User student, Course course);

    @Query("""
        SELECT NEW br.com.alura.projeto.registration.RegistrationReportItem(
            c.name,
            c.code,
            i.name,
            i.email,
            COUNT(r.id)
        )
        FROM Registration r
        JOIN r.course c
        JOIN c.instructor i
        GROUP BY c.id, i.id
        ORDER BY COUNT(r.id) DESC
    """)
    List<RegistrationReportItem> findCourseRegistrationReport();
}