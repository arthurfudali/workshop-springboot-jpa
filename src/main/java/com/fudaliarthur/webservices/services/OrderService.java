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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    // repassa a chamada para o repository
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = orderRepository.findById(id);
        return obj.get();
    }

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        // busca o cliente no banco
        User client = userRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(orderRequestDTO.getClientId()));

        // criar order
        Order order = new Order();
        order.setMoment(orderRequestDTO.getMoment());
        order.setClient(client);
        order.setOrderStatus(orderRequestDTO.getOrderStatus());

        // listar e acossiar os OrderItems
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO itemRequestDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemRequestDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(itemRequestDTO.getProductId()));

            // criar o OrderItem usando o construtor
            OrderItem orderItem = new OrderItem(
                    order,           // associa o pedido
                    product,         // associa o produto
                    itemRequestDTO.getQuantity(),  // quantidade
                    itemRequestDTO.getPrice()      // preço
            );

            orderItems.add(orderItem);

        }

        // associar os itens ao pedido
        // Como seu Order tem um Set<OrderItem>, precisamos adicionar todos
        order.getItems().addAll(orderItems);

        // salvar o pedido (funciona por causa do cascade)

        return orderRepository.save(order);


    }

}
