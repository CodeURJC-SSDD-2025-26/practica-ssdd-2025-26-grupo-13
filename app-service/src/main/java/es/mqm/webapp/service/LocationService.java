package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Optional<Location> findById(int id) {
        return repository.findById(id);
    } 
    public List<Location> findAll() {
        return repository.findAll();
    }

    public Page<Location> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public Optional<Location> deleteById(int id) {
        return repository.deleteById(id);
    }
}
