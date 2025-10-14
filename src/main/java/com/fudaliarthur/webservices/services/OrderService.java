package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.dto.OrderItemRequestDTO;
import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.*;
import com.fudaliarthur.webservices.entities.enums.OrderStatus;
import com.fudaliarthur.webservices.repositories.OrderRepository;
import com.fudaliarthur.webservices.repositories.PaymentRepository;
import com.fudaliarthur.webservices.repositories.ProductRepository;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
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

    @Transactional
    public Order addPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(orderId));
        validateNoExistingPayment(order);

        Payment payment = createPaymentForOrder(order);
        associatePaymentWithOrder(order, payment);
        order.setOrderStatus(OrderStatus.PAID);

        return order;
    }

    private void validateNoExistingPayment(Order order) {
        if (order.getPayment() != null) {
            throw new IllegalStateException("Order already has payment: " + order.getId());
        }
    }

    private Payment createPaymentForOrder(Order order) {
        Payment payment = new Payment();
        payment.setMoment(Instant.now());
        payment.setOrder(order);
        return paymentRepository.save(payment);
    }

    private void associatePaymentWithOrder(Order order, Payment payment) {
        order.setPayment(payment);
        orderRepository.save(order);
    }

}
