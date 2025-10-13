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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // injecao por construtor
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // repassa a chamada para o repository
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = orderRepository.findById(id);
        return obj.get();
    }

    @Transactional
    public Order createOrderWithItems(OrderRequestDTO orderRequestDTO) {

        // busca o cliente
        User client = userRepository.findById(orderRequestDTO.getClientId()).orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + orderRequestDTO.getClientId()));

        // cria o pedido
        Order order = new Order();
        order.setMoment(orderRequestDTO.getMoment());
        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        order.setClient(client);

        // cria os itens
        for (OrderItemRequestDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem(order, product, itemDTO.getQuantity(), itemDTO.getPrice());

            order.getItems().add(orderItem);
        }

        // salva o pedido
        return orderRepository.save(order);
    }

}
