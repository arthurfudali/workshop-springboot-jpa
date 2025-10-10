package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.DatabaseException;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {

        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            userRepository.delete(user);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }


    }

    public User updateUser(Long id, User obj) {
        try {
            User user = userRepository.getReferenceById(id);
            updateData(user, obj);
            return userRepository.save(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }


    }

    private void updateData(User user, User obj) {
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        user.setPhone(obj.getPhone());
    }
}
