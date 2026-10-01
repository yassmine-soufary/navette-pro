package ma.mundiapolis.navettepro.service;

import ma.mundiapolis.navettepro.model.Complaint;
import ma.mundiapolis.navettepro.model.Route;
import ma.mundiapolis.navettepro.model.User;
import ma.mundiapolis.navettepro.repository.ComplaintRepository;
import ma.mundiapolis.navettepro.repository.RouteRepository;
import ma.mundiapolis.navettepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    public Complaint createComplaint(Long studentId, Long routeId, Complaint.ComplaintType type, String description) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        Complaint complaint = new Complaint();
        complaint.setStudent(student);
        complaint.setRoute(route);
        complaint.setType(type);
        complaint.setDescription(description);
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getStudentComplaints(Long studentId) {
        return complaintRepository.findByStudentId(studentId);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint updateComplaintStatus(Long complaintId, Complaint.ComplaintStatus status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }
}
