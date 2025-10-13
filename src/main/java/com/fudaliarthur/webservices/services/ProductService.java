package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.dto.ProductRequestDTO;
import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.repositories.CategoryRepository;
import com.fudaliarthur.webservices.repositories.ProductRepository;
import com.fudaliarthur.webservices.services.exceptions.DatabaseException;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // repassa a chamada para o repository
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Product createProduct(ProductRequestDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImgUrl(productDTO.getImgUrl());

        if (productDTO.getCategories() != null) {
            for (Category category : productDTO.getCategories()) {
                Category existingCategory = categoryRepository.findById(category.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(category.getId()));
                product.getCategories().add(existingCategory);
            }
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            productRepository.delete(product);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
