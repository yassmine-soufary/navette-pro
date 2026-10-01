package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.BusLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusLocationRepository extends JpaRepository<BusLocation, Long> {
    @Query("SELECT bl FROM BusLocation bl WHERE bl.route.id = :routeId ORDER BY bl.timestamp DESC")
    Optional<BusLocation> findLatestByRouteId(Long routeId);
    
    Optional<BusLocation> findTopByDriverIdOrderByTimestampDesc(Long driverId);
}
