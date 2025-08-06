package com.ihg.hotelbooking.controller;

import com.ihg.hotelbooking.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody ChatRequest request) {
        Map<String, Object> response = chatbotService.processUserQuery(request.getMessage());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/help")
    public ResponseEntity<Map<String, Object>> getHelp() {
        Map<String, Object> response = chatbotService.processUserQuery("help");
        return ResponseEntity.ok(response);
    }
    
    // DTO class for chat requests
    public static class ChatRequest {
        private String message;
        
        public ChatRequest() {}
        
        public ChatRequest(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
