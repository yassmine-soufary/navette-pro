package ma.mundiapolis.navettepro.repository;

import ma.mundiapolis.navettepro.model.StudentRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRouteRepository extends JpaRepository<StudentRoute, Long> {
    Optional<StudentRoute> findByStudentId(Long studentId);
    List<StudentRoute> findByRouteId(Long routeId);
    boolean existsByStudentIdAndRouteId(Long studentId, Long routeId);
}
