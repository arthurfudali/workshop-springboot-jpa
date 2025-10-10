package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.CategoryRepository;
import com.fudaliarthur.webservices.services.exceptions.DatabaseException;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // repassa a chamada para o repository
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        Optional<Category> obj = categoryRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Category insertCategory(Category obj){
        return categoryRepository.save(obj);
    }

    public void deleteCategoryById(Long id){
        try{
            Category cat = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            categoryRepository.delete(cat);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
    
    private void updateData(Category cat, Category obj) {
        cat.setName(obj.getName());
    }
}
