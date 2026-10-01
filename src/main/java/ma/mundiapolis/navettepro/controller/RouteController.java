package ma.mundiapolis.navettepro.controller;

import ma.mundiapolis.navettepro.model.PickupPoint;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Route> getRouteByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(routeService.getRouteByCode(code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{routeId}/pickup-points")
    public ResponseEntity<List<PickupPoint>> getPickupPoints(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getPickupPointsByRoute(routeId));
    }
}
