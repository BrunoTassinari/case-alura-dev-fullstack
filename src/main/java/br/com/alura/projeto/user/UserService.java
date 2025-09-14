package br.com.alura.projeto.user;

import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public List<InstructorOptionDTO> getInstructorsOptions() {
        return userRepository.findAllInstructors()
                .stream()
                .map(u -> new InstructorOptionDTO(u.getId(), u.getName() + " - " + u.getEmail())).toList();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
