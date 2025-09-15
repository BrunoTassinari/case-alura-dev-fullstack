package br.com.alura.projeto.category.dto;

import br.com.alura.projeto.category.Category;

public record CategoryDTO(Long id, String name, String code, String color, int order) {
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName(), category.getCode(), category.getColor(), category.getOrder());
    }
}
