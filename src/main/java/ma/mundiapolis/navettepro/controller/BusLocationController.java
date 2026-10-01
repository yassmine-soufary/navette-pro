package ma.mundiapolis.navettepro.controller;

import ma.mundiapolis.navettepro.model.BusLocation;
import ma.mundiapolis.navettepro.service.BusLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bus-location")
@CrossOrigin(origins = "*")
public class BusLocationController {

    @Autowired
    private BusLocationService busLocationService;

    @PostMapping("/update")
    public ResponseEntity<?> updateLocation(@RequestBody Map<String, Object> request) {
        try {
            Long driverId = Long.valueOf(request.get("driverId").toString());
            BigDecimal latitude = new BigDecimal(request.get("latitude").toString());
            BigDecimal longitude = new BigDecimal(request.get("longitude").toString());
            BigDecimal speed = request.get("speed") != null ? 
                new BigDecimal(request.get("speed").toString()) : BigDecimal.ZERO;

            BusLocation location = busLocationService.updateLocation(driverId, latitude, longitude, speed);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<?> getLocationByRoute(@PathVariable Long routeId) {
        Optional<BusLocation> location = busLocationService.getLatestLocationByRoute(routeId);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<?> getLocationByDriver(@PathVariable Long driverId) {
        Optional<BusLocation> location = busLocationService.getLatestLocationByDriver(driverId);
        return location.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
