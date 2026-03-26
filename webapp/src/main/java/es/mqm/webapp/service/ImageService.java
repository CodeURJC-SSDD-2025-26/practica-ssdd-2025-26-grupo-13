package es.mqm.webapp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.*;
import es.mqm.webapp.repository.ImageRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import jakarta.persistence.criteria.Predicate;

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

}
