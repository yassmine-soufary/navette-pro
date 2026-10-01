let driverInfo = null;
let driverRoute = null;
let locationWatchId = null;

document.addEventListener('DOMContentLoaded', async () => {
    driverInfo = getUserInfo();
    
    if (!driverInfo || driverInfo.role !== 'DRIVER') {
        window.location.href = 'login.html';
        return;
    }
    
    document.getElementById('driverName').textContent = `${driverInfo.firstName} ${driverInfo.lastName}`;
    
    await loadDriverRoute();
    await loadRouteStudents();
    
    document.getElementById('delayForm').addEventListener('submit', handleDelay);
    
    // Load current trip status
    if (driverRoute) {
        loadTripStatus();
    }
});

async function loadDriverRoute() {
    try {
        // Get route assigned to driver
        const routes = await apiCall('/routes');
        const route = routes.find(r => r.driver && r.driver.id === driverInfo.userId);
        
        if (route) {
            driverRoute = route;
            document.getElementById('routeName').textContent = `${route.code} - ${route.name}`;
            
            // Load pickup points for map
            const pickupPoints = await apiCall(`/routes/${route.id}/pickup-points`);
            displayRouteMap(pickupPoints);
        } else {
            document.getElementById('routeName').textContent = 'Aucun trajet assigné';
            showError('Aucun trajet n\'est assigné à ce chauffeur. Contactez l\'administrateur.');
        }
    } catch (error) {
        console.error('Error loading driver route:', error);
        showError('Erreur lors du chargement du trajet: ' + error.message);
    }
}

function displayRouteMap(pickupPoints) {
    const mapDiv = document.getElementById('map');
    let html = '<h4>Points de ramassage:</h4><ul style="list-style: none; padding: 0;">';
    pickupPoints.forEach(point => {
        html += `<li style="padding: 10px; margin: 5px 0; background: #f0f0f0; border-radius: 5px;">
            ${point.orderIndex}. ${point.name}
        </li>`;
    });
    html += '</ul>';
    mapDiv.innerHTML = html;
}

async function loadRouteStudents() {
    if (!driverRoute) return;
    
    try {
        const students = await apiCall(`/driver/${driverRoute.id}/students`);
        const studentsList = document.getElementById('studentsList');
        studentsList.innerHTML = '';
        
        if (students.length === 0) {
            studentsList.innerHTML = '<p>Aucun étudiant inscrit</p>';
        } else {
            students.forEach(studentRoute => {
                const div = document.createElement('div');
                div.className = 'student-item';
                div.innerHTML = `
                    <div>
                        <strong>${studentRoute.student.firstName} ${studentRoute.student.lastName}</strong>
                        <p style="margin: 5px 0; color: #666;">${studentRoute.student.email}</p>
                        <small>Point de ramassage: ${studentRoute.pickupPoint.name}</small>
                    </div>
                `;
                studentsList.appendChild(div);
            });
        }
    } catch (error) {
        console.error('Error loading route students:', error);
    }
}

async function updateTripStatus() {
    if (!driverRoute) return;
    
    const status = document.getElementById('tripStatus').value;
    
    try {
        await apiCall('/driver/trip-status', 'POST', {
            driverId: driverInfo.userId,
            routeId: driverRoute.id,
            status: status
        });
        showSuccess('Statut mis à jour');
    } catch (error) {
        showError(error.message || 'Erreur lors de la mise à jour du statut');
    }
}

async function loadTripStatus() {
    if (!driverRoute) return;
    
    try {
        const status = await apiCall(`/driver/${driverRoute.id}/trip-status`);
        if (status) {
            document.getElementById('tripStatus').value = status.status;
        }
    } catch (error) {
        console.error('Error loading trip status:', error);
    }
}

async function handleDelay(event) {
    event.preventDefault();
    
    if (!driverRoute) {
        showError('Aucun trajet assigné');
        return;
    }
    
    const formData = {
        routeId: driverRoute.id,
        driverId: driverInfo.userId,
        delayMinutes: parseInt(document.getElementById('delayMinutes').value),
        reason: document.getElementById('delayReason').value
    };
    
    try {
        // Note: This endpoint needs to be created in the backend
        await apiCall('/driver/delay', 'POST', formData);
        showSuccess('Retard signalé');
        document.getElementById('delayForm').reset();
    } catch (error) {
        showError(error.message || 'Erreur lors du signalement du retard');
    }
}

function startLocationTracking() {
    if (!navigator.geolocation) {
        showError('La géolocalisation n\'est pas supportée par votre navigateur');
        return;
    }
    
    const statusDiv = document.getElementById('locationStatus');
    statusDiv.textContent = 'Activation de la géolocalisation...';
    
    locationWatchId = navigator.geolocation.watchPosition(
        async (position) => {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;
            const speed = position.coords.speed || 0;
            
            statusDiv.textContent = `Position: ${latitude.toFixed(6)}, ${longitude.toFixed(6)}`;
            
            try {
                await apiCall('/bus-location/update', 'POST', {
                    driverId: driverInfo.userId,
                    latitude: latitude,
                    longitude: longitude,
                    speed: speed
                });
            } catch (error) {
                console.error('Error updating location:', error);
            }
        },
        (error) => {
            showError('Erreur de géolocalisation: ' + error.message);
            statusDiv.textContent = 'Erreur de géolocalisation';
        },
        {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        }
    );
    
    document.getElementById('startTracking').style.display = 'none';
    document.getElementById('stopTracking').style.display = 'inline-block';
}

function stopLocationTracking() {
    if (locationWatchId !== null) {
        navigator.geolocation.clearWatch(locationWatchId);
        locationWatchId = null;
        document.getElementById('locationStatus').textContent = 'Suivi GPS arrêté';
        document.getElementById('startTracking').style.display = 'inline-block';
        document.getElementById('stopTracking').style.display = 'none';
    }
}
