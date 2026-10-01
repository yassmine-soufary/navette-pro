// Main JavaScript file
const API_BASE_URL = 'http://localhost:8080/api';

// Utility functions
function getAuthToken() {
    return localStorage.getItem('authToken');
}

function setAuthToken(token) {
    localStorage.setItem('authToken', token);
}

function removeAuthToken() {
    localStorage.removeItem('authToken');
}

function getUserInfo() {
    const userInfo = localStorage.getItem('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
}

function setUserInfo(userInfo) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
}

function removeUserInfo() {
    localStorage.removeItem('userInfo');
}

function logout() {
    removeAuthToken();
    removeUserInfo();
    window.location.href = 'index.html';
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
        setTimeout(() => {
            errorDiv.classList.remove('show');
        }, 5000);
    } else {
        alert(message);
    }
}

function showSuccess(message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message show';
    successDiv.textContent = message;
    document.body.appendChild(successDiv);
    setTimeout(() => {
        successDiv.remove();
    }, 3000);
}

async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        }
    };

    const token = getAuthToken();
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        
        // Handle empty responses (204 No Content)
        if (response.status === 204 || response.status === 201) {
            return null;
        }
        
        // Try to parse JSON, but handle cases where response might not be JSON
        let data;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            const text = await response.text();
            if (text) {
                try {
                    data = JSON.parse(text);
                } catch (e) {
                    throw new Error(text || 'Une erreur est survenue');
                }
            }
        }
        
        if (!response.ok) {
            const errorMessage = (data && data.message) ? data.message : 
                                (typeof data === 'string' ? data : 'Une erreur est survenue');
            const error = new Error(errorMessage);
            error.status = response.status;
            throw error;
        }
        
        return data;
    } catch (error) {
        if (error instanceof TypeError && error.message.includes('fetch')) {
            throw new Error('Impossible de se connecter au serveur. Vérifiez que le backend est démarré.');
        }
        throw error;
    }
}
