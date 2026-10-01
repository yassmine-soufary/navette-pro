let adminInfo = null;

document.addEventListener('DOMContentLoaded', async () => {
    adminInfo = getUserInfo();
    
    if (!adminInfo || adminInfo.role !== 'ADMIN') {
        window.location.href = 'login.html';
        return;
    }
    
    await loadStatistics();
    await loadDrivers();
    await loadComplaints();
    await loadBusesLocation();
    
    document.getElementById('driverForm').addEventListener('submit', handleAddDriver);
    
    // Refresh data every 30 seconds
    setInterval(async () => {
        await loadStatistics();
        await loadBusesLocation();
    }, 30000);
});

async function loadStatistics() {
    try {
        const stats = await apiCall('/admin/statistics');
        const statsDiv = document.getElementById('statistics');
        
        statsDiv.innerHTML = `
            <div class="stat-item">
                <strong>Total étudiants:</strong> ${stats.totalStudents}
            </div>
            <div class="stat-item">
                <strong>Total chauffeurs:</strong> ${stats.totalDrivers}
            </div>
            <div class="stat-item">
                <strong>Total trajets:</strong> ${stats.totalRoutes}
            </div>
            <div class="stat-item">
                <strong>Réclamations en attente:</strong> ${stats.pendingComplaints}
            </div>
        `;
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

async function loadDrivers() {
    try {
        const drivers = await apiCall('/admin/drivers');
        const driversList = document.getElementById('driversList');
        driversList.innerHTML = '';
        
        if (drivers.length === 0) {
            driversList.innerHTML = '<p>Aucun chauffeur</p>';
        } else {
            drivers.forEach(driver => {
                const div = document.createElement('div');
                div.className = 'driver-item';
                div.innerHTML = `
                    <div>
                        <strong>${driver.firstName} ${driver.lastName}</strong>
                        <p style="margin: 5px 0; color: #666;">${driver.email}</p>
                        <small>${driver.phone || 'Pas de téléphone'}</small>
                    </div>
                    <div class="driver-actions">
                        <button class="btn btn-secondary" onclick="editDriverEmail(${driver.id})">Modifier email</button>
                        <button class="btn btn-secondary" onclick="editDriverPassword(${driver.id})">Modifier mot de passe</button>
                        <button class="btn btn-warning" onclick="deleteDriver(${driver.id})">Supprimer</button>
                    </div>
                `;
                driversList.appendChild(div);
            });
        }
    } catch (error) {
        console.error('Error loading drivers:', error);
    }
}

async function loadComplaints() {
    try {
        const complaints = await apiCall('/admin/complaints');
        const complaintsList = document.getElementById('complaintsList');
        complaintsList.innerHTML = '';
        
        if (complaints.length === 0) {
            complaintsList.innerHTML = '<p>Aucune réclamation</p>';
        } else {
            complaints.forEach(complaint => {
                const div = document.createElement('div');
                div.className = 'complaint-item';
                div.innerHTML = `
                    <h4>${complaint.type} - ${complaint.student.firstName} ${complaint.student.lastName}</h4>
                    <p>${complaint.description}</p>
                    <p><strong>Trajet:</strong> ${complaint.route.code} - ${complaint.route.name}</p>
                    <p><strong>Date:</strong> ${new Date(complaint.createdAt).toLocaleString('fr-FR')}</p>
                    <span class="status ${complaint.status}">${complaint.status}</span>
                    <div style="margin-top: 10px;">
                        <select onchange="updateComplaintStatus(${complaint.id}, this.value)">
                            <option value="PENDING" ${complaint.status === 'PENDING' ? 'selected' : ''}>En attente</option>
                            <option value="IN_PROGRESS" ${complaint.status === 'IN_PROGRESS' ? 'selected' : ''}>En cours</option>
                            <option value="RESOLVED" ${complaint.status === 'RESOLVED' ? 'selected' : ''}>Résolu</option>
                            <option value="REJECTED" ${complaint.status === 'REJECTED' ? 'selected' : ''}>Rejeté</option>
                        </select>
                    </div>
                `;
                complaintsList.appendChild(div);
            });
        }
    } catch (error) {
        console.error('Error loading complaints:', error);
    }
}

async function loadBusesLocation() {
    try {
        const routes = await apiCall('/routes');
        const mapDiv = document.getElementById('map');
        
        let html = '<h4>Position des bus:</h4><ul style="list-style: none; padding: 0;">';
        
        for (const route of routes) {
            if (route.driver) {
                try {
                    const location = await apiCall(`/bus-location/route/${route.id}`);
                    if (location) {
                        html += `<li style="padding: 10px; margin: 5px 0; background: #f0f0f0; border-radius: 5px;">
                            <strong>${route.code} - ${route.name}</strong><br>
                            Chauffeur: ${route.driver.firstName} ${route.driver.lastName}<br>
                            Position: ${location.latitude}, ${location.longitude}<br>
                            Dernière mise à jour: ${new Date(location.timestamp).toLocaleString('fr-FR')}
                        </li>`;
                    }
                } catch (error) {
                    // Bus not located yet
                }
            }
        }
        
        html += '</ul>';
        mapDiv.innerHTML = html;
    } catch (error) {
        console.error('Error loading buses location:', error);
    }
}

function showAddDriverForm() {
    document.getElementById('addDriverForm').style.display = 'block';
}

function hideAddDriverForm() {
    document.getElementById('addDriverForm').style.display = 'none';
    document.getElementById('driverForm').reset();
}

async function handleAddDriver(event) {
    event.preventDefault();
    
    const formData = {
        email: document.getElementById('driverEmail').value,
        password: document.getElementById('driverPassword').value,
        firstName: document.getElementById('driverFirstName').value,
        lastName: document.getElementById('driverLastName').value,
        phone: document.getElementById('driverPhone').value
    };
    
    try {
        await apiCall('/admin/drivers', 'POST', formData);
        showSuccess('Chauffeur créé avec succès');
        hideAddDriverForm();
        loadDrivers();
    } catch (error) {
        showError(error.message || 'Erreur lors de la création du chauffeur');
    }
}

async function deleteDriver(driverId) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce chauffeur ?')) {
        return;
    }
    
    try {
        await apiCall(`/admin/drivers/${driverId}`, 'DELETE');
        showSuccess('Chauffeur supprimé avec succès');
        loadDrivers();
    } catch (error) {
        showError(error.message || 'Erreur lors de la suppression du chauffeur');
    }
}

async function editDriverEmail(driverId) {
    const newEmail = prompt('Nouvel email:');
    if (!newEmail) return;
    
    try {
        await apiCall(`/admin/drivers/${driverId}/email`, 'PUT', { email: newEmail });
        showSuccess('Email modifié avec succès');
        loadDrivers();
    } catch (error) {
        showError(error.message || 'Erreur lors de la modification de l\'email');
    }
}

async function editDriverPassword(driverId) {
    const newPassword = prompt('Nouveau mot de passe:');
    if (!newPassword) return;
    
    try {
        await apiCall(`/admin/drivers/${driverId}/password`, 'PUT', { password: newPassword });
        showSuccess('Mot de passe modifié avec succès');
    } catch (error) {
        showError(error.message || 'Erreur lors de la modification du mot de passe');
    }
}

async function updateComplaintStatus(complaintId, status) {
    try {
        await apiCall(`/admin/complaints/${complaintId}/status`, 'PUT', { status });
        showSuccess('Statut de la réclamation mis à jour');
        loadComplaints();
    } catch (error) {
        showError(error.message || 'Erreur lors de la mise à jour du statut');
    }
}

function generateReport() {
    // In a real app, this would generate a PDF report
    showSuccess('Rapport mensuel généré (fonctionnalité à implémenter)');
}
