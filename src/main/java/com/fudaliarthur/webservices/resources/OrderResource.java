package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.dto.OrderRequestDTO;
import com.fudaliarthur.webservices.entities.Order;
import com.fudaliarthur.webservices.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order management", description = "Rotas para gerenciamento de pedidos")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Busca todos os pedidos", description = "Endpoint que retorna uma lista completa de todos os pedidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> Orders = orderService.findAll();
        return ResponseEntity.ok(Orders);
    }

    @Operation(summary = "Busca pedido por ID", description = "Endpoint que retorna um pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order Order = orderService.findById(id);
        return ResponseEntity.ok(Order);
    }

    @Operation(summary = "Cria pedido", description = "Endpoint que cria um pedido com base nas informações do corpo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado"),
            @ApiResponse(responseCode = "404", description = "Item do pedido não encontrado")
    })
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order createdOrder = orderService.createOrderWithItems(orderRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdOrder.getId()).toUri();
        return ResponseEntity.created(uri).body(createdOrder);

    }

    @Operation(summary = "Deleta pedido por ID", description = "Endpoint que deleta um pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Insere um pagamento", description = "Endpoint que insere um pagamento em um pedido com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento inserido com sucesso"),
            @ApiResponse(responseCode = "409", description = "Pedido já possui pagamento")
    })
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<?> addPayment(@PathVariable Long orderId) {

        Order order = orderService.addPayment(orderId);
        return ResponseEntity.ok()
                .header("Payment-Processed", "true")
                .body(order);


    }
}
