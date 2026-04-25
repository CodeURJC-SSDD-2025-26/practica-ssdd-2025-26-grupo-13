package es.mqm.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.dto.ImageDTO;
import es.mqm.webapp.dto.ImageMapper;
import es.mqm.webapp.service.ImageService;

import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageMapper imageMapper;
    
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImage(@PathVariable int id) {
        Optional<Image> op = imageService.findById(id);
        if (op.isPresent()) {
            Image image = op.get();
            return ResponseEntity.ok(imageMapper.toDTO(image));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/media")
    public ResponseEntity<Object> getImageFile(@PathVariable int id) throws SQLException, IOException {
        Resource imageFile = imageService.getImageFile(id);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(imageFile)
                .orElse(MediaType.IMAGE_JPEG);

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(imageFile);
    }
    
    @PutMapping("/{id}/media")
    public ResponseEntity<Object> replaceImageFile(@PathVariable int id, @RequestParam MultipartFile imageFile) throws IOException {
        imageService.replaceImageFile(id, imageFile.getInputStream());
        return ResponseEntity.noContent().build();
    }
}
