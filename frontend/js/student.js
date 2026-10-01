let studentInfo = null;
let studentRoute = null;
let locationUpdateInterval = null;

document.addEventListener('DOMContentLoaded', async () => {
    studentInfo = getUserInfo();
    
    if (!studentInfo || studentInfo.role !== 'STUDENT') {
        window.location.href = 'login.html';
        return;
    }
    
    document.getElementById('studentName').textContent = `${studentInfo.firstName} ${studentInfo.lastName}`;
    
    await loadStudentRoute();
    await loadNotifications();
    await loadComplaints();
    
    document.getElementById('complaintForm').addEventListener('submit', handleComplaint);
    
    // Start location tracking
    startLocationTracking();
    
    // Refresh notifications every 30 seconds
    setInterval(loadNotifications, 30000);
});

async function loadStudentRoute() {
    try {
        const response = await apiCall(`/student/${studentInfo.userId}/route`);
        studentRoute = response;
        
        if (response && response.route) {
            document.getElementById('routeName').textContent = `${response.route.code} - ${response.route.name}`;
        } else {
            document.getElementById('routeName').textContent = 'Aucun trajet assigné';
        }
        
        if (response && response.pickupPoint) {
            document.getElementById('pickupPointName').textContent = response.pickupPoint.name;
        } else {
            document.getElementById('pickupPointName').textContent = 'Non défini';
        }
    } catch (error) {
        console.error('Error loading student route:', error);
        if (error.message && error.message.includes('404')) {
            document.getElementById('routeName').textContent = 'Aucun trajet assigné';
            document.getElementById('pickupPointName').textContent = 'Non défini';
            showError('Aucun trajet n\'est assigné. Veuillez contacter l\'administrateur.');
        } else {
            showError('Erreur lors du chargement du trajet: ' + error.message);
        }
    }
}

async function loadNotifications() {
    try {
        const notifications = await apiCall(`/student/${studentInfo.userId}/notifications`);
        const unreadCount = await apiCall(`/student/${studentInfo.userId}/notifications/unread`);
        
        const notificationsList = document.getElementById('notificationsList');
        notificationsList.innerHTML = '';
        
        if (notifications.length === 0) {
            notificationsList.innerHTML = '<p>Aucune notification</p>';
        } else {
            notifications.forEach(notification => {
                const div = document.createElement('div');
                div.className = `notification-item ${!notification.isRead ? 'unread' : ''}`;
                div.innerHTML = `
                    <h4>${notification.title}</h4>
                    <p>${notification.message}</p>
                    <small>${new Date(notification.createdAt).toLocaleString('fr-FR')}</small>
                `;
                div.onclick = () => markAsRead(notification.id);
                notificationsList.appendChild(div);
            });
        }
        
        const unreadBadge = document.getElementById('unreadCount');
        if (unreadCount > 0) {
            unreadBadge.textContent = `${unreadCount} non lue(s)`;
            unreadBadge.style.display = 'inline-block';
        } else {
            unreadBadge.style.display = 'none';
        }
    } catch (error) {
        console.error('Error loading notifications:', error);
    }
}

async function markAsRead(notificationId) {
    try {
        await apiCall(`/student/notifications/${notificationId}/read`, 'PUT');
        loadNotifications();
    } catch (error) {
        console.error('Error marking notification as read:', error);
    }
}

function startLocationTracking() {
    if (!studentRoute || !studentRoute.route) return;
    
    updateBusLocation();
    locationUpdateInterval = setInterval(updateBusLocation, 10000); // Update every 10 seconds
}

function stopLocationTracking() {
    if (locationUpdateInterval) {
        clearInterval(locationUpdateInterval);
        locationUpdateInterval = null;
    }
}

async function updateBusLocation() {
    if (!studentRoute || !studentRoute.route) return;
    
    try {
        const location = await apiCall(`/bus-location/route/${studentRoute.route.id}`);
        
        if (location) {
            // Update map (simplified - in real app, use Google Maps or Leaflet)
            const mapDiv = document.getElementById('map');
            mapDiv.innerHTML = `
                <div style="padding: 20px; text-align: center;">
                    <h4>Position du bus</h4>
                    <p>Latitude: ${location.latitude}</p>
                    <p>Longitude: ${location.longitude}</p>
                    <p>Dernière mise à jour: ${new Date(location.timestamp).toLocaleString('fr-FR')}</p>
                </div>
            `;
            
            // Calculate ETA (simplified)
            calculateETA(location);
        } else {
            document.getElementById('eta').textContent = 'Bus non localisé';
        }
    } catch (error) {
        console.error('Error updating bus location:', error);
    }
}

function calculateETA(location) {
    // Simplified ETA calculation
    // In a real app, you would calculate distance and time based on route
    const etaMinutes = Math.floor(Math.random() * 15) + 5; // Random between 5-20 minutes
    const now = new Date();
    const eta = new Date(now.getTime() + etaMinutes * 60000);
    document.getElementById('eta').textContent = eta.toLocaleTimeString('fr-FR');
}

async function handleComplaint(event) {
    event.preventDefault();
    
    if (!studentRoute || !studentRoute.route) {
        showError('Aucun trajet assigné');
        return;
    }
    
    const formData = {
        studentId: studentInfo.userId,
        routeId: studentRoute.route.id,
        type: document.getElementById('complaintType').value,
        description: document.getElementById('complaintDescription').value
    };
    
    try {
        await apiCall('/student/complaint', 'POST', formData);
        showSuccess('Réclamation envoyée avec succès');
        document.getElementById('complaintForm').reset();
        loadComplaints();
    } catch (error) {
        showError(error.message || 'Erreur lors de l\'envoi de la réclamation');
    }
}

async function loadComplaints() {
    try {
        const complaints = await apiCall(`/student/${studentInfo.userId}/complaints`);
        const complaintsHistory = document.getElementById('complaintsHistory');
        complaintsHistory.innerHTML = '';
        
        if (complaints.length === 0) {
            complaintsHistory.innerHTML = '<p>Aucune réclamation</p>';
        } else {
            complaints.forEach(complaint => {
                const div = document.createElement('div');
                div.className = 'complaint-item';
                div.innerHTML = `
                    <h4>${complaint.type}</h4>
                    <p>${complaint.description}</p>
                    <p><strong>Date:</strong> ${new Date(complaint.createdAt).toLocaleString('fr-FR')}</p>
                    <span class="status ${complaint.status}">${complaint.status}</span>
                `;
                complaintsHistory.appendChild(div);
            });
        }
    } catch (error) {
        console.error('Error loading complaints:', error);
    }
}
