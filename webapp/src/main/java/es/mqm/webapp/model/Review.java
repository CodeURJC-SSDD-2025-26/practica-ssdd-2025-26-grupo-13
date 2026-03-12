package es.mqm.webapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String product;
    private String userId;
    private String userDestId;
    private String description;
    private String date;
    private float rating;

    public Review() {
    }

    public Review(String product, String userId, String userDestId, String description, String date,
            Float rating) {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDestId() {
        return userDestId;
    }

    public void setUserDestId(String userDestId) {
        this.userDestId = userDestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
