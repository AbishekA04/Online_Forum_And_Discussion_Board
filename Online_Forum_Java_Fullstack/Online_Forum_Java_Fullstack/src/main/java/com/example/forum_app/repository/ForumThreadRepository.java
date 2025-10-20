   package com.example.forum_app.repository;

   import com.example.forum_app.model.ForumThread;
   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.data.jpa.repository.Query;
   import org.springframework.stereotype.Repository;

   import java.util.List;

   @Repository
   public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
       @Query("SELECT t FROM ForumThread t WHERE t.category.id = ?1")
       List<ForumThread> findByCategoryId(Long categoryId);
   }
   