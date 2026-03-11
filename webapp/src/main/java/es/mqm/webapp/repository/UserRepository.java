package es.mqm.webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.mqm.webapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findBySurnames(String surnames);
    List<User> findByName(String name);
    Optional<User> findById(Integer id);
}