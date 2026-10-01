package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.StudentRoute;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.StudentRouteRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRouteRepository studentRouteRepository;

    public Optional<StudentRoute> getStudentRoute(Long studentId) {
        return studentRouteRepository.findByStudentId(studentId);
    }

    public List<StudentRoute> getRouteStudents(Long routeId) {
        return studentRouteRepository.findByRouteId(routeId);
    }

    public User getStudentById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
    }
}
