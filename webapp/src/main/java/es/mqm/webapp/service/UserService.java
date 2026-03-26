package es.mqm.webapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public User save(User user) {
        return repository.save(user);
    }
    public User addImageToUser(int id, Image image) {
        User user = repository.findById(id).orElseThrow();
        user.setImage(image);
        repository.save(user);
        return user;
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }
    
    public long count() {
        return repository.count();
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean isOwnerOrAdmin(int id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Optional<User> user = repository.findById(id);
        boolean isOwner = user.isPresent() && user.get().getEmail().equals(auth.getName());
        return isOwner || isAdmin;
    }

}
