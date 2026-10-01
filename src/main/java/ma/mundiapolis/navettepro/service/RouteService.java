package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.PickupPoint;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.repository.PickupPointRepository;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PickupPointRepository pickupPointRepository;

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route getRouteByCode(String code) {
        return routeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
    }

    public List<PickupPoint> getPickupPointsByRoute(Long routeId) {
        return pickupPointRepository.findByRouteIdOrderByOrderIndex(routeId);
    }

    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
    }
}
