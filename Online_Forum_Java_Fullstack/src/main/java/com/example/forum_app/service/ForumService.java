package com.example.forum_app.service;

import com.example.forum_app.model.Category;
import com.example.forum_app.model.ForumThread;
import com.example.forum_app.model.Post;
import com.example.forum_app.repository.CategoryRepository;
import com.example.forum_app.repository.ForumThreadRepository;
import com.example.forum_app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class ForumService {

    @Autowired
    private ForumThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Removed UserRepository since it's not used

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<ForumThread> getAllThreads() {
        return threadRepository.findAll();
    }

    public List<ForumThread> getThreadsByCategory(Long categoryId) {
        return threadRepository.findByCategoryId(categoryId);
    }

    public ForumThread getThreadById(Long id) {
        return threadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Thread not found with ID: " + id));
    }

    public ForumThread createThread(ForumThread thread, String username) {
        thread.setAuthor(username);
        if (thread.getCategory() != null && thread.getCategory().getId() != null) {
            Category category = categoryRepository.findById(thread.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + thread.getCategory().getId()));
            thread.setCategory(category);
        } else {
            throw new RuntimeException("Category is required");
        }
        return threadRepository.save(thread);
    }

    public ForumThread updateThread(Long id, ForumThread updatedThread, String currentUsername) {
        ForumThread existingThread = getThreadById(id);
        if (!existingThread.getAuthor().equals(currentUsername)) {
            throw new RuntimeException("You can only edit your own threads");
        }
        existingThread.setTitle(updatedThread.getTitle());
        existingThread.setContent(updatedThread.getContent());
        if (updatedThread.getCategory() != null && updatedThread.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedThread.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            existingThread.setCategory(category);
        }
        return threadRepository.save(existingThread);
    }

    public void deleteThread(Long id, String currentUsername) {
        ForumThread thread = getThreadById(id);
        if (!thread.getAuthor().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own threads");
        }
        threadRepository.delete(thread);
    }

    public Post createPost(Post post, ForumThread thread, String username) {
        post.setThread(thread);
        post.setAuthor(username);
        return postRepository.save(post);
    }

    public List<Post> getPostsByThread(Long threadId) {
        return postRepository.findByThreadId(threadId);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
    }

    public Post updatePost(Long id, Post updatedPost, String currentUsername) {
        Post existingPost = getPostById(id);
        if (!existingPost.getAuthor().equals(currentUsername)) {
            throw new RuntimeException("You can only edit your own posts");
        }
        existingPost.setContent(updatedPost.getContent());
        return postRepository.save(existingPost);
    }

    public void deletePost(Long id, String currentUsername) {
        Post post = getPostById(id);
        if (!post.getAuthor().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    @PostConstruct
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            Category general = new Category("General");
            categoryRepository.save(general);

            Category tech = new Category("Technology");
            categoryRepository.save(tech);

            System.out.println("Seeded initial categories: General, Technology");
        }
    }
}
