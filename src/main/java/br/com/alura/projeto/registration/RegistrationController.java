package br.com.alura.projeto.registration;

import br.com.alura.projeto.registration.dto.NewRegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration/new")
    public ResponseEntity newRegistration(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        registrationService.createRegistration(newRegistration);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = registrationService.generateReport();
        return ResponseEntity.ok(items);
    }

}
