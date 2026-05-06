package es.mqm.webapp.controller;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;


import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;


import es.mqm.webapp.dto.OrderDTO;
import es.mqm.webapp.dto.OrderMapper;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.MailService;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.TicketService;
import es.mqm.webapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
	private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TicketService ticketService;

    @Operation(summary="Get a list of all orders")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public Collection<OrderDTO> getOrders() {
        return orderMapper.toDTOs(orderService.findAll());
    }

    @Operation(summary="Get an order by its id")
    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow();
        return orderMapper.toDTO(order);
    }

    @Operation(summary="Create a new order")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        Order order = orderMapper.toDomain(orderDTO);

        if (orderDTO.product() == null || orderDTO.product().id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product.id must be provided");
        }

        int productId = orderDTO.product().id();
        if (orderService.findByProductId(productId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already has an order");
        }

        User buyer = userService.findByEmail(authentication.getName()).orElseThrow();
        Product product = productService.findById(productId).orElseThrow();

        order.setBuyer(buyer);
        order.setProduct(product);
        order.setTotalPrice(calculateTotalPrice(product));

        validateOrderInput(orderDTO);

        Order savedOrder = orderService.save(order);
        mailService.sendOrderConfirmation(order);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedOrder.getId()).toUri();
        return ResponseEntity.created(location).body(orderMapper.toDTO(savedOrder));
    }

    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @Operation(summary="Delete the order with the given id")
    @DeleteMapping("/{id}")
    public OrderDTO deleteOrder(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow();
        orderService.delete(order);
        return orderMapper.toDTO(order);
    }

    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @Operation(summary="Modify the order with the given id")
    @PutMapping("/{id}")
    public OrderDTO replaceOrder(@PathVariable int id, @RequestBody OrderDTO updatedOrderDTO) throws SQLException {
        Order updatedOrder = orderMapper.toDomain(updatedOrderDTO);

        Order existingOrder = orderService.findById(id).orElseThrow();
        updatedOrder.setId(id);
        updatedOrder.setCreatedAt(existingOrder.getCreatedAt());
        updatedOrder.setUpdatedAt(existingOrder.getUpdatedAt());

        if (updatedOrderDTO.buyer() != null && updatedOrderDTO.buyer().id() != null) {
            User buyer = userService.findById(updatedOrderDTO.buyer().id().intValue()).orElseThrow();
            updatedOrder.setBuyer(buyer);
        } else {
            updatedOrder.setBuyer(existingOrder.getBuyer());
        }

        if (updatedOrderDTO.product() != null) {
            Product product = productService.findById(updatedOrderDTO.product().id()).orElseThrow();
            updatedOrder.setProduct(product);
        } else {
            updatedOrder.setProduct(existingOrder.getProduct());
        }

        updatedOrder.setTotalPrice(calculateTotalPrice(updatedOrder.getProduct()));

        Order savedOrder = orderService.save(updatedOrder);
        return orderMapper.toDTO(savedOrder);
    }

    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @Operation(summary="Get the invoice PDF of the order with the given id")
    @GetMapping("/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        byte[] ticketBytes = ticketService.generateTicket(order);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedido_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(ticketBytes);
    }

    private boolean isInvalid(String value) {
        return value == null || value.isBlank();
    }

    private double calculateTotalPrice(Product product) {
        double price = product.getPrice();
        return price < 30 ? price + 3.5 : price;
    }

    private void validateOrderInput(OrderDTO orderDTO) {
        if (isInvalid(orderDTO.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (isInvalid(orderDTO.surnames())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "surnames is required");
        }
        if (isInvalid(orderDTO.country())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country is required");
        }
        if (isInvalid(orderDTO.address())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "address is required");
        }
        if (isInvalid(orderDTO.province())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "province is required");
        }
        if (isInvalid(orderDTO.city())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "city is required");
        }
        if (isInvalid(orderDTO.zipcode()) || !orderDTO.zipcode().matches("\\d{5}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "zipcode must be 5 digits");
        }
        if (isInvalid(orderDTO.phone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phone is required");
        }
        if (isInvalid(orderDTO.creditCardNumber()) || !orderDTO.creditCardNumber().matches("\\d{16}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creditCardNumber must be 16 digits");
        }
        if (isInvalid(orderDTO.creditCardExpiryDate())
                || !orderDTO.creditCardExpiryDate().matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creditCardExpiryDate must be MM/YY");
        }
        if (isInvalid(orderDTO.creditCardCVV()) || !orderDTO.creditCardCVV().matches("\\d{3}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creditCardCVV must be 3 digits");
        }
    }
}