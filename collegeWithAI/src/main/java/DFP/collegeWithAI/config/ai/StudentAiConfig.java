package DFP.collegeWithAI.config.ai;

import java.util.function.Function;
import DFP.collegeWithAI.dto.ai.AiDto.*;
import DFP.collegeWithAI.service.StudentService;
import DFP.collegeWithAI.service.AiValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class StudentAiConfig {

    private final StudentService studentService;
    private final AiValidationService aiValidationService;

    public StudentAiConfig(StudentService studentService, AiValidationService aiValidationService) {
        this.studentService = studentService;
        this.aiValidationService = aiValidationService;
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Crea un estudiante. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureStudentRequest, ChatResponse> createStudent() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                studentService.addStudent(req.data());
                return new ChatResponse("ÉXITO: Estudiante creado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Actualiza un estudiante. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureUpdateStudentRequest, ChatResponse> updateStudent() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                studentService.updateStudent(req.id(), req.data());
                return new ChatResponse("ÉXITO: Estudiante actualizado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Elimina un estudiante. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureDeleteRequest, ChatResponse> deleteStudent() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                studentService.deleteStudent(req.id());
                return new ChatResponse("ÉXITO: Estudiante eliminado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista básica de estudiantes. NO requiere confirmación.")
    public Function<ListRequest, Object> listStudents() {
        return req -> {
            try {
                return studentService.findAllStudents();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar estudiantes: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista completa de estudiantes con todos sus atributos detallados. NO requiere confirmación.")
    public Function<ListRequest, Object> listStudentsFull() {
        return req -> {
            try {
                return studentService.findAllFull();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar estudiantes completos: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un estudiante por su ID único. NO requiere confirmación.")
    public Function<SearchByIdRequest, Object> findStudentById() {
        return req -> {
            try {
                return studentService.findById(req.id());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiante por ID: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca estudiantes por su nombre. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findStudentsByName() {
        return req -> {
            try {
                return studentService.findByName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiantes por nombre: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca estudiantes por su apellido. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findStudentsBySurname() {
        return req -> {
            try {
                return studentService.findBySurname(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiantes por apellido: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un estudiante por su DNI. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findStudentByDni() {
        return req -> {
            try {
                return studentService.findByDni(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiante por DNI: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un estudiante por su correo electrónico. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findStudentByEmail() {
        return req -> {
            try {
                return studentService.findByEmail(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiante por email: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un estudiante por su número de teléfono. NO requiere confirmación.")
    public Function<SearchByIntegerRequest, Object> findStudentByPhone() {
        return req -> {
            try {
                return studentService.findByPhone(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiante por teléfono: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Filtra estudiantes por su horario (MORNING o AFTERNOON). NO requiere confirmación.")
    public Function<SearchByScheduleRequest, Object> findStudentsBySchedule() {
        return req -> {
            try {
                return studentService.findBySchedule(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiantes por horario: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca estudiantes que ingresaron en una fecha específica o anterior. NO requiere confirmación.")
    public Function<SearchByDateRequest, Object> findStudentsByDateJoining() {
        return req -> {
            try {
                return studentService.findByDateJoining(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar estudiantes por fecha de ingreso: " + e.getMessage());
            }
        };
    }
}
