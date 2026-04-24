package es.mqm.webapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.*;
import es.mqm.webapp.repository.ImageRepository;
import org.springframework.core.io.Resource;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image createImage(MultipartFile imageFile) throws IOException {
        Image image = new Image();
        try {
            image.setImageFile(new SerialBlob(imageFile.getBytes()));
        } catch (Exception e) {
            throw new IOException("Failed to create image", e);
        }
        imageRepository.save(image);
        return image;
    }


    public Resource getImageFile(int id) throws SQLException {
        Image image = imageRepository.findById(id).orElseThrow();
        if (image.getImageFile() != null) {
            return new InputStreamResource(image.getImageFile().getBinaryStream());
        } else {
            throw new RuntimeException("Image file not found");
        }
    }

    public Optional<Image> findById(int id) {
        return imageRepository.findById(id);
    }

    public Image replaceImageFile(int id, InputStream inputStream) throws IOException {
        Image image = imageRepository.findById(id).orElseThrow();

        try {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new IOException("Failed to create image", e);
        }

        imageRepository.save(image);
        
        return image;
    }

}
