package es.mqm.webapp.model;

public class Review {
    private int id;
    private String product;
    private String user;
    private String userDest;
    private String description;
    private String date;
    private float rating;
    public Review(int id,String product, String user, String userDest, String description, String date, Float rating) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.userDest = userDest;
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
    public String getUser() {
        return user;
    }
    public String getUserDest() {
        return userDest;
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
