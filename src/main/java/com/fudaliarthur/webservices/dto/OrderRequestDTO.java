package com.fudaliarthur.webservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fudaliarthur.webservices.entities.enums.OrderStatus;

import java.time.Instant;
import java.util.List;

public class OrderRequestDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant moment;
    private OrderStatus orderStatus;
    private Long clientId;
    private List<OrderItemRequestDTO> items;

    public OrderRequestDTO() {
    }


    public OrderRequestDTO(Instant moment, OrderStatus orderStatus, Long clientId, List<OrderItemRequestDTO> items) {
        this.moment = moment;
        this.orderStatus = orderStatus;
        this.clientId = clientId;
        this.items = items;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<OrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }

}
