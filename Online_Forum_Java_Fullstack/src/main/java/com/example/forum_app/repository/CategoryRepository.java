package com.example.forum_app.repository;

import com.example.forum_app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;  // Added import for @NonNull (fixes null constraint mismatch)
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);  // For uniqueness in seeding

    @NonNull  // Added: Matches inherited findAll() (never null, fixes warning)
    List<Category> findAll();  // For dropdown in controller (redundant but annotated)
}
