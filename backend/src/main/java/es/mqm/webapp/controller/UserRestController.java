package es.mqm.webapp.controller;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;


import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;


import es.mqm.webapp.dto.UserDTO;
import es.mqm.webapp.dto.UserMapper;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;



@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
	private UserMapper userMapper;

    @GetMapping("/")
    public Collection<UserDTO> getUsers() {
        return userMapper.toDTOs(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        Optional<User> op = userService.findById(id);
        if (op.isPresent()) {
            User user = op.get();
            return ResponseEntity.ok(userMapper.toDTO(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        User user = userMapper.toDomain(userDTO);
        userService.save(user);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.toDTO(user));
    }

    @PutMapping("/{id}")
    public UserDTO replaceUser(@PathVariable long id, @RequestBody UserDTO updatedUserDTO) throws SQLException {

        User updatedUser = userMapper.toDomain(updatedUserDTO);
        updatedUser = userService.save(updatedUser);
        return userMapper.toDTO(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable int id) {

        Optional<User> op = userService.deleteById(id);
        if (op.isPresent()) {
            User user = op.get();
            return ResponseEntity.ok(userMapper.toDTO(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}