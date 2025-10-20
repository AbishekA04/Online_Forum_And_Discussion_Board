  package com.example.forum_app;

  import com.example.forum_app.model.Category;
  import com.example.forum_app.repository.CategoryRepository;
  import org.springframework.boot.CommandLineRunner;
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;

  @SpringBootApplication
  @ComponentScan(basePackages = "com.example.forum_app")  // Scans controllers, entities, repositories
  public class ForumAppApplication {

      @SpringBootApplication
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }
    @Bean
    public CommandLineRunner demo(CategoryRepository categoryRepository) {
        return (args) -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("General"));
                categoryRepository.save(new Category("Technology"));
                categoryRepository.save(new Category("Sports"));
                categoryRepository.save(new Category("Entertainment"));
                System.out.println("Seeded initial categories!");
            }
        };
    }
}
  }