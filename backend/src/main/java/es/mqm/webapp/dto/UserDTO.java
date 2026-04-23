package es.mqm.webapp.dto;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(
        Long id,
        String name,
        String surnames,
        String email,
        // String password,
        ImageDTO image,
        LocationDTO location,
        LocalDate createdAt,
        List<String> roles,
        List<ProductBasicDTO> products,
        List<OrderBasicDTO> orders) {
}
