package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.DriverRating;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllDrivers() {
        return userRepository.findByRole(User.Role.DRIVER);
    }

    public User createDriver(String email, String password, String firstName, String lastName, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User driver = new User();
        driver.setEmail(email);
        driver.setPassword(passwordEncoder.encode(password));
        driver.setRole(User.Role.DRIVER);
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setPhone(phone);

        return userRepository.save(driver);
    }

    public void deleteDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
        if (driver.getRole() != User.Role.DRIVER) {
            throw new RuntimeException("Cet utilisateur n'est pas un chauffeur");
        }
        userRepository.delete(driver);
    }

    public User updateDriverEmail(Long driverId, String email) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
        if (userRepository.existsByEmail(email) && !driver.getEmail().equals(email)) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        driver.setEmail(email);
        return userRepository.save(driver);
    }

    public User updateDriverPassword(Long driverId, String password) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
        driver.setPassword(passwordEncoder.encode(password));
        return userRepository.save(driver);
    }

    public Map<String, Object> getStatistics() {
        long totalStudents = userRepository.findByRole(User.Role.STUDENT).size();
        long totalDrivers = userRepository.findByRole(User.Role.DRIVER).size();
        long totalRoutes = routeRepository.count();
        long pendingComplaints = complaintRepository.findAll().stream()
                .filter(c -> c.getStatus() == ma.mundiapolis.navettepro.model.Complaint.ComplaintStatus.PENDING)
                .count();

        Map<String, Double> driverRatings = driverRatingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    r -> r.getDriver().getFirstName() + " " + r.getDriver().getLastName(),
                    Collectors.averagingDouble(DriverRating::getRating)
                ));

        return Map.of(
            "totalStudents", totalStudents,
            "totalDrivers", totalDrivers,
            "totalRoutes", totalRoutes,
            "pendingComplaints", pendingComplaints,
            "driverRatings", driverRatings
        );
    }
}
