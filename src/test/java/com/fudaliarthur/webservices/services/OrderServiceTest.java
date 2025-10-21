package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.dto.OrderItemRequestDTO;
import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.Order;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.entities.enums.OrderStatus;
import com.fudaliarthur.webservices.repositories.OrderRepository;
import com.fudaliarthur.webservices.repositories.ProductRepository;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    private User u1, u2;
    private Order o1, o2, o3;
    private Product p1, p2; // ← ADICIONE PRODUTOS
    private Category cat1, cat2;

    @BeforeEach
    void setUp() {
        u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456");


        cat1 = new Category(1L, "Electronics");
        cat2 = new Category(2L, "Books");


        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum", 90.5, "");
        p2 = new Product(2L, "Smart TV", "Nam eleifend", 1250.0, "");


        p1.getCategories().add(cat2);
        p2.getCategories().add(cat1);

        o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, u1);
        o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), OrderStatus.CANCELLED, u2);
        o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, u1);
    }

    @Test
    @DisplayName("Should find all orders")
    void findAll() {
        List<Order> orders = Arrays.asList(o1, o2, o3);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(orders);

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find order by ID when it exists")
    void findById() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.of(o1));

        Order result = orderService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(o1);
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when order not found ")
    void findByIdNotFound() {
        Long id = 99L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findById(id);
        });
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void createOrderWithItems() {
        Long clientId = 1L;
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setMoment(Instant.parse("2019-06-20T19:53:07Z"));
        orderRequest.setOrderStatus(OrderStatus.PAID);
        orderRequest.setClientId(clientId);

        List<OrderItemRequestDTO> items = new ArrayList<>();
        items.add(new OrderItemRequestDTO(1L, 2, 90.5));  // productId, quantity, price
        items.add(new OrderItemRequestDTO(2L, 1, 1250.0));
        orderRequest.setItems(items);

        when(userRepository.findById(clientId)).thenReturn(Optional.of(u1));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(p2));

        // mock do save do pedido
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(4L);
            return order;
        });

        Order result = orderService.createOrderWithItems(orderRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getMoment()).isEqualTo(Instant.parse("2019-06-20T19:53:07Z"));
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(result.getClient()).isEqualTo(u1);

        assertThat(result.getItems()).isNotNull();
        assertThat(result.getTotal()).isEqualTo(2 * 90.5 + 1250.0);

        verify(userRepository, times(1)).findById(clientId);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
        verify(orderRepository, times(1)).save(any(Order.class));


    }

    @Test
    void deleteOrder() {
    }

    @Test
    void addPayment() {
    }
}