// es/mqm/webapp/CategoryData.java
package es.mqm.webapp;

import java.util.List;

public class CategoryData {

    public record Category(String name, String slug, String imageUrl, int imgWidth) {
    }

    public static final List<Category> CATEGORIES = List.of(
            new Category("Ropa", "ropa", "images/clothes.jpeg", 150),
            new Category("Automóviles", "automoviles", "images/car.png", 200),
            new Category("Informática", "informatica", "images/computers.png", 200),
            new Category("Libros", "libros", "images/books.png", 150),
            new Category("Electrodomésticos", "electrodomesticos", "images/electrodomesticos.png", 200));

    private CategoryData() {
    } // prevent instantiation
}
