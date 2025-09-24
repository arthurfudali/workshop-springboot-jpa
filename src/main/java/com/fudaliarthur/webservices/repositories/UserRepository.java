package com.fudaliarthur.webservices.repositories;

import com.fudaliarthur.webservices.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
