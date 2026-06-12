package DFP.collegeWithAI.service;

import org.springframework.stereotype.Service;

@Service
public class AiValidationService {

    public boolean esCitaValida(String cita) {
        if (cita == null || cita.trim().isEmpty() || cita.length() < 2) {
            return false;
        }
        String c = cita.toLowerCase();
        return c.contains("sí") || c.contains("si") || c.contains("ok") ||
               c.contains("confirm") || c.contains("adelante") || c.contains("procede");
    }
}
