package es.mqm.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

import es.mqm.webapp.dto.ImageDTO;
import es.mqm.webapp.dto.ImageMapper;
import es.mqm.webapp.dto.ProductBasicDTO;
import es.mqm.webapp.dto.ProductDTO;
import es.mqm.webapp.dto.ProductMapper;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Image;
import es.mqm.webapp.service.ImageService;
import es.mqm.webapp.service.ProductService;
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

    @GetMapping("/")
    public Collection<ProductBasicDTO> getProducts() {
        return productService.findAll().stream()
				.map(ProductMapper::toBasicDTO)
				.toList();
    }

    @GetMapping("/{id}")
	public ProductDTO getProduct(@PathVariable int id) {
		Product product = productService.findById(id).orElseThrow();
		return ProductMapper.toDTO(product); 
	}

    @PostMapping("/")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {

		Product product = ProductMapper.toDomain(productDTO);
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

    @PostMapping("/{id}/images/")
	public ResponseEntity<ImageDTO> createBookImage(@PathVariable int id, @RequestParam MultipartFile imageFile)
			throws IOException{

		if (imageFile.isEmpty()) {
			throw new IllegalArgumentException("Image file cannot be empty");
		}

		Image image = ImageService.createImage(imageFile);
		productService.addImageToProduct(id, image);

		URI location = fromCurrentContextPath()
				.path("/api/images/{imageId}/media")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(location).body(ImageMapper.toDTO(image));
	}

    @DeleteMapping("/{id}/images/{imageId}")
	public ImageDTO deleteProductImage(@PathVariable int productId, @PathVariable int imageId)
			throws SQLException {

		Optional<Image> imageOptional = ImageService.findById(imageId);
		if (imageOptional.isPresent()) {
			Image image = imageOptional.get();
			productService.removeImageFromProduct(productId);
			ImageService.deleteImage(imageId);
			return ImageMapper.toDTO(image);
		} else {
			throw new SQLException("Image not found");
		}
	}
}
