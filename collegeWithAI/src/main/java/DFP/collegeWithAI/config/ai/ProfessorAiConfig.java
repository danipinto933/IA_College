package DFP.collegeWithAI.config.ai;

import java.util.function.Function;
import DFP.collegeWithAI.dto.ai.AiDto.*;
import DFP.collegeWithAI.service.ProfessorService;
import DFP.collegeWithAI.service.AiValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ProfessorAiConfig {

    private final ProfessorService professorService;
    private final AiValidationService aiValidationService;

    public ProfessorAiConfig(ProfessorService professorService, AiValidationService aiValidationService) {
        this.professorService = professorService;
        this.aiValidationService = aiValidationService;
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Crea un profesor. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureProfessorRequest, ChatResponse> createProfessor() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                professorService.addProfessor(req.data());
                return new ChatResponse("ÉXITO: Profesor creado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Actualiza un profesor. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureUpdateProfessorRequest, ChatResponse> updateProfessor() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                professorService.updateProfessor(req.id(), req.data());
                return new ChatResponse("ÉXITO: Profesor actualizado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Elimina un profesor. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureDeleteRequest, ChatResponse> deleteProfessor() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                professorService.deleteProfessor(req.id());
                return new ChatResponse("ÉXITO: Profesor eliminado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista de todos los profesores. NO requiere confirmación.")
    public Function<ListRequest, Object> listProfessors() {
        return req -> {
            try {
                return professorService.findAllProfessors();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar profesores: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un profesor por su ID único. NO requiere confirmación.")
    public Function<SearchByIdRequest, Object> findProfessorById() {
        return req -> {
            try {
                return professorService.findById(req.id());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesor por ID: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca profesores por su nombre. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findProfessorsByName() {
        return req -> {
            try {
                return professorService.findByName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesores por nombre: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca profesores por su apellido. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findProfessorsBySurname() {
        return req -> {
            try {
                return professorService.findBySurname(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesores por apellido: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un profesor por su DNI. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findProfessorByDni() {
        return req -> {
            try {
                return professorService.findByDni(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesor por DNI: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un profesor por su correo electrónico. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findProfessorByEmail() {
        return req -> {
            try {
                return professorService.findByEmail(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesor por email: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un profesor por su número de teléfono. NO requiere confirmación.")
    public Function<SearchByIntegerRequest, Object> findProfessorByPhone() {
        return req -> {
            try {
                return professorService.findByPhone(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesor por teléfono: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Filtra profesores por su horario (MORNING o AFTERNOON). NO requiere confirmación.")
    public Function<SearchByScheduleRequest, Object> findProfessorsBySchedule() {
        return req -> {
            try {
                return professorService.findBySchedule(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesores por horario: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca profesores por su salario exacto. NO requiere confirmación.")
    public Function<SearchByDoubleRequest, Object> findProfessorsBySalary() {
        return req -> {
            try {
                return professorService.findBySalary(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesores por salario: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Lista profesores pertenecientes a un departmento por su nombre. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findProfessorsByDepartment() {
        return req -> {
            try {
                return professorService.findByDepartmentName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar profesores por departmento: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Asigna un estudiante a un profesor. Requiere confirmación.")
    public Function<AssignStudentRequest, ChatResponse> assignStudentToProfessor() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                professorService.assignStudentToProfessor(req.professorId(), req.studentId());
                return new ChatResponse("ÉXITO: Estudiante asignado al profesor.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Remueve un estudiante de un profesor. Requiere confirmación.")
    public Function<AssignStudentRequest, ChatResponse> removeStudentFromProfessor() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                professorService.removeStudentFromProfessor(req.professorId(), req.studentId());
                return new ChatResponse("ÉXITO: Estudiante removido del profesor.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }
}
