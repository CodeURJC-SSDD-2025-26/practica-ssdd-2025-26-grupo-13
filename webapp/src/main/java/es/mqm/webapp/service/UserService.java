package es.mqm.webapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
	private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

}
