package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.dto.AuthRequest;
import ma.mundiapolis.navettepro.dto.AuthResponse;
import ma.mundiapolis.navettepro.dto.RegisterRequest;
import ma.mundiapolis.navettepro.model.PickupPoint;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.StudentRoute;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.PickupPointRepository;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import ma.mundiapolis.navettepro.repository.StudentRouteRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import ma.mundiapolis.navettepro.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRouteRepository studentRouteRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PickupPointRepository pickupPointRepository;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        // Vérification spéciale pour admin
        if (user.getRole() == User.Role.ADMIN) {
            if (!request.getPassword().equals("ad123")) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }
        } else if (user.getRole() == User.Role.DRIVER) {
            // Pour les chauffeurs, vérifier le mot de passe par défaut ou encodé
            if (!request.getPassword().equals("1234") && !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }
        } else {
            // Pour les étudiants, vérifier le mot de passe encodé
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getId(), 
                user.getFirstName(), user.getLastName());
    }

    public AuthResponse register(RegisterRequest request) {
        // Vérifier que l'email est un email universitaire
        if (!request.getEmail().endsWith("@mundiapolis.ma")) {
            throw new RuntimeException("Seuls les emails universitaires (@mundiapolis.ma) sont autorisés");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.STUDENT);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        user = userRepository.save(user);

        // Inscrire l'étudiant au trajet choisi
        if (request.getRouteId() != null && request.getPickupPointId() != null) {
            Route route = routeRepository.findById(request.getRouteId())
                    .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
            PickupPoint pickupPoint = pickupPointRepository.findById(request.getPickupPointId())
                    .orElseThrow(() -> new RuntimeException("Point de ramassage non trouvé"));
            
            StudentRoute studentRoute = new StudentRoute();
            studentRoute.setStudent(user);
            studentRoute.setRoute(route);
            studentRoute.setPickupPoint(pickupPoint);
            studentRouteRepository.save(studentRoute);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getId(), 
                user.getFirstName(), user.getLastName());
    }
}
