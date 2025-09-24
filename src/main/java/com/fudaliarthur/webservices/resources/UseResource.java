package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UseResource {
    @GetMapping
    public ResponseEntity<User> findAll(){
        User user = new User(1L, "maria", "maria@gmail", "9999999", "1234");
        return ResponseEntity.ok(user);
    }
}
