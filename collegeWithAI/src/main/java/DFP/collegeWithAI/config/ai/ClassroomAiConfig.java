package DFP.collegeWithAI.config.ai;

import java.util.function.Function;
import DFP.collegeWithAI.dto.ai.AiDto.*;
import DFP.collegeWithAI.service.ClassroomService;
import DFP.collegeWithAI.service.AiValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ClassroomAiConfig {

    private final ClassroomService classroomService;
    private final AiValidationService aiValidationService;

    public ClassroomAiConfig(ClassroomService classroomService, AiValidationService aiValidationService) {
        this.classroomService = classroomService;
        this.aiValidationService = aiValidationService;
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Crea una matrícula/clase vinculando estudiante, profesor y asignatura. Requiere confirmación.")
    public Function<SecureClassroomRequest, ChatResponse> createClassroom() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                classroomService.addClassroom(req.studentEmail(), req.professorEmail(), req.subjectInternCode(), req.grade());
                return new ChatResponse("ÉXITO: Clase/Matrícula creada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Lista todas las matrículas/clases existentes. NO requiere confirmación.")
    public Function<ListRequest, Object> listClassrooms() {
        return req -> {
            try {
                return classroomService.findAllClassrooms();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar clases: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca una matrícula/clase por su ID. NO requiere confirmación.")
    public Function<SearchByIdRequest, Object> findClassroomById() {
        return req -> {
            try {
                return classroomService.findById(req.id());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar clase por ID: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca matrículas/clases de un estudiante por su correo electrónico. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findClassroomsByStudentEmail() {
        return req -> {
            try {
                return classroomService.findByStudentEmail(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar clases por correo del estudiante: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca matrículas/clases de una asignatura por su código interno. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findClassroomsBySubjectCode() {
        return req -> {
            try {
                return classroomService.findBySubjectInternCode(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar clases por código de asignatura: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca matrículas/clases de un profesor por su correo electrónico. NO requiere confirmación.")
    public Function<SearchByCodeRequest, Object> findClassroomsByProfessorEmail() {
        return req -> {
            try {
                return classroomService.findByProfessorEmail(req.internCode());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar clases por correo del profesor: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Actualiza la nota de una matrícula/clase. Requiere confirmación.")
    public Function<UpdateGradeRequest, ChatResponse> updateClassroomGrade() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                classroomService.updateGrade(req.id(), req.grade());
                return new ChatResponse("ÉXITO: Nota actualizada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Elimina una matrícula/clase. Requiere confirmación.")
    public Function<SecureDeleteRequest, ChatResponse> deleteClassroom() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                classroomService.deleteClassroom(req.id());
                return new ChatResponse("ÉXITO: Clase/Matrícula eliminada.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }
}
