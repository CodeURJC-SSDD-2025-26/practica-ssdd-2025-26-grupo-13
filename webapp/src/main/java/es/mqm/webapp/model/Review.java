package es.mqm.webapp.model;

public class Review {
    private int id;
    private String product;
    private String userId;
    private String userDestId;
    private String description;
    private String date;
    private float rating;
    public Review(int id,String product, String userId, String userDestId, String description, String date, Float rating) {
        this.id = id;
        this.product = product;
        this.userId = userId;
        this.userDestId = userDestId;
        this.description = description;
        this.date = date;
        this.rating = rating;
    }
    public int getId() {
        return id;
    }
    public String getProduct() {
        return product;
    }
    public String getUserId() {
        return userId;
    }
    public String getUserDestId() {
        return userDestId;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public float getRating() {
        return rating;
    }
}
