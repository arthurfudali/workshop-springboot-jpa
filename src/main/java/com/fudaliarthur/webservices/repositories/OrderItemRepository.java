package com.fudaliarthur.webservices.repositories;

import com.fudaliarthur.webservices.entities.OrderItem;
import com.fudaliarthur.webservices.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
