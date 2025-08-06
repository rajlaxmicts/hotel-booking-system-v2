// Chatbot functionality
class ChatbotInterface {
    constructor() {
        this.chatContainer = document.getElementById('chat-container');
        this.chatInput = document.getElementById('chat-input');
        this.sendBtn = document.getElementById('send-btn');
        this.isTyping = false;
        
        this.init();
    }
    
    init() {
        this.sendBtn.addEventListener('click', () => this.sendMessage());
        this.chatInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                this.sendMessage();
            }
        });
        
        // Focus on input
        this.chatInput.focus();
    }
    
    async sendMessage() {
        const message = this.chatInput.value.trim();
        if (!message || this.isTyping) return;
        
        this.addMessage(message, 'user');
        this.chatInput.value = '';
        this.setTyping(true);
        
        try {
            const response = await this.sendToAPI(message);
            this.addMessage(response.message, 'bot', response);
        } catch (error) {
            this.addMessage('Sorry, I encountered an error. Please try again later.', 'bot');
        } finally {
            this.setTyping(false);
        }
    }
    
    async sendToAPI(message) {
        const response = await fetch('/api/chatbot/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    }
    
    addMessage(content, sender, data = null) {
        const messageElement = document.createElement('div');
        messageElement.className = `chat-message ${sender}-message fade-in-up`;
        
        const timestamp = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        
        messageElement.innerHTML = `
            <div class="message-content">
                ${sender === 'bot' ? '<i class="fas fa-robot me-2"></i>' : ''}
                ${this.formatMessage(content, data)}
            </div>
            <div class="message-time">${timestamp}</div>
        `;
        
        this.chatContainer.appendChild(messageElement);
        this.scrollToBottom();
    }
    
    formatMessage(content, data) {
        // If there's hotel data, format it nicely
        if (data && data.hotels && data.hotels.length > 0) {
            let formatted = content + '<div class="mt-3">';
            data.hotels.forEach(hotel => {
                formatted += `
                    <div class="card mb-2 border-primary">
                        <div class="card-body p-3">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <h6 class="card-title mb-1">🏨 ${hotel.name}</h6>
                                    <p class="card-text mb-1">
                                        <small class="text-muted">
                                            📍 ${hotel.location}<br>
                                            ⭐ ${hotel.rating}/5 stars | 🛏️ ${hotel.availableRooms} rooms available
                                        </small>
                                    </p>
                                </div>
                                <span class="badge bg-success fs-6">$${hotel.pricePerNight}/night</span>
                            </div>
                            <button class="btn btn-outline-primary btn-sm mt-2" 
                                    onclick="showBookingForm(${hotel.id})">
                                Book Now
                            </button>
                        </div>
                    </div>
                `;
            });
            formatted += '</div>';
            return formatted;
        }
        
        // If there's booking data, format it
        if (data && data.bookings && data.bookings.length > 0) {
            let formatted = content + '<div class="mt-3">';
            data.bookings.forEach(booking => {
                formatted += `
                    <div class="card mb-2 border-info">
                        <div class="card-body p-3">
                            <h6 class="card-title mb-1">🆔 Booking #${booking.id}</h6>
                            <p class="card-text mb-1">
                                <small class="text-muted">
                                    🏨 ${booking.hotel.name}<br>
                                    📅 ${this.formatDate(booking.checkInDate)} - ${this.formatDate(booking.checkOutDate)}<br>
                                    👥 ${booking.numberOfGuests} guests, ${booking.numberOfRooms} room(s)<br>
                                    💰 Total: $${booking.totalPrice}
                                </small>
                            </p>
                            <span class="badge bg-${this.getStatusColor(booking.status)}">${booking.status}</span>
                            ${booking.status === 'CONFIRMED' ? 
                                `<button class="btn btn-outline-danger btn-sm ms-2" 
                                         onclick="cancelBooking(${booking.id})">
                                    Cancel
                                </button>` : ''}
                        </div>
                    </div>
                `;
            });
            formatted += '</div>';
            return formatted;
        }
        
        return content;
    }
    
    formatDate(dateString) {
        return new Date(dateString).toLocaleDateString();
    }
    
    getStatusColor(status) {
        const colors = {
            'CONFIRMED': 'success',
            'CANCELLED': 'danger',
            'COMPLETED': 'primary',
            'PENDING': 'warning'
        };
        return colors[status] || 'secondary';
    }
    
    setTyping(isTyping) {
        this.isTyping = isTyping;
        
        if (isTyping) {
            this.sendBtn.innerHTML = '<div class="loading"></div>';
            this.sendBtn.disabled = true;
            this.chatInput.disabled = true;
            
            // Add typing indicator
            const typingElement = document.createElement('div');
            typingElement.className = 'chat-message bot-message typing-indicator';
            typingElement.innerHTML = `
                <div class="message-content">
                    <i class="fas fa-robot me-2"></i>
                    <span class="typing-dots">
                        <span></span>
                        <span></span>
                        <span></span>
                    </span>
                </div>
            `;
            
            this.chatContainer.appendChild(typingElement);
            this.scrollToBottom();
        } else {
            this.sendBtn.innerHTML = '<i class="fas fa-paper-plane"></i>';
            this.sendBtn.disabled = false;
            this.chatInput.disabled = false;
            this.chatInput.focus();
            
            // Remove typing indicator
            const typingIndicator = this.chatContainer.querySelector('.typing-indicator');
            if (typingIndicator) {
                typingIndicator.remove();
            }
        }
    }
    
    scrollToBottom() {
        this.chatContainer.scrollTop = this.chatContainer.scrollHeight;
    }
}

// Global functions for button clicks
function sendSuggestion(message) {
    const chatbot = window.chatbotInstance;
    if (chatbot) {
        chatbot.chatInput.value = message;
        chatbot.sendMessage();
    }
}

function promptForEmail() {
    const email = prompt('Please enter your email address:');
    if (email) {
        sendSuggestion(`My bookings for ${email}`);
    }
}

function showBookingForm(hotelId) {
    // Simple booking form modal (you can enhance this)
    const name = prompt('Your name:');
    const email = prompt('Your email:');
    const phone = prompt('Your phone number:');
    const checkIn = prompt('Check-in date (YYYY-MM-DD):');
    const checkOut = prompt('Check-out date (YYYY-MM-DD):');
    const guests = prompt('Number of guests:');
    const rooms = prompt('Number of rooms:');
    
    if (name && email && phone && checkIn && checkOut && guests && rooms) {
        createBooking({
            guestName: name,
            email: email,
            phoneNumber: phone,
            checkInDate: checkIn,
            checkOutDate: checkOut,
            numberOfGuests: parseInt(guests),
            numberOfRooms: parseInt(rooms),
            hotelId: hotelId
        }).then(booking => {
            showToast('Booking created successfully!', 'success');
            sendSuggestion(`My bookings for ${email}`);
        }).catch(error => {
            showToast('Error creating booking. Please try again.', 'danger');
        });
    }
}

async function cancelBooking(bookingId) {
    if (confirm('Are you sure you want to cancel this booking?')) {
        try {
            await fetch(`/api/bookings/${bookingId}/cancel`, { method: 'POST' });
            showToast('Booking cancelled successfully!', 'success');
            sendSuggestion('Refresh my bookings');
        } catch (error) {
            showToast('Error cancelling booking. Please try again.', 'danger');
        }
    }
}

// Utility functions
function handleKeyPress(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

function sendMessage() {
    if (window.chatbotInstance) {
        window.chatbotInstance.sendMessage();
    }
}

// Initialize chatbot when page loads
document.addEventListener('DOMContentLoaded', () => {
    window.chatbotInstance = new ChatbotInterface();
    
    // Add typing animation CSS
    const style = document.createElement('style');
    style.textContent = `
        .typing-dots {
            display: inline-block;
        }
        
        .typing-dots span {
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: #007bff;
            margin: 0 2px;
            animation: typing 1.4s infinite ease-in-out;
        }
        
        .typing-dots span:nth-child(1) { animation-delay: -0.32s; }
        .typing-dots span:nth-child(2) { animation-delay: -0.16s; }
        
        @keyframes typing {
            0%, 80%, 100% {
                transform: scale(0);
                opacity: 0.5;
            }
            40% {
                transform: scale(1);
                opacity: 1;
            }
        }
    `;
    document.head.appendChild(style);
});
