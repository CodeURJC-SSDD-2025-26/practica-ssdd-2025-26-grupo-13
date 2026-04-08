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

import es.mqm.webapp.model.Location;
import es.mqm.webapp.model.ExtendedProduct;
import es.mqm.webapp.model.ExtendedProduct;
import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.ProductRepository;
import es.mqm.webapp.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.PageImpl;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    public int countByCategory(String category) {
        return repository.countByCategory(category);
    }

    public Optional<ExtendedProduct> findByIdWithDistance(int id, User viewer) {
        Optional<Product> p = repository.findById(id);
        if (!p.isPresent()) {
            return Optional.empty();
        } 
        else if (viewer == null) {
            return Optional.of(new ExtendedProduct(p.get()));
        }
        else {
            Product product = p.get();
            return Optional.of(new ExtendedProduct(product, viewer));
        }
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


    public Page<ExtendedProduct> getAvailableProducts(int pageNo, int pageSize, User viewer) {
        List<Product> products = repository.findByIsSoldFalse();
        List<String> c = viewer != null ? userRepository.findLastCategoriesBoughtInById(viewer.getId(), PageRequest.of(0,3)).getContent() : List.of();
        List<ExtendedProduct> sorted = products.stream().map(p -> new ExtendedProduct(p,viewer,c.contains(p.getCategory()))).sorted().toList();
        List<ExtendedProduct> pageContent = sorted.subList(pageSize * pageNo, Math.min(pageSize * pageNo + pageSize, sorted.size()));
        return new PageImpl<>(pageContent, PageRequest.of(pageNo,pageSize),sorted.size());
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

    public Page<ExtendedProduct> searchProducts(String name, String category, String location, String date, String minPrice, String maxPrice, User viewer, int pageNo, int pageSize) {
        System.out.println("category: " + category);
        return repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Product, User> u = root.join("user");
            Join<User, Location> l = u.join("location");

            predicates.add(cb.isFalse(root.get("isSold"))); // only shows not ordered products
  //           predicates.add(cb.notEqual(u, viewer)); // don't show your own products, disabled currently for testing purposes
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            } // filters all products that contain the word in its name, case-insensitive
            if (category != null && !category.equals("todas")) {
                predicates.add(cb.equal(root.get("category"), category));
            } // filters all products with specified category
            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(cb.lower(l.get("name")), "%" + location.toLowerCase() + "%"));
            } // filters all products whose location name contains the string specified, case-insensitive
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
            } // filters for products with price between the minimum and maximum specified

            Double longitude = null;
            Double latitude = null;
            if (viewer != null) {
                Location loc = viewer.getLocation();
                longitude = loc.getLongitude();
                latitude = loc.getLatitude();
            }
            if (latitude != null && longitude != null) { // order by distance (lowest first) using haversine formula (same as in GeoUtils.java) and by last updated first
                query.orderBy(cb.asc(
                        cb.prod(6371.0,
                        cb.function("acos", Double.class,
                            cb.sum(
                                cb.prod(
                                    cb.prod(
                                        cb.function("cos", Double.class, cb.function("radians", Double.class, cb.literal(latitude))),
                                        cb.function("cos", Double.class, cb.function("radians", Double.class, l.get("latitude")))
                                    ),
                                    cb.function("cos", Double.class,
                                        cb.diff(
                                            cb.function("radians", Double.class, l.get("longitude")),
                                            cb.function("radians", Double.class, cb.literal(longitude))
                                        )
                                    )
                                ),
                                cb.prod(
                                    cb.function("sin", Double.class, cb.function("radians", Double.class, cb.literal(latitude))),
                                    cb.function("sin", Double.class, cb.function("radians", Double.class, l.get("latitude")))
                                )
                            )
                        )
                    )
                ), cb.desc(root.get("updatedAt"))); 
            } else { // only order by last updated if user not logged in
                query.orderBy(cb.desc(root.get("updatedAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(pageNo, pageSize)).map(p -> new ExtendedProduct(p, viewer));
    }

    public boolean isOwnerOrAdmin(int id, Authentication auth) {
        Product product = repository.findById(id).orElseThrow();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = product.getUser().getEmail().equals(auth.getName());
        return isOwner || isAdmin;
    }

    public List<Product> findByIsSoldFalseAndUser(User user) {
        return repository.findByIsSoldFalseAndUser(user);
    }
    
}