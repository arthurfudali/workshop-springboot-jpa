package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.Order;
import com.fudaliarthur.webservices.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> Orders = orderService.findAll();
        return ResponseEntity.ok(Orders);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order Order = orderService.findById(id);
        return ResponseEntity.ok(Order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order createdOrder = orderService.createOrderWithItems(orderRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdOrder.getId()).toUri();
        return ResponseEntity.created(uri).body(createdOrder);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<?> addPayment(@PathVariable Long orderId) {

        Order order = orderService.addPayment(orderId);
        return ResponseEntity.ok()
                .header("Payment-Processed", "true")
                .body(order);


    }
}
