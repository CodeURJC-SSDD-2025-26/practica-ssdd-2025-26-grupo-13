package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    public List<Product> findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public List<Product> findByCategory(String category) {
        return repository.findByCategoriesContaining(category);
    }

    public List<Product> findBySeller(String seller) {
        return repository.findBySeller(seller);
    }

    public Page<Product> getProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findAll(pageable);
    }

    public Page<Product> findByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findByNameIgnoreCase(name, pageable);
    }

    public Page<Product> findByCategory(String category, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return repository.findByCategoriesContaining(category, pageable);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

}
