package com.fudaliarthur.webservices.repositories;

import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
