package es.mqm.webapp.controller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

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

import es.mqm.webapp.dto.OrderDTO;
import es.mqm.webapp.dto.OrderMapper;
import es.mqm.webapp.dto.UserDTO;
import es.mqm.webapp.dto.UserMapper;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
	private OrderMapper orderMapper;

    @Operation(summary="Get a list of all orders")
    @GetMapping("/")
    public Collection<OrderDTO> getOrders() {
        return orderMapper.toDTOs(orderService.findAll());
    }

    @Operation(summary="Get an order by its id")
    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow();
        return orderMapper.toDTO(order);
    }

    @Operation(summary="Create a new order")
    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderMapper.toDomain(orderDTO);
        orderService.save(order);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(orderDTO.id()).toUri();
        return ResponseEntity.created(location).body(orderDTO);
    }

    @Operation(summary="Delete the order with the given id")
    @DeleteMapping("/{id}")
    public OrderDTO deleteOrder(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow();
        orderService.delete(order);
        return orderMapper.toDTO(order);
    }

    @Operation(summary="Modify the order with the given id")
    @PutMapping("/{id}")
    public OrderDTO replaceOrder(@PathVariable int id, @RequestBody OrderDTO updatedOrderDTO) throws SQLException {
        Order updatedOrder = orderMapper.toDomain(updatedOrderDTO);
        orderService.save(updatedOrder);
        return orderMapper.toDTO(updatedOrder);
    }
}