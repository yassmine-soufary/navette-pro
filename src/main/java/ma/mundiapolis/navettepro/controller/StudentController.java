package ma.mundiapolis.navettepro.controller;

import ma.mundiapolis.navettepro.model.Complaint;
import ma.mundiapolis.navettepro.model.Notification;
import ma.mundiapolis.navettepro.model.StudentRoute;
import ma.mundiapolis.navettepro.service.ComplaintService;
import ma.mundiapolis.navettepro.service.NotificationService;
import ma.mundiapolis.navettepro.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{studentId}/route")
    public ResponseEntity<?> getStudentRoute(@PathVariable Long studentId) {
        Optional<StudentRoute> studentRoute = studentService.getStudentRoute(studentId);
        return studentRoute.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/complaint")
    public ResponseEntity<?> createComplaint(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long routeId = Long.valueOf(request.get("routeId").toString());
            Complaint.ComplaintType type = Complaint.ComplaintType.valueOf(request.get("type").toString());
            String description = request.get("description").toString();

            Complaint complaint = complaintService.createComplaint(studentId, routeId, type, description);
            return ResponseEntity.ok(complaint);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/complaints")
    public ResponseEntity<List<Complaint>> getStudentComplaints(@PathVariable Long studentId) {
        return ResponseEntity.ok(complaintService.getStudentComplaints(studentId));
    }

    @GetMapping("/{studentId}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(studentId));
    }

    @GetMapping("/{studentId}/notifications/unread")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(studentId));
    }

    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
