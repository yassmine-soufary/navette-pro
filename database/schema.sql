-- NavettePro Database Schema
CREATE DATABASE IF NOT EXISTS navettepro;
USE navettepro;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'DRIVER', 'ADMIN') NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table des trajets
CREATE TABLE IF NOT EXISTS routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    driver_id BIGINT,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Table des points de ramassage
CREATE TABLE IF NOT EXISTS pickup_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    order_index INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

-- Table des inscriptions étudiants aux trajets
CREATE TABLE IF NOT EXISTS student_routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    pickup_point_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (pickup_point_id) REFERENCES pickup_points(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_route (student_id, route_id)
);

-- Table des positions GPS des bus
CREATE TABLE IF NOT EXISTS bus_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    speed DECIMAL(5, 2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

-- Table des réclamations
CREATE TABLE IF NOT EXISTS complaints (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    type ENUM('DELAY', 'OVERLOAD', 'BEHAVIOR', 'OTHER') NOT NULL,
    description TEXT NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

-- Table des notifications
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('BUS_APPROACHING', 'DELAY', 'ROUTE_CHANGE', 'GENERAL') NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table des retards
CREATE TABLE IF NOT EXISTS delays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    delay_minutes INT NOT NULL,
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table des évaluations de chauffeurs
CREATE TABLE IF NOT EXISTS driver_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_rating (driver_id, student_id, DATE(created_at))
);

-- Table des statuts de trajet
CREATE TABLE IF NOT EXISTS trip_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (driver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insertion de l'administrateur par défaut
INSERT INTO users (email, password, role, first_name, last_name) 
VALUES ('admin@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'ADMIN', 'Admin', 'System')
ON DUPLICATE KEY UPDATE email=email;

-- Insertion des trajets et chauffeurs
-- Trajet A
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('ahmed.benali@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Ahmed', 'Benali', '0612345678')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_a_id = (SELECT id FROM users WHERE email = 'ahmed.benali@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('A', 'RAHMA / OULFA', @driver_a_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_a_id = (SELECT id FROM routes WHERE code = 'A');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_a_id, 'Station Petromine Rahma', 1),
(@route_a_id, 'Rond point George', 2),
(@route_a_id, 'Mosquée Haj Fatteh', 3),
(@route_a_id, 'Rond point ALMAZ (en face McDonalds)', 4),
(@route_a_id, 'BSK Mosquée Al Saoud', 5),
(@route_a_id, 'Gare Bouskoura', 6);

-- Trajet B
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('youssef.elamrani@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Youssef', 'El Amrani', '0612345679')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_b_id = (SELECT id FROM users WHERE email = 'youssef.elamrani@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('B', 'Trajet B', @driver_b_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_b_id = (SELECT id FROM routes WHERE code = 'B');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_b_id, 'Rond point Al Moustakbal', 1),
(@route_b_id, 'Rond point Bouskoura sortie autoroute', 2),
(@route_b_id, 'Péage Nouaceur', 3),
(@route_b_id, 'Zaouia Nouaceur', 4);

-- Trajet C
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('hamza.rachidi@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Hamza', 'Rachidi', '0612345680')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_c_id = (SELECT id FROM users WHERE email = 'hamza.rachidi@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('C', 'Trajet C', @driver_c_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_c_id = (SELECT id FROM routes WHERE code = 'C');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_c_id, 'Commune Sidi Belyout terminus bus', 1),
(@route_c_id, 'Laouina Hara', 2),
(@route_c_id, 'Intersection Bd Zerktouni & Touajtate', 3),
(@route_c_id, 'Croisement Bd Anfa & Bd Zerktouni (Station TOTAL)', 4),
(@route_c_id, 'Mundiapolis Roudani', 5),
(@route_c_id, 'Agence Inwi Biranzarane', 6),
(@route_c_id, 'Bd Sokrate La Poste', 7),
(@route_c_id, 'Carrefour Yaacoub Mansour', 8),
(@route_c_id, 'Rond point Oulmes (Bd Ghandi Lydec)', 9),
(@route_c_id, 'Intersection Bd Roudani & Bd Ghandi', 10),
(@route_c_id, 'ISCAE Route El Jadida', 11);

-- Trajet D
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('omar.tazi@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Omar', 'Tazi', '0612345681')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_d_id = (SELECT id FROM users WHERE email = 'omar.tazi@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('D', 'Trajet D', @driver_d_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_d_id = (SELECT id FROM routes WHERE code = 'D');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_d_id, 'Rond point Emile Zola', 1),
(@route_d_id, 'Mall Ibn Tachfine', 2),
(@route_d_id, 'Résidence Salam ATW', 3),
(@route_d_id, 'Acima Ibn Tachfine', 4),
(@route_d_id, 'Pharmacie Adil', 5),
(@route_d_id, 'Pharmacie Taha', 6),
(@route_d_id, 'Café Mouhajir', 7),
(@route_d_id, 'Rond point Masrah', 8),
(@route_d_id, 'Marché gros légumes', 9),
(@route_d_id, 'Planete House', 10);

-- Trajet E
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('karim.alaoui@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Karim', 'Alaoui', '0612345682')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_e_id = (SELECT id FROM users WHERE email = 'karim.alaoui@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('E', 'Trajet E', @driver_e_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_e_id = (SELECT id FROM routes WHERE code = 'E');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_e_id, 'Intersection Bd Fida & Bd Med 6 (Station Afriquia)', 1),
(@route_e_id, 'Marjane Drissia', 2),
(@route_e_id, 'Dar Touzani', 3),
(@route_e_id, 'Bd Med 6 en face Bd Oued Dahab', 4),
(@route_e_id, 'Sanitube', 5),
(@route_e_id, 'Route Mediouna', 6),
(@route_e_id, 'Mosquée Hosna Bd Qods', 7),
(@route_e_id, 'Café Kyoto', 8),
(@route_e_id, 'École Majoralle Bd Qods', 9),
(@route_e_id, 'Café Fragola', 10),
(@route_e_id, 'Café Famagusta', 11),
(@route_e_id, 'UIC Bouskoura', 12);

-- Trajet F
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('mehdi.berrada@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Mehdi', 'Berrada', '0612345683')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_f_id = (SELECT id FROM users WHERE email = 'mehdi.berrada@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('F', 'Trajet F', @driver_f_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_f_id = (SELECT id FROM routes WHERE code = 'F');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_f_id, 'Shell Berrechid', 1),
(@route_f_id, 'BQ Chaabi Bd Med 5', 2),
(@route_f_id, 'Lycée Ouled Hriz', 3),
(@route_f_id, 'BIM El Qods', 4),
(@route_f_id, 'Mosquée Abou Bakr Seddik', 5),
(@route_f_id, 'Croisement Quartier Essaad', 6),
(@route_f_id, 'Route Derroua Hemam Turk', 7),
(@route_f_id, 'Rabha', 8),
(@route_f_id, 'Chaabi', 9),
(@route_f_id, 'Lotissement Oumnia', 10),
(@route_f_id, 'Rabii', 11),
(@route_f_id, 'Municipalité Berrechid', 12),
(@route_f_id, 'Marwa Café Layali', 13);

-- Trajet G
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('rachid.idrissi@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Rachid', 'Idrissi', '0612345684')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_g_id = (SELECT id FROM users WHERE email = 'rachid.idrissi@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('G', 'Trajet G', @driver_g_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_g_id = (SELECT id FROM routes WHERE code = 'G');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_g_id, 'Intersection Bd Grande Ceinture & Route Rabat', 1),
(@route_g_id, 'Marjane Ain Sebaa', 2),
(@route_g_id, 'Rond point Saada', 3),
(@route_g_id, 'Rond point Houda', 4),
(@route_g_id, '2M Ain Sebaa', 5),
(@route_g_id, 'Stade El Arbi Zaouli', 6),
(@route_g_id, 'Kissaria Hay Mohamadi', 7);

-- Trajet H
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('soufiane.kabbaj@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Soufiane', 'Kabbaj', '0612345685')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_h_id = (SELECT id FROM users WHERE email = 'soufiane.kabbaj@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('H', 'Trajet H', @driver_h_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_h_id = (SELECT id FROM routes WHERE code = 'H');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_h_id, 'Rond point Anassi', 1),
(@route_h_id, 'Rond point Al Azhar', 2),
(@route_h_id, 'Mosquée Al Khalil', 3),
(@route_h_id, 'Rond point Tit Mellil', 4);

-- Trajet I
INSERT INTO users (email, password, role, first_name, last_name, phone) 
VALUES ('zakaria.naim@navettepro.ma', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8p6/Pa', 'DRIVER', 'Zakaria', 'Naim', '0612345686')
ON DUPLICATE KEY UPDATE email=email;

SET @driver_i_id = (SELECT id FROM users WHERE email = 'zakaria.naim@navettepro.ma');
INSERT INTO routes (code, name, driver_id) VALUES ('I', 'Trajet I', @driver_i_id)
ON DUPLICATE KEY UPDATE code=code;

SET @route_i_id = (SELECT id FROM routes WHERE code = 'I');
INSERT INTO pickup_points (route_id, name, order_index) VALUES
(@route_i_id, 'Route Azemmour', 1),
(@route_i_id, 'Marjane Hay Hassani', 2),
(@route_i_id, 'Pharmacie Oum Rabii / Bd Oued Oum Rabii', 3);
