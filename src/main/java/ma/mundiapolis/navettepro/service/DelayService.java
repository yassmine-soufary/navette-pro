package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.Delay;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.DelayRepository;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelayService {

    @Autowired
    private DelayRepository delayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    public Delay createDelay(Long driverId, Long routeId, Integer delayMinutes, String reason) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        Delay delay = new Delay();
        delay.setDriver(driver);
        delay.setRoute(route);
        delay.setDelayMinutes(delayMinutes);
        delay.setReason(reason);

        return delayRepository.save(delay);
    }

    public List<Delay> getDelaysByRoute(Long routeId) {
        return delayRepository.findByRouteId(routeId);
    }
}
