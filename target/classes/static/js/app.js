// Global API configuration
const API_BASE_URL = '/api';

// Authentication functions
function getAuthToken() {
    return localStorage.getItem('authToken');
}

function getUsername() {
    return localStorage.getItem('username');
}

function getEmail() {
    return localStorage.getItem('email');
}

function isLoggedIn() {
    return !!getAuthToken();
}

function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    updateAuthUI();
    showToast('Logged out successfully', 'success');
    // Redirect to home page
    window.location.href = '/';
}

function updateAuthUI() {
    const loginNav = document.getElementById('login-nav');
    const registerNav = document.getElementById('register-nav');
    const userNav = document.getElementById('user-nav');
    const usernameDisplay = document.getElementById('username-display');
    const authActions = document.getElementById('auth-actions');
    
    console.log('Updating auth UI, logged in:', isLoggedIn()); // Debug log
    
    if (isLoggedIn()) {
        // User is logged in - hide login/register, show user menu
        if (loginNav) loginNav.style.display = 'none';
        if (registerNav) registerNav.style.display = 'none';
        if (userNav) userNav.style.display = 'block';
        if (usernameDisplay) usernameDisplay.textContent = getUsername();
        if (authActions) authActions.style.display = 'none';
        console.log('User is logged in, hiding auth buttons');
    } else {
        // User is not logged in - show login/register, hide user menu
        if (loginNav) loginNav.style.display = 'block';
        if (registerNav) registerNav.style.display = 'block';
        if (userNav) userNav.style.display = 'none';
        if (authActions) authActions.style.display = 'flex'; // Use flex to maintain button layout
        console.log('User is not logged in, showing auth buttons');
    }
}

// Utility functions
function formatDate(date) {
    return new Date(date).toLocaleDateString();
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function showToast(message, type = 'info') {
    // Create toast element
    const toast = document.createElement('div');
    toast.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    toast.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    toast.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(toast);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 5000);
}

// API functions
async function fetchAPI(endpoint, options = {}) {
    try {
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };
        
        // Add authorization header if token exists
        const token = getAuthToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        const response = await fetch(API_BASE_URL + endpoint, {
            headers,
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        showToast('Error connecting to server', 'danger');
        throw error;
    }
}

async function getHotels(filters = {}) {
    const params = new URLSearchParams();
    Object.keys(filters).forEach(key => {
        if (filters[key] !== null && filters[key] !== undefined && filters[key] !== '') {
            params.append(key, filters[key]);
        }
    });
    
    const queryString = params.toString();
    const endpoint = queryString ? `/hotels/search?${queryString}` : '/hotels';
    
    return await fetchAPI(endpoint);
}

async function getBookings() {
    return await fetchAPI('/bookings');
}

async function createBooking(bookingData) {
    return await fetchAPI('/bookings', {
        method: 'POST',
        body: JSON.stringify(bookingData)
    });
}

async function sendChatMessage(message) {
    return await fetchAPI('/chatbot/chat', {
        method: 'POST',
        body: JSON.stringify({ message })
    });
}

// Load statistics on homepage
async function loadStats() {
    try {
        const [hotels, bookings] = await Promise.all([
            getHotels(),
            getBookings()
        ]);
        
        // Animate counters
        animateCounter('hotels-count', hotels.length);
        animateCounter('bookings-count', bookings.length);
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

function animateCounter(elementId, target) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    let current = 0;
    const increment = target / 50;
    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            current = target;
            clearInterval(timer);
        }
        element.textContent = Math.floor(current);
    }, 20);
}

// Mini chat functionality for homepage
function initMiniChat() {
    const chatContainer = document.getElementById('mini-chat');
    const chatInput = document.getElementById('mini-chat-input');
    const sendBtn = document.getElementById('mini-send-btn');
    
    if (!chatContainer || !chatInput || !sendBtn) return;
    
    // Add welcome message
    addMiniChatMessage('Hi! Ask me about hotels, like "Find hotels in Paris"', 'bot');
    
    sendBtn.addEventListener('click', sendMiniChatMessage);
    chatInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMiniChatMessage();
        }
    });
    
    async function sendMiniChatMessage() {
        const message = chatInput.value.trim();
        if (!message) return;
        
        addMiniChatMessage(message, 'user');
        chatInput.value = '';
        
        try {
            const response = await sendChatMessage(message);
            addMiniChatMessage(response.message, 'bot');
        } catch (error) {
            addMiniChatMessage('Sorry, I encountered an error. Please try again.', 'bot');
        }
    }
    
    function addMiniChatMessage(message, sender) {
        const messageElement = document.createElement('div');
        messageElement.className = `chat-message ${sender}-message mb-2`;
        
        const truncatedMessage = message.length > 100 ? message.substring(0, 100) + '...' : message;
        
        messageElement.innerHTML = `
            <div class="message-content">
                ${sender === 'bot' ? '<i class="fas fa-robot me-2"></i>' : ''}
                ${truncatedMessage}
            </div>
        `;
        
        chatContainer.appendChild(messageElement);
        chatContainer.scrollTop = chatContainer.scrollHeight;
        
        // Limit messages to prevent overflow
        if (chatContainer.children.length > 6) {
            chatContainer.removeChild(chatContainer.firstChild);
        }
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    // Load stats if on homepage
    if (document.getElementById('hotels-count')) {
        loadStats();
    }
    
    // Initialize mini chat if present
    if (document.getElementById('mini-chat')) {
        initMiniChat();
    }
    
    // Add smooth scrolling to all anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Add fade-in animation to cards
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in-up');
            }
        });
    }, observerOptions);
    
    document.querySelectorAll('.card, .feature-card').forEach(card => {
        observer.observe(card);
    });
    
    // Update authentication UI
    updateAuthUI();
    
    // Add logout functionality
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    }
});
