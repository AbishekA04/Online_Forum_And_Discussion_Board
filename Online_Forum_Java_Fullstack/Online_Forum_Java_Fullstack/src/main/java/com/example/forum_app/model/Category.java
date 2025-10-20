package com.example.forum_app.model;  // Your package (with underscore)

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "categories")  // Optional: Custom table name (default is "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    private String name;  // e.g., "General", "Technology"

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ForumThread> threads;

    // Default constructor (required for JPA/Hibernate)
    public Category() {}

    // Parameterized constructor (for easy creation in services/controllers)
    public Category(String name) {
        this.name = name;
    }

    // Getters and Setters (enables access in services and fixes "not used" warnings)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ForumThread> getThreads() {
        return threads;
    }

    public void setThreads(List<ForumThread> threads) {
        this.threads = threads;
    }
}
