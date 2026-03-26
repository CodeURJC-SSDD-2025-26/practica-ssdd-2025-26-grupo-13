package es.mqm.webapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    public List<Product> findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public List<Product> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Product> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Page<Product> getAvailableProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findByIsSoldFalse(pageable);
    }

    public Page<Product> findByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findByNameIgnoreCaseContaining(name, pageable);
    }
    public Product addImageToProduct(int id, Image image) {
        Product product = repository.findById(id).orElseThrow();
        product.setImage(image);
        repository.save(product);
        return product;
    }
    public Page<Product> findByCategory(String category, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findByCategory(category, pageable);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }
    public long count() {
        return repository.count();
    }

    public Page<Product> searchProducts(String name, String category, String location, String date, String minPrice, String maxPrice, int pageNo, int pageSize) {
        System.out.println("category: " + category);
        return repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (category != null && !category.equals("todas")) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (location != null && !location.isEmpty()) {
                predicates.add(cb.equal(root.get("location"), location));
            }
            if (date != null && !date.isEmpty()) {
                LocalDateTime minDate = null;
                LocalDateTime now = LocalDateTime.now();

                switch (date) {
                    case "hoy":
                        minDate = LocalDate.now().atStartOfDay();
                        break;
                    case "7":
                        minDate = now.minusDays(7);
                        break;
                    case "30":
                        minDate = now.minusDays(30);
                        break;
                }

                if (minDate != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), minDate));
                }
            }
            if (minPrice != null && !minPrice.isEmpty()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), Integer.parseInt(minPrice)));
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), Integer.parseInt(maxPrice)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(pageNo, pageSize));
    }

    public boolean isOwnerOrAdmin(int id, Authentication auth) {
        Product product = repository.findById(id).orElseThrow();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = product.getUser().getEmail().equals(auth.getName());
        return isOwner || isAdmin;
    }
    
}