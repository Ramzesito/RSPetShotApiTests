package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// Если сервер вернёт поле, которого нет в модели, Jackson по умолчанию упадёт с UnrecognizedPropertyException.
// Лечение - аннотация @JsonIgnoreProperties(ignoreUnknown = true) над классом Pet (импорт из com.fasterxml.jackson.annotation).
// У PetShop лишних полей нет, но на других проектах это встречается постоянно.

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {
    private int id;
    private String name;
    private Category category;
    private List<String> photoUrls;
    private List<Tag> tags;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }

    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Вложенный объект "category": {"id": 1, "name": "Dogs"}
    public static class Category {
        private int id;
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Элемент массива "tags": [{"id": 0, "name": "string"}]
    public static class Tag {
        private int id;
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
