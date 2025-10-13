package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.dto.OrderItemRequestDTO;
import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.Order;
import com.fudaliarthur.webservices.entities.OrderItem;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.OrderRepository;
import com.fudaliarthur.webservices.repositories.ProductRepository;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = orderRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Order createOrderWithItems(OrderRequestDTO orderRequestDTO) {

        User client = userRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(orderRequestDTO.getClientId()));

        Order order = new Order();
        order.setMoment(orderRequestDTO.getMoment());
        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        order.setClient(client);

        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem(order, product, itemDTO.getQuantity(), itemDTO.getPrice());

            order.getItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        orderRepository.delete(order);
    }

}
