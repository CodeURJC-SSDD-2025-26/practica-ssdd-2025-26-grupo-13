package es.mqm.webapp;

import java.util.ArrayList;

public class Product {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private  ArrayList<String> categories;
    public Product(String name, String description, double price, String imageUrl, ArrayList<String> categories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }
    public String getName() {
        return name;
    }  
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public ArrayList<String> getCategories() {
        return categories;
    }


}
