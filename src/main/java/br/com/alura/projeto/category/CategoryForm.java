package br.com.alura.projeto.category;

import br.com.alura.projeto.validation.OnCreate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CategoryForm {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank(groups = OnCreate.class, message = "O código é obrigatório")
    @Length(groups = OnCreate.class, min = 4, max = 10)
    private String code;

    @Min(1)
    private int order;

    @NotBlank
    private String color;

    @Deprecated
    public CategoryForm() {}

    public CategoryForm(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.code = category.getCode();
        this.order = category.getOrder();
        this.color = category.getColor();
    }

    public Category toModel() {
        return new Category(name, code, color, order);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}