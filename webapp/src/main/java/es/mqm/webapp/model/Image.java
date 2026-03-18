package es.mqm.webapp.model;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Lob
    private Blob imageFile;

    public int getId() {
        return id;
    }
    public Blob getImageFile() {
        return imageFile;
    }
    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

}
