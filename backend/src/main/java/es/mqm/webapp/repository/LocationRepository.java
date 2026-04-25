package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.mqm.webapp.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String name);
    Optional<Location> findById(int id);
    Optional<Location> deleteById(int id);
    List<Location> findAll();
}
