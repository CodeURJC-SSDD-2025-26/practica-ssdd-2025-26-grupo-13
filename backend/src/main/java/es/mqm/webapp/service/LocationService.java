package es.mqm.webapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.mqm.webapp.model.Location;
import es.mqm.webapp.repository.LocationRepository;

@Service
public class LocationService {
    @Autowired
    private LocationRepository repository;

    public Location save(Location location) {
        return repository.save(location);
    }

    public Optional<Location> findByCity(String name) {
        return repository.findByName(name);
    }
}
