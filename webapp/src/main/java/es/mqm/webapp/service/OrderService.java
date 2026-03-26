package es.mqm.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Order> findById(int id) {
        return repository.findById(id);
    }

    public List<Order> findByBuyer(User user) {
        return repository.findByBuyer(user);
    }

    public Order save(Order order) {
        return repository.save(order);
    }

    public void delete(Order order) {
        repository.delete(order);
    }
}
