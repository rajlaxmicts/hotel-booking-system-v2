<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Hotel Booking System - Copilot Instructions

This is a chatbot-driven hotel booking system built with Java Spring Boot. The system provides AI-powered natural language processing for hotel search and booking operations.

## Project Structure

- **Backend**: Java Spring Boot with JPA/Hibernate for data persistence
- **Database**: H2 in-memory database for development
- **Frontend**: HTML, CSS, JavaScript with Bootstrap for responsive design
- **API**: RESTful APIs for hotel and booking management
- **Chatbot**: Natural language processing service for user queries

## Key Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Thymeleaf template engine
- Bootstrap 5.3.0
- Font Awesome icons

## Architecture Guidelines

- Follow Spring Boot best practices
- Use proper REST API conventions
- Implement proper error handling and validation
- Maintain clean separation of concerns (Controller → Service → Repository)
- Use DTOs for API requests/responses where appropriate

## Code Style

- Use proper Java naming conventions
- Follow Spring Boot annotation patterns
- Implement proper logging
- Use meaningful variable and method names
- Add appropriate comments for complex business logic

## Database Design

- Hotel entity with properties: name, location, description, rating, price, availability
- Booking entity with guest information, dates, and relationship to Hotel
- Use proper JPA annotations and relationships

## API Endpoints

- `/api/hotels` - Hotel management operations
- `/api/bookings` - Booking management operations  
- `/api/chatbot/chat` - Chatbot interaction endpoint

## Frontend Guidelines

- Use Bootstrap components for consistent UI
- Implement responsive design for mobile compatibility
- Use modern JavaScript (ES6+) features
- Maintain clean separation between presentation and logic
- Follow accessibility best practices
