package es.mqm.webapp.dto;
import java.sql.Blob;

public record ImageDTO(
    int id,
    Blob imageFile    
) {}
