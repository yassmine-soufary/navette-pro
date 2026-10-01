package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.TripStatus;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import ma.mundiapolis.navettepro.repository.TripStatusRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TripStatusService {

    @Autowired
    private TripStatusRepository tripStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    public TripStatus updateTripStatus(Long driverId, Long routeId, TripStatus.Status status) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        TripStatus tripStatus = new TripStatus();
        tripStatus.setDriver(driver);
        tripStatus.setRoute(route);
        tripStatus.setStatus(status);

        if (status == TripStatus.Status.IN_PROGRESS) {
            tripStatus.setStartTime(LocalDateTime.now());
        } else if (status == TripStatus.Status.COMPLETED || status == TripStatus.Status.CANCELLED) {
            tripStatus.setEndTime(LocalDateTime.now());
        }

        return tripStatusRepository.save(tripStatus);
    }

    public Optional<TripStatus> getCurrentTripStatus(Long routeId) {
        return tripStatusRepository.findTopByRouteIdOrderByCreatedAtDesc(routeId);
    }
}
