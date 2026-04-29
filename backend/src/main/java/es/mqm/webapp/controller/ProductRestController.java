package es.mqm.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.io.IOException;
import java.util.Optional;

import es.mqm.webapp.dto.ExtendedProductDTO;
import es.mqm.webapp.dto.ImageDTO;
import es.mqm.webapp.dto.ImageMapper;
import es.mqm.webapp.dto.ProductDTO;
import es.mqm.webapp.dto.ProductMapper;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Image;
import es.mqm.webapp.service.ImageService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.model.User;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    @Autowired
	private ProductService productService;
    @Autowired
    private ProductMapper ProductMapper;
    @Autowired
	private ImageService ImageService;
    @Autowired 
    private ImageMapper ImageMapper;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public Page<ExtendedProductDTO> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        User viewer = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> userOpt = userService.findByEmail(authentication.getName());
            viewer = userOpt.orElse(null);
        }
        
        return productService.searchProducts(name, category, location, date, minPrice, maxPrice, viewer, page, size)
                .map(ProductMapper::toExtendedDTO);
    }

    @GetMapping("/{id}")
	public ProductDTO getProduct(@PathVariable int id) {
		Product product = productService.findById(id).orElseThrow();
		return ProductMapper.toDTO(product); 
	}

    @PostMapping("/")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {

		Product product = ProductMapper.toDomain(productDTO);

		if (productDTO.user() == null || productDTO.user().id() == null) {
			throw new IllegalArgumentException("Se necesita un user.id");
		}

		User user = userService.findById(productDTO.user().id().intValue()).orElseThrow();
		product.setUser(user);

		if (productDTO.image() != null) {
			Image image = ImageService.findById(productDTO.image().id()).orElseThrow();
			product.setImage(image);
		}

		product = productService.save(product);
		productDTO = ProductMapper.toDTO(product);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.id()).toUri();

		return ResponseEntity.created(location).body(productDTO);
	}

    @PutMapping("/{id}")
	public ProductDTO replaceProduct(@PathVariable int id, @RequestBody ProductDTO updatedProductDTO) throws SQLException {

		Product updatedProduct = ProductMapper.toDomain(updatedProductDTO);
		updatedProduct = productService.update(id, updatedProduct);
		return ProductMapper.toDTO(updatedProduct);
	}

	@DeleteMapping("/{id}")
	public ProductDTO deleteProduct(@PathVariable int id) {
		return ProductMapper.toDTO(productService.deleteById(id));
	}

    @PostMapping("/{id}/image/")
	public ResponseEntity<ImageDTO> createProductImage(@PathVariable int id, @RequestParam MultipartFile imageFile)
			throws IOException{

		if (imageFile.isEmpty()) {
			throw new IllegalArgumentException("Image file cannot be empty");
		}

		Image image = ImageService.createImage(imageFile);
		productService.addImageToProduct(id, image);

		URI location = fromCurrentContextPath()
				.path("/api/image/{imageId}/media")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(location).body(ImageMapper.toDTO(image));
	}

    @DeleteMapping("/{id}/image/{imageId}")
	public ImageDTO deleteProductImage(@PathVariable int productId, @PathVariable int imageId)
			throws SQLException {

		Image image = ImageService.findById(imageId).orElseThrow();
		productService.removeImageFromProduct(productId);
		ImageService.deleteImage(imageId);
		return ImageMapper.toDTO(image);
	}
}
