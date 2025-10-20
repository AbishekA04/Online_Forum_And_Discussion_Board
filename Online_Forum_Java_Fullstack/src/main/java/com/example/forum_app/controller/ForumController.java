package com.example.forum_app.controller;

import com.example.forum_app.model.ForumThread;
import com.example.forum_app.model.Post;
import com.example.forum_app.model.Category;
import com.example.forum_app.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/thread")
public class ForumController {

    @Autowired
    private ForumService forumService;

    // 🏠 Home page (matches index.html)
    @GetMapping("/")
    public String home(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("message", "Welcome to the Forum!");
            model.addAttribute("threads", forumService.getAllThreads());
            if (redirectAttributes.getFlashAttributes().containsKey("successMessage")) {
                model.addAttribute("successMessage", redirectAttributes.getFlashAttributes().get("successMessage"));
            }
            return "index";
        }
        return "redirect:/login";
    }

    // 🆕 Show form to create a new thread (matches new-thread.html)
    @GetMapping("/new")
    public String newThreadForm(Model model, Authentication auth) {
        model.addAttribute("thread", new ForumThread());
        model.addAttribute("categories", forumService.getAllCategories());
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("currentUser", auth.getName());
        }
        return "new-thread";
    }

    // 💾 Handle new thread creation
    @PostMapping("/new")
    public String createThread(@Valid @ModelAttribute("thread") ForumThread thread,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long categoryId,
                               Authentication auth,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", forumService.getAllCategories());
            return "new-thread";
        }

        String username = (auth != null) ? auth.getName() : "anonymous";

        if (categoryId != null) {
            Category c = new Category();
            c.setId(categoryId);
            thread.setCategory(c);
        }

        thread.setAuthor(username);
        thread.setCreatedAt(LocalDateTime.now());

        ForumThread saved = forumService.createThread(thread, username);
        redirectAttributes.addFlashAttribute("successMessage", "Thread created successfully!");
        return "redirect:/thread/" + saved.getId();
    }

    // 👀 View a specific thread (matches thread-view.html)
    @GetMapping("/{id}")
    public String viewThread(@PathVariable Long id, Model model, Authentication auth) {
        ForumThread thread = forumService.getThreadById(id);
        model.addAttribute("thread", thread);
        model.addAttribute("posts", forumService.getPostsByThread(id));

        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("currentUser", auth.getName());
        }

        return "thread-view";
    }

    // 💬 Add reply to a thread (changed to /reply to match thread-view.html)
    @PostMapping("/{id}/reply")
    public String createPost(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        ForumThread thread = forumService.getThreadById(id);

        String username = (auth != null) ? auth.getName() : "anonymous";
        Post post = new Post();
        post.setContent(content);
        post.setAuthor(username);
        post.setCreatedAt(LocalDateTime.now());

        forumService.createPost(post, thread, username);
        redirectAttributes.addFlashAttribute("successMessage", "Reply added successfully!");
        return "redirect:/thread/" + id;
    }

    // ✏️ Edit thread (basic, matches thread-view.html link)
    @GetMapping("/{id}/edit")
    public String editThreadForm(@PathVariable Long id, Model model, Authentication auth) {
        ForumThread thread = forumService.getThreadById(id);
        if (!thread.getAuthor().equals(auth.getName())) {
            return "redirect:/thread/" + id; // Not authorized
        }
        model.addAttribute("thread", thread);
        model.addAttribute("categories", forumService.getAllCategories());
        return "edit-thread"; // You'll need to create this template
    }

    // 🗑️ Delete thread
    @PostMapping("/{id}/delete")
    public String deleteThread(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        ForumThread thread = forumService.getThreadById(id);
        if (!thread.getAuthor().equals(auth.getName())) {
            return "redirect:/thread/" + id; // Not authorized
        }
        forumService.deleteThread(id, auth.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Thread deleted!");
        return "redirect:/thread/";
    }

    // ✏️ Edit post (basic)
    @GetMapping("/post/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model, Authentication auth) {
        Post post = forumService.getPostById(id); // Add this method to ForumService
        if (!post.getAuthor().equals(auth.getName())) {
            return "redirect:/thread/" + post.getThread().getId();
        }
        model.addAttribute("post", post);
        return "edit-post"; // You'll need to create this template
    }

    // 🗑️ Delete post
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        Post post = forumService.getPostById(id); // Add this method
        if (!post.getAuthor().equals(auth.getName())) {
            return "redirect:/thread/" + post.getThread().getId();
        }
        forumService.deletePost(id, auth.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Reply deleted!");
        return "redirect:/thread/" + post.getThread().getId();
    }
}