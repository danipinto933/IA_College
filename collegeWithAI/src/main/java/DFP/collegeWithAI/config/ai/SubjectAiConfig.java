package DFP.collegeWithAI.config.ai;

import java.util.function.Function;
import DFP.collegeWithAI.dto.ai.AiDto.*;
import DFP.collegeWithAI.service.SubjectService;
import DFP.collegeWithAI.service.AiValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class SubjectAiConfig {

    private final SubjectService subjectService;
    private final AiValidationService aiValidationService;

    public SubjectAiConfig(SubjectService subjectService, AiValidationService aiValidationService) {
        this.subjectService = subjectService;
        this.aiValidationService = aiValidationService;
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Crea una asignatura. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureSubjectRequest, ChatResponse> createSubject() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                subjectService.addSubject(req.data());
                return new ChatResponse("ÉXITO: Asignatura creada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Actualiza una asignatura. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureUpdateSubjectRequest, ChatResponse> updateSubject() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                subjectService.updateSubject(req.id(), req.data());
                return new ChatResponse("ÉXITO: Asignatura actualizada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Elimina una asignatura. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureDeleteRequest, ChatResponse> deleteSubject() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                subjectService.deleteSubject(req.id());
                return new ChatResponse("ÉXITO: Asignatura eliminada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista básica de asignaturas. NO requiere confirmación.")
    public Function<ListRequest, Object> listSubjects() {
        return req -> {
            try {
                return subjectService.findAllSubjects();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar asignaturas: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista completa de asignaturas con todos sus atributos detallados. NO requiere confirmación.")
    public Function<ListRequest, Object> listSubjectsFull() {
        return req -> {
            try {
                return subjectService.findAllFull();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar asignaturas completas: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca una asignatura por su ID único. NO requiere confirmación.")
    public Function<SearchByIdRequest, Object> findSubjectById() {
        return req -> {
            try {
                return subjectService.findById(req.id());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar asignatura por ID: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca una asignatura por su nombre. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findSubjectByName() {
        return req -> {
            try {
                return subjectService.findByName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar asignatura por nombre: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca una asignatura por su código interno. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findSubjectByInternCode() {
        return req -> {
            try {
                return subjectService.findByInternCode(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar asignatura por código interno: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Filtra asignaturas según si son electivas o no. NO requiere confirmación.")
    public Function<SearchByBooleanRequest, Object> findSubjectsByIsElective() {
        return req -> {
            try {
                return subjectService.findByIsElective(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al filtrar asignaturas por electividad: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca asignaturas que tengan un número de horas igual o menor al indicado. NO requiere confirmación.")
    public Function<SearchByIntegerRequest, Object> findSubjectsByTotalHours() {
        return req -> {
            try {
                return subjectService.findByTotalHours(req.value());
            } catch (Exception e) {
                return new ChatResponse("ERROR al filtrar asignaturas por horas: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Lista todas las asignaturas que pertenecen a un departmento específico por su nombre. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findSubjectsByDepartment() {
        return req -> {
            try {
                return subjectService.findByDepartmentName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al filtrar asignaturas por departmento: " + e.getMessage());
            }
        };
    }
}
