package es.mqm.webapp.model;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;
import jakarta.persistence.*;

@Entity
@Table(name = "mqm_user")
@Component
@SessionScope
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String surnames;
    private String email;
    @OneToOne(cascade = CascadeType.ALL) //to eliminate the image when the user is deleted
    private Image image;
    private String password;
    private double rating;
    @ManyToOne
    private Location location;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;



    public User() {
    }

    public User(String name, String surnames, String email, Image image, String password, double rating, Location location,
            String... roles) {
        this.name = name;
        this.surnames = surnames;
        this.email = email;
        this.image = image;
        this.password = password;
        this.rating = rating;
        this.location = location;
          this.roles = List.of(roles);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
