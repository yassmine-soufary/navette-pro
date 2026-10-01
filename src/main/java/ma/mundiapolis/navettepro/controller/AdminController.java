package ma.mundiapolis.navettepro.controller;

import ma.mundiapolis.navettepro.model.Complaint;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.service.AdminService;
import ma.mundiapolis.navettepro.service.ComplaintService;
import ma.mundiapolis.navettepro.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/drivers")
    public ResponseEntity<List<User>> getAllDrivers() {
        return ResponseEntity.ok(adminService.getAllDrivers());
    }

    @PostMapping("/drivers")
    public ResponseEntity<?> createDriver(@RequestBody Map<String, String> request) {
        try {
            User driver = adminService.createDriver(
                request.get("email"),
                request.get("password"),
                request.get("firstName"),
                request.get("lastName"),
                request.get("phone")
            );
            return ResponseEntity.ok(driver);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @DeleteMapping("/drivers/{driverId}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long driverId) {
        try {
            adminService.deleteDriver(driverId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @PutMapping("/drivers/{driverId}/email")
    public ResponseEntity<?> updateDriverEmail(@PathVariable Long driverId, @RequestBody Map<String, String> request) {
        try {
            User driver = adminService.updateDriverEmail(driverId, request.get("email"));
            return ResponseEntity.ok(driver);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @PutMapping("/drivers/{driverId}/password")
    public ResponseEntity<?> updateDriverPassword(@PathVariable Long driverId, @RequestBody Map<String, String> request) {
        try {
            User driver = adminService.updateDriverPassword(driverId, request.get("password"));
            return ResponseEntity.ok(driver);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @PutMapping("/complaints/{complaintId}/status")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long complaintId, @RequestBody Map<String, String> request) {
        try {
            Complaint.ComplaintStatus status = Complaint.ComplaintStatus.valueOf(request.get("status"));
            Complaint complaint = complaintService.updateComplaintStatus(complaintId, status);
            return ResponseEntity.ok(complaint);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(adminService.getStatistics());
    }
}
