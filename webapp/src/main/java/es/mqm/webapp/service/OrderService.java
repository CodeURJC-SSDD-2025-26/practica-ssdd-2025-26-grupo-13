package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.OrderRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository repository;

    public List<Order> findAll() {
        return repository.findAll();
    }
    public Page<Order> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public Optional<Order> findById(int id) {
        return repository.findById(id);
    }
    public long count() {
        return repository.count();
    }

    public List<Order> findByBuyer(User user) {
        return repository.findByBuyer(user);
    }
    
    public Page<Order> findByBuyer(User user, Pageable pageable) {
        return repository.findByBuyer(user, pageable);
    }
    public Order save(Order order) {
        return repository.save(order);
    }

    public void delete(Order order) {
        repository.delete(order);
    }
    public int countByBuyer(User buyer) {
        return repository.countByBuyer(buyer);
    }
    public int countBySeller(User seller) {
        return repository.countByProductUser(seller);
    }
    public boolean isBuyerOrAdmin(int id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Optional<Order> orderOpt = repository.findById(id);
        if (!orderOpt.isPresent()) {
            return false;
        }
        Order order = orderOpt.get();
        User user = order.getBuyer();
        boolean isBuyer = user.getEmail().equals(auth.getName());
        return isBuyer || isAdmin;
    }

    public boolean isBuyerOrAdminReview(int id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Optional<Order> orderOpt = repository.findByProductId(id);
        if (!orderOpt.isPresent()) {
            return false;
        }
        Order order = orderOpt.get();
        User user = order.getBuyer();
        boolean isBuyer = user.getEmail().equals(auth.getName());
        return isBuyer || isAdmin;
    }
}
