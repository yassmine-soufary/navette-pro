package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.PickupPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
    List<PickupPoint> findByRouteIdOrderByOrderIndex(Long routeId);
}
