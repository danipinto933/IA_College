package DFP.collegeWithAI.config.ai;

import java.util.function.Function;
import DFP.collegeWithAI.dto.ai.AiDto.*;
import DFP.collegeWithAI.service.DepartmentService;
import DFP.collegeWithAI.service.AiValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class DepartmentAiConfig {

    private final DepartmentService departmentService;
    private final AiValidationService aiValidationService;

    public DepartmentAiConfig(DepartmentService departmentService, AiValidationService aiValidationService) {
        this.departmentService = departmentService;
        this.aiValidationService = aiValidationService;
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Crea un departmento. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureDeptRequest, ChatResponse> createDepartment() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                departmentService.addDepartment(req.data());
                return new ChatResponse("ÉXITO: Departmento creado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Actualiza un departmento. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureUpdateDeptRequest, ChatResponse> updateDepartment() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                departmentService.updateDepartment(req.id(), req.data());
                return new ChatResponse("ÉXITO: Departmento actualizado.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("SOLO LLAMAR SI EL USUARIO YA HA DICHO SÍ. Elimina un departmento. Si el usuario acaba de pedirlo, NO LLAMES a esta función, pregúntale primero.")
    public Function<SecureDeleteRequest, ChatResponse> deleteDepartment() {
        return req -> {
            if (!aiValidationService.esCitaValida(req.citaDeConfirmacionDelUsuario())) {
                return new ChatResponse(
                        "ERROR CRÍTICO: No tienes permiso del usuario aún. DETENTE INMEDIATAMENTE. No reintentes llamar a la función. Responde al usuario preguntándole si desea proceder.");
            }
            try {
                departmentService.deleteDepartment(req.id());
                return new ChatResponse("ÉXITO: Departmento eliminado correctamente.");
            } catch (Exception e) {
                return new ChatResponse("ERROR: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista completa de departmentos. NO requiere confirmación.")
    public Function<ListRequest, Object> listDepartments() {
        return req -> {
            try {
                return departmentService.findAllDepartments();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar departmentos: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Obtiene la lista completa de departmentos con todos sus atributos detallados. NO requiere confirmación.")
    public Function<ListRequest, Object> listDepartmentsFull() {
        return req -> {
            try {
                return departmentService.findAllFull();
            } catch (Exception e) {
                return new ChatResponse("ERROR al listar departmentos completos: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un departmento por su ID único. NO requiere confirmación.")
    public Function<SearchByIdRequest, Object> findDepartmentById() {
        return req -> {
            try {
                return departmentService.findById(req.id());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar departmento por ID: " + e.getMessage());
            }
        };
    }

    @Bean
    @Description("Busca un departmento por su nombre exacto. NO requiere confirmación.")
    public Function<SearchByNameRequest, Object> findDepartmentByName() {
        return req -> {
            try {
                return departmentService.findByDepartmentName(req.name());
            } catch (Exception e) {
                return new ChatResponse("ERROR al buscar departmento por nombre: " + e.getMessage());
            }
        };
    }
}
