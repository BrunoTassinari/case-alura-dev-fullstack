package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CourseDTO(Long id,
                        String name,
                        String code,
                        String instructor,
                        Category category,
                        String description,
                        String status,
                        String inactivatedAt) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public CourseDTO(Course course) {
        this(course.getId(),
                course.getName(),
                course.getCode(),
                course.getInstructor().getEmail(),
                course.getCategory(),
                course.getDescription(),
                formatStatus(course.getStatus()),
                formatDate(course.getInactivatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "—";
        }
        return dateTime.format(FORMATTER);
    }

    private static String formatStatus(Status status) {
        return switch (status) {
            case ACTIVE -> "Ativo";
            case INACTIVE -> "Inativo";
        };
    }
}
