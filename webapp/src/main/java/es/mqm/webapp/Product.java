package es.mqm.webapp;

import java.util.ArrayList;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String seller;
    private String imageUrl;
    private  ArrayList<String> categories;
    public Product(int id, String name, String description, double price, String seller, String imageUrl, ArrayList<String> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }
    public int getId() {
        return id;
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
    public String getSeller() {
        return seller;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public ArrayList<String> getCategories() {
        return categories;
    }


}
