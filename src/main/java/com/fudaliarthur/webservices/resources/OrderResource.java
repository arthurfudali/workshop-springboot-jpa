package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.Order;
import com.fudaliarthur.webservices.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Order> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        System.out.println("Recebendo requisição: " + orderRequestDTO);
        try {
            Order createdOrder = orderService.createOrderWithItems(orderRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
