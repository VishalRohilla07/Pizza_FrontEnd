package com.pizza.ordering.config;

import com.pizza.ordering.entity.User;
import com.pizza.ordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialize database with default users
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.pizza.ordering.repository.PizzaRepository pizzaRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create admin user if not exists
            if (!userRepository.existsByEmail("admin@pizza.com")) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@pizza.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);
                userRepository.save(admin);
                logger.info("Admin user created: admin@pizza.com / admin123");
            }

            // Create test customer if not exists
            if (!userRepository.existsByEmail("user@pizza.com")) {
                User customer = new User();
                customer.setName("John Doe");
                customer.setEmail("user@pizza.com");
                customer.setPassword(passwordEncoder.encode("user123"));
                customer.setRole(User.Role.CUSTOMER);
                userRepository.save(customer);
                logger.info("Test customer created: user@pizza.com / user123");
            }

            // Seed Pizzas if none exist
            if (pizzaRepository.count() == 0) {
                seedPizzas();
                logger.info("Pizza menu seeded successfully");
            }

            logger.info("Data initialization completed");
        };
    }

    private void seedPizzas() {
        java.util.List<com.pizza.ordering.entity.Pizza> pizzas = java.util.Arrays.asList(
                createPizza("Margherita",
                        "Classic tomato sauce, fresh mozzarella, basil leaves, and extra virgin olive oil",
                        new java.math.BigDecimal("12.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.VEG,
                        "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&h=500&fit=crop", true),

                createPizza("Pepperoni Feast",
                        "Loaded with premium pepperoni, mozzarella cheese, and our signature tomato sauce",
                        new java.math.BigDecimal("15.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.NON_VEG,
                        "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500&h=500&fit=crop", true),

                createPizza("Garden Veggie", "Bell peppers, mushrooms, onions, olives, tomatoes on a bed of mozzarella",
                        new java.math.BigDecimal("13.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.VEG,
                        "https://images.unsplash.com/photo-1511689660979-10d2b1aada49?w=500&h=500&fit=crop", true),

                createPizza("BBQ Chicken", "Grilled chicken, smoky BBQ sauce, red onions, cilantro, and mozzarella",
                        new java.math.BigDecimal("16.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.NON_VEG,
                        "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&h=500&fit=crop", true),

                createPizza("Four Cheese", "Mozzarella, parmesan, gorgonzola, and ricotta with garlic cream sauce",
                        new java.math.BigDecimal("14.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.VEG,
                        "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&h=500&fit=crop", true),

                createPizza("Meat Lovers", "Pepperoni, Italian sausage, bacon, ham, and ground beef with extra cheese",
                        new java.math.BigDecimal("18.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.NON_VEG,
                        "https://images.unsplash.com/photo-1594007654729-407eedc4be65?w=500&h=500&fit=crop", true),

                createPizza("Mushroom Truffle", "Wild mushrooms, truffle oil, fontina cheese, and fresh thyme",
                        new java.math.BigDecimal("17.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.VEG,
                        "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?w=500&h=500&fit=crop", true),

                createPizza("Spicy Diavola", "Spicy salami, chili flakes, roasted peppers, and mozzarella",
                        new java.math.BigDecimal("15.99"), com.pizza.ordering.entity.Pizza.PizzaCategory.NON_VEG,
                        "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=500&h=500&fit=crop", false));

        pizzaRepository.saveAll(pizzas);
    }

    private com.pizza.ordering.entity.Pizza createPizza(String name, String description, java.math.BigDecimal price,
            com.pizza.ordering.entity.Pizza.PizzaCategory category, String imageUrl, boolean available) {
        com.pizza.ordering.entity.Pizza pizza = new com.pizza.ordering.entity.Pizza();
        pizza.setName(name);
        pizza.setDescription(description);
        pizza.setPrice(price);
        pizza.setCategory(category);
        pizza.setImageUrl(imageUrl);
        pizza.setAvailable(available);
        return pizza;
    }
}
