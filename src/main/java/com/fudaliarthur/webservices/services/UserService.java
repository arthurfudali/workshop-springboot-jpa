package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // repassa a chamada para o repository
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundExeption(id));
    }

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserbyId(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User obj) {
        User user = userRepository.getReferenceById(id);
        updateData(user, obj);
        return userRepository.save(user);

    }

    private void updateData(User user, User obj) {
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        user.setPhone(obj.getPhone());
    }
}
