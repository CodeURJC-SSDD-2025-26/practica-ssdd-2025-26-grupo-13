package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.mqm.webapp.model.*;

public interface ImageRepository extends JpaRepository<Image,Integer>{
    List<Image> findAll();

    Optional<Image> findById(Integer id);

    Page<Image> findAll(Pageable pageable);
    
}
