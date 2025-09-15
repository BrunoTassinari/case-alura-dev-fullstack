package br.com.alura.projeto.user;

import br.com.alura.projeto.exceptions.DataConflictException;
import br.com.alura.projeto.exceptions.ResourceNotFoundException;
import br.com.alura.projeto.user.dto.InstructorOptionDTO;
import br.com.alura.projeto.user.dto.NewUserDTO;
import br.com.alura.projeto.user.dto.UserListItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public List<InstructorOptionDTO> getInstructorsOptions() {
        return userRepository.findAllInstructors()
                .stream()
                .map(u -> new InstructorOptionDTO(u.getId(), u.getName() + " - " + u.getEmail())).toList();
    }

    @Transactional
    public void createUser(NewUserDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new DataConflictException("Email already exists");
        }

        User user = dto.toModel();
        userRepository.save(user);
    }

    public List<UserListItemDTO> listUsers() {
        return userRepository.findAll().stream().map(UserListItemDTO::new).toList();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
