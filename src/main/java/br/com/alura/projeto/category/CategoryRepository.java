package br.com.alura.projeto.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCode(String code);

    @Query("""
    SELECT DISTINCT c
    FROM Category c
    LEFT JOIN FETCH c.courses co
    WHERE co.status = 'ACTIVE'
    ORDER BY c.order
    """)
    List<Category> findCategoriesWithCourses();
}
