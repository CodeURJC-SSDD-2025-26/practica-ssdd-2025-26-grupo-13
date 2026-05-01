package es.mqm.webapp.dto;

import java.util.List;
public record UserBasicDTO(
        Long id,
        String name,
        String surnames,
        String email,
        List<String> roles) {
}
