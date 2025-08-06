package com.ihg.hotelbooking.config;

import com.ihg.hotelbooking.entity.Hotel;
import com.ihg.hotelbooking.entity.User;
import com.ihg.hotelbooking.service.HotelService;
import com.ihg.hotelbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize with sample users if database is empty
        initializeUsers();
        
        // Initialize with sample hotels if database is empty
        if (hotelService.getAllHotels().isEmpty()) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // Create sample hotels
        Hotel hotel1 = new Hotel(
            "Grand Palace Hotel",
            "Paris, France",
            "123 Champs-Élysées, Paris 75008, France",
            "Luxury hotel in the heart of Paris with stunning views of the city. Features elegant rooms, fine dining, and world-class service.",
            5,
            new BigDecimal("350.00"),
            25
        );
        
        Hotel hotel2 = new Hotel(
            "Business Center Inn",
            "New York, USA",
            "456 5th Avenue, New York, NY 10018, USA",
            "Modern business hotel perfect for corporate travelers. Located in Manhattan with easy access to major attractions.",
            4,
            new BigDecimal("250.00"),
            40
        );
        
        Hotel hotel3 = new Hotel(
            "Seaside Resort",
            "Miami, USA",
            "789 Ocean Drive, Miami Beach, FL 33139, USA",
            "Beautiful beachfront resort with stunning ocean views, pools, and spa facilities. Perfect for vacation getaways.",
            4,
            new BigDecimal("200.00"),
            30
        );
        
        Hotel hotel4 = new Hotel(
            "Mountain View Lodge",
            "Aspen, USA",
            "321 Ski Resort Road, Aspen, CO 81611, USA",
            "Cozy mountain lodge with breathtaking views and easy access to ski slopes. Ideal for winter sports enthusiasts.",
            3,
            new BigDecimal("180.00"),
            15
        );
        
        Hotel hotel5 = new Hotel(
            "Budget Comfort Inn",
            "London, UK",
            "654 Oxford Street, London W1C 1AX, UK",
            "Affordable accommodation in central London. Clean, comfortable rooms with essential amenities for budget-conscious travelers.",
            3,
            new BigDecimal("120.00"),
            50
        );
        
        Hotel hotel6 = new Hotel(
            "Royal Heritage Hotel",
            "Jaipur, India",
            "Pink City Palace, Jaipur, Rajasthan 302001, India",
            "Heritage hotel showcasing traditional Rajasthani architecture and culture. Experience royal hospitality in the Pink City.",
            5,
            new BigDecimal("150.00"),
            20
        );
        
        Hotel hotel7 = new Hotel(
            "Tokyo Business Tower",
            "Tokyo, Japan",
            "1-1-1 Shinjuku, Tokyo 160-0022, Japan",
            "Ultra-modern hotel in Shinjuku district with high-tech amenities and panoramic city views. Perfect for business and leisure.",
            4,
            new BigDecimal("300.00"),
            35
        );
        
        Hotel hotel8 = new Hotel(
            "Mediterranean Villa",
            "Santorini, Greece",
            "Oia Village, Santorini 84702, Greece",
            "Romantic villa resort overlooking the Aegean Sea. Features traditional Cycladic architecture and unforgettable sunsets.",
            5,
            new BigDecimal("400.00"),
            12
        );
        
        // Save hotels to database
        hotelService.saveHotel(hotel1);
        hotelService.saveHotel(hotel2);
        hotelService.saveHotel(hotel3);
        hotelService.saveHotel(hotel4);
        hotelService.saveHotel(hotel5);
        hotelService.saveHotel(hotel6);
        hotelService.saveHotel(hotel7);
        hotelService.saveHotel(hotel8);
        
        System.out.println("✅ Sample hotel data initialized successfully!");
    }
    
    private void initializeUsers() {
        // Check if any users exist, if not create test users
        if (userService.findByUsername("admin").isEmpty()) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@hotel.com");
            admin.setPassword("admin123");
            admin.setRoles(new HashSet<>());
            admin.getRoles().add("ADMIN");
            admin.getRoles().add("USER");
            userService.registerUser(admin);
            System.out.println("✅ Admin user created - Username: admin, Password: admin123");
        }
        
        if (userService.findByUsername("user").isEmpty()) {
            // Create regular user
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@hotel.com");
            user.setPassword("user123");
            user.setRoles(new HashSet<>());
            user.getRoles().add("USER");
            userService.registerUser(user);
            System.out.println("✅ Regular user created - Username: user, Password: user123");
        }
        
        if (userService.findByUsername("demo").isEmpty()) {
            // Create demo user
            User demo = new User();
            demo.setUsername("demo");
            demo.setEmail("demo@hotel.com");
            demo.setPassword("demo123");
            demo.setRoles(new HashSet<>());
            demo.getRoles().add("USER");
            userService.registerUser(demo);
            System.out.println("✅ Demo user created - Username: demo, Password: demo123");
        }
    }
}
