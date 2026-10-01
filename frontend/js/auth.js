let selectedRole = 'STUDENT';
let routes = [];
let pickupPoints = [];

// Initialize
document.addEventListener('DOMContentLoaded', async () => {
    loadRoutes();
    
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('regRoute').addEventListener('change', handleRouteChange);
    
    // Hide register section for students (show by default when clicking link)
    const registerSection = document.getElementById('registerSection');
    registerSection.style.display = 'none';
});

function toggleRegisterForm() {
    const loginForm = document.getElementById('loginForm');
    const registerSection = document.getElementById('registerSection');
    
    if (registerSection.style.display === 'none') {
        loginForm.style.display = 'none';
        registerSection.style.display = 'block';
    } else {
        loginForm.style.display = 'block';
        registerSection.style.display = 'none';
    }
}

function selectRole(role) {
    selectedRole = role;
    document.querySelectorAll('.role-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-role="${role}"]`).classList.add('active');
    
    const loginForm = document.getElementById('loginForm');
    const registerSection = document.getElementById('registerSection');
    
    if (role === 'STUDENT') {
        // For students, show login form by default (can toggle to register)
        loginForm.style.display = 'block';
        registerSection.style.display = 'none';
    } else {
        // For drivers and admins, only show login form
        loginForm.style.display = 'block';
        registerSection.style.display = 'none';
    }
}

async function loadRoutes() {
    try {
        routes = await apiCall('/routes');
        const routeSelect = document.getElementById('regRoute');
        routeSelect.innerHTML = '<option value="">Sélectionner un trajet</option>';
        routes.forEach(route => {
            const option = document.createElement('option');
            option.value = route.id;
            option.textContent = `${route.code} - ${route.name}`;
            routeSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading routes:', error);
    }
}

async function handleRouteChange(event) {
    const routeId = event.target.value;
    const pickupPointSelect = document.getElementById('regPickupPoint');
    
    if (!routeId) {
        pickupPointSelect.disabled = true;
        pickupPointSelect.innerHTML = '<option value="">Sélectionner un point de ramassage</option>';
        return;
    }
    
    try {
        pickupPoints = await apiCall(`/routes/${routeId}/pickup-points`);
        pickupPointSelect.disabled = false;
        pickupPointSelect.innerHTML = '<option value="">Sélectionner un point de ramassage</option>';
        pickupPoints.forEach(point => {
            const option = document.createElement('option');
            option.value = point.id;
            option.textContent = `${point.orderIndex}. ${point.name}`;
            pickupPointSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading pickup points:', error);
        showError('Erreur lors du chargement des points de ramassage');
    }
}

async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await apiCall('/auth/login', 'POST', {
            email,
            password,
            role: selectedRole
        });
        
        setAuthToken(response.token);
        setUserInfo({
            email: response.email,
            role: response.role,
            userId: response.userId,
            firstName: response.firstName,
            lastName: response.lastName
        });
        
        // Redirect based on role
        if (response.role === 'STUDENT') {
            window.location.href = 'student-dashboard.html';
        } else if (response.role === 'DRIVER') {
            window.location.href = 'driver-dashboard.html';
        } else if (response.role === 'ADMIN') {
            window.location.href = 'admin-dashboard.html';
        }
    } catch (error) {
        showError(error.message || 'Erreur de connexion');
    }
}

async function handleRegister(event) {
    event.preventDefault();
    
    const email = document.getElementById('regEmail').value;
    
    // Validate email domain
    if (!email.endsWith('@mundiapolis.ma')) {
        showError('Seuls les emails universitaires (@mundiapolis.ma) sont autorisés');
        return;
    }
    
    const formData = {
        email,
        password: document.getElementById('regPassword').value,
        firstName: document.getElementById('regFirstName').value,
        lastName: document.getElementById('regLastName').value,
        phone: document.getElementById('regPhone').value,
        routeId: parseInt(document.getElementById('regRoute').value),
        pickupPointId: parseInt(document.getElementById('regPickupPoint').value)
    };
    
    try {
        const response = await apiCall('/auth/register', 'POST', formData);
        
        setAuthToken(response.token);
        setUserInfo({
            email: response.email,
            role: response.role,
            userId: response.userId,
            firstName: response.firstName,
            lastName: response.lastName
        });
        
        window.location.href = 'student-dashboard.html';
    } catch (error) {
        showError(error.message || 'Erreur lors de l\'inscription');
    }
}
