package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByCode(String code);
    Optional<Route> findByDriverId(Long driverId);
}
