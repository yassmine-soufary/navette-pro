package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.Delay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelayRepository extends JpaRepository<Delay, Long> {
    List<Delay> findByRouteId(Long routeId);
}
