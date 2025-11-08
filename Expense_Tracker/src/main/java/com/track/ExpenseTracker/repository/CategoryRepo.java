package com.track.ExpenseTracker.repository;

import com.track.ExpenseTracker.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Page<Category> findByUserId(Integer userId, Pageable pageable);
}
