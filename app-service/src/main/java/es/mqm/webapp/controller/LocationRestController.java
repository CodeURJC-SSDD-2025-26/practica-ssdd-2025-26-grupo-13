package es.mqm.webapp.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.mqm.webapp.dto.LocationDTO;
import es.mqm.webapp.dto.LocationMapper;
import es.mqm.webapp.model.Location;
import es.mqm.webapp.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {
    @Autowired
    private LocationMapper locationMapper; 
    @Autowired 
    private LocationService locationService;

    @Operation(summary="Get all locations")
    @GetMapping("/")
    public Page<LocationDTO> getLocations(Pageable pageable) {
        return locationService.findAll(pageable).map(locationMapper::toDTO);
    }

    @Operation(summary="Get the location with the given id")
    @GetMapping("/{id}")
    public LocationDTO getLocation(@PathVariable int id) {
        Location loc = locationService.findById(id).orElseThrow();
        return locationMapper.toDTO(loc);
    }

    @Operation(summary="Delete the location with the given id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public LocationDTO deleteLocation(@PathVariable int id) {
        return locationMapper.toDTO(locationService.deleteById(id).orElseThrow());
    }

    @Operation(summary="Create a new location")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locDTO) {
        Location loc = locationMapper.toDomain(locDTO);
        Location saved = locationService.save(loc);
        URI location = fromCurrentRequest().path("{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(locationMapper.toDTO(saved));
    }

    @Operation(summary="Modify the location with the given id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public LocationDTO replaceLocation(@PathVariable int id, @RequestBody LocationDTO updatedLocDTO) throws SQLException {
        Location updatedLoc = locationMapper.toDomain(updatedLocDTO);
        locationService.save(updatedLoc);
        return locationMapper.toDTO(updatedLoc);
    }
}
