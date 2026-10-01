package ma.mundiapolis.navettepro.controller;

import ma.mundiapolis.navettepro.model.Delay;
import ma.mundiapolis.navettepro.model.StudentRoute;
import ma.mundiapolis.navettepro.model.TripStatus;
import ma.mundiapolis.navettepro.service.DelayService;
import ma.mundiapolis.navettepro.service.StudentService;
import ma.mundiapolis.navettepro.service.TripStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/driver")
@CrossOrigin(origins = "*")
public class DriverController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TripStatusService tripStatusService;

    @Autowired
    private DelayService delayService;

    @GetMapping("/{routeId}/students")
    public ResponseEntity<List<StudentRoute>> getRouteStudents(@PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.getRouteStudents(routeId));
    }

    @PostMapping("/trip-status")
    public ResponseEntity<?> updateTripStatus(@RequestBody Map<String, Object> request) {
        try {
            Long driverId = Long.valueOf(request.get("driverId").toString());
            Long routeId = Long.valueOf(request.get("routeId").toString());
            TripStatus.Status status = TripStatus.Status.valueOf(request.get("status").toString());

            TripStatus tripStatus = tripStatusService.updateTripStatus(driverId, routeId, status);
            return ResponseEntity.ok(tripStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/{routeId}/trip-status")
    public ResponseEntity<?> getCurrentTripStatus(@PathVariable Long routeId) {
        return tripStatusService.getCurrentTripStatus(routeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delay")
    public ResponseEntity<?> createDelay(@RequestBody Map<String, Object> request) {
        try {
            Long driverId = Long.valueOf(request.get("driverId").toString());
            Long routeId = Long.valueOf(request.get("routeId").toString());
            Integer delayMinutes = Integer.valueOf(request.get("delayMinutes").toString());
            String reason = request.get("reason") != null ? request.get("reason").toString() : null;

            Delay delay = delayService.createDelay(driverId, routeId, delayMinutes, reason);
            return ResponseEntity.ok(delay);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }
}
