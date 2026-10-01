package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripStatusRepository extends JpaRepository<TripStatus, Long> {
    Optional<TripStatus> findTopByRouteIdOrderByCreatedAtDesc(Long routeId);
    Optional<TripStatus> findTopByDriverIdOrderByCreatedAtDesc(Long driverId);
}
