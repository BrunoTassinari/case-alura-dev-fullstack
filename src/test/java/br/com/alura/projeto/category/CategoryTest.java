package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {
    @Test
    void updateInfoCategory__should_update_fields_correctly() {
        String code = "code";
        Category category = new Category("Devops", code, "#FF0000", 1);

        String newName = "Ops";
        String newColor = "#0000FF";
        int newOrder = 5;

        category.updateInfo(newName, newColor, newOrder);

        assertEquals(newName, category.getName());
        assertEquals(newColor, category.getColor());
        assertEquals(newOrder, category.getOrder());
        assertEquals(code, category.getCode());
    }
}
