package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.BusLocation;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.BusLocationRepository;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BusLocationService {

    @Autowired
    private BusLocationRepository busLocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    public BusLocation updateLocation(Long driverId, BigDecimal latitude, BigDecimal longitude, BigDecimal speed) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        Route route = routeRepository.findByDriverId(driverId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé pour ce chauffeur"));

        BusLocation location = new BusLocation();
        location.setDriver(driver);
        location.setRoute(route);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setSpeed(speed);

        return busLocationRepository.save(location);
    }

    public Optional<BusLocation> getLatestLocationByRoute(Long routeId) {
        return busLocationRepository.findLatestByRouteId(routeId);
    }

    public Optional<BusLocation> getLatestLocationByDriver(Long driverId) {
        return busLocationRepository.findTopByDriverIdOrderByTimestampDesc(driverId);
    }
}
