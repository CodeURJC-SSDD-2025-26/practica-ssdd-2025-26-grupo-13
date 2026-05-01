package es.mqm.webapp.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;

import es.mqm.webapp.dto.ImageDTO;
import es.mqm.webapp.dto.ImageMapper;
import es.mqm.webapp.dto.UserDTO;
import es.mqm.webapp.dto.UserMapper;
import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.ImageService;
import es.mqm.webapp.service.UserService;



@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
	private UserMapper UserMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
	private ImageMapper ImageMapper;



    @GetMapping("/")
    public Page<UserDTO> getUsers(Pageable pageable) {
        return userService.findAll(pageable).map(UserMapper::toDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        Optional<User> op = userService.findById(id);
        if (op.isPresent()) {
            User user = op.get();
            return ResponseEntity.ok(UserMapper.toDTO(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        User user = UserMapper.toDomain(userDTO);
        userService.save(user);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(UserMapper.toDTO(user));
    }

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @PutMapping("/{id}")
    public UserDTO replaceUser(@PathVariable long id, @RequestBody UserDTO updatedUserDTO) throws SQLException {

        User updatedUser = UserMapper.toDomain(updatedUserDTO);
        updatedUser = userService.save(updatedUser);
        return UserMapper.toDTO(updatedUser);
    }

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable int id) {

        Optional<User> op = userService.deleteById(id);
        if (op.isPresent()) {
            User user = op.get();
            return ResponseEntity.ok(UserMapper.toDTO(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @PostMapping("/{id}/image/")
    public ResponseEntity<ImageDTO> createUserImage(@PathVariable int id, @RequestParam MultipartFile imageFile)
			throws IOException{

		if (imageFile.isEmpty()) {
			throw new IllegalArgumentException("Image file cannot be empty");
		}

		Image image = imageService.createImage(imageFile);
		userService.addImageToUser(id, image);

		URI location = fromCurrentContextPath()
				.path("/api/image/{imageId}/media")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(location).body(ImageMapper.toDTO(image));
	}

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @DeleteMapping("/{id}/image/{imageId}")
	public ImageDTO deleteUserImage(@PathVariable int id, @PathVariable int imageId)
			throws SQLException {

		Image image = imageService.findById(imageId).orElseThrow();
		userService.removeImageFromUser(id);
		imageService.deleteImage(imageId);
		return ImageMapper.toDTO(image);
	}
}