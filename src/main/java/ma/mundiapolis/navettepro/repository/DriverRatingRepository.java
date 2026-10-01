package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {
    List<DriverRating> findByDriverId(Long driverId);
}
