package es.mqm.webapp.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private String surnames;
    private String email;
    private String imageUrl;
    private float rating;
    private String location;
    private int bought;
    private int sold;
    private List<Product> products = new ArrayList<Product>();
    private ArrayList<String> categories;
    private List<Review> reviews = new ArrayList<Review>();
    public User(int id, String name, String surnames, String email, String imageUrl, float rating, String location, int bought, int sold) {
        this.id = id;
        this.name = name;
        this.surnames = surnames;
        this.email = email;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.location = location;
        this.bought = bought;
        this.sold = sold;
        categories = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            products.add(new Product(i+1, "Producto " + (i + 1), "", 50, "Vendedor" + (i+1), "placeholder100x100.png", categories));
        }
        for(int i=0;i<3;i++){
            reviews.add(new Review(i+1,"iPhone XR", ""+(id + i+1), "1","Muy buen producto, gran calidad precio","Hace 6 meses", (float) 4.0));
        }
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurnames() {
        return surnames;
    }
    public String getEmail() {
        return email;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public float getRating() {
        return rating;
    }
    public String getLocation() {
        return location;
    }
    public int getBought() {
        return bought;
    }
    public int getSold() {
        return sold;
    }
    public List<Product> getProducts() {
        return products;
    }
    public List<Review> getReviews() {
        return reviews;
    }
}
