package com.example.forum_app.model;  // Your package (with underscore)
 // Import used for @ManyToOne relationship (fixes warning)
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "forum_threads")
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 10000, message = "Content must be between 10 and 10000 characters")
    @Column(nullable = false, length = 10000)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)  // EAGER to load category details immediately (uses the import)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // Foreign key to your Category entity (from dropdown selection) - uses the import

    @Column(nullable = false, length = 50)
    private String author;  // Username as String (e.g., "testuser")

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;  // Optional: For future posts relationship

    // Default constructor (required for JPA/Hibernate)
    public ForumThread() {
        this.createdAt = LocalDateTime.now();
    }

    // Parameterized constructor (for easy creation in services/controllers)
    public ForumThread(String title, String content, Category category, String author) {
        this.title = title;
        this.content = content;
        this.category = category;  // Uses Category (import referenced here)
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ForumThread that = (ForumThread) o;
      return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id);
  }
  
    // Getters and Setters (manual, like your Category entity)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {  // Uses Category (import referenced)
        return category;
    }

    public void setCategory(Category category) {  // Uses Category (import referenced)
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
