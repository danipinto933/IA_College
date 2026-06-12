package DFP.collegeWithAI.dto.ai;

import DFP.collegeWithAI.dto.response.*;
import DFP.collegeWithAI.model.Schedule;

public final class AiDto {

    private AiDto() {
        // Prevent instantiation
    }

    public record SecureDeptRequest(DepartmentDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureUpdateDeptRequest(Long id, DepartmentDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureStudentRequest(StudentDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureUpdateStudentRequest(Long id, StudentDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureSubjectRequest(SubjectDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureUpdateSubjectRequest(Long id, SubjectDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureProfessorRequest(ProfessorDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureUpdateProfessorRequest(Long id, ProfessorDto data, String citaDeConfirmacionDelUsuario) {
    }

    public record SecureClassroomRequest(
            String studentEmail,
            String professorEmail,
            String subjectInternCode,
            Double grade,
            String citaDeConfirmacionDelUsuario
    ) {
    }

    public record SecureDeleteRequest(Long id, String citaDeConfirmacionDelUsuario) {
    }

    public record ListRequest(
            @com.fasterxml.jackson.annotation.JsonPropertyDescription("Campo ignorado, no es necesario enviar nada aquí") String dummy
    ) {
    }

    public record SearchByIdRequest(Long id) {
    }

    public record SearchByNameRequest(String name) {
    }

    public record SearchByCodeRequest(String internCode) {
    }

    public record SearchByBooleanRequest(Boolean value) {
    }

    public record SearchByIntegerRequest(Integer value) {
    }

    public record SearchByDoubleRequest(Double value) {
    }

    public record SearchByDateRequest(java.util.Date value) {
    }

    public record SearchByScheduleRequest(Schedule value) {
    }

    public record AssignStudentRequest(Long professorId, Long studentId, String citaDeConfirmacionDelUsuario) {
    }

    public record UpdateGradeRequest(Long id, Double grade, String citaDeConfirmacionDelUsuario) {
    }

    public record ChatResponse(String resultado) {
    }
}
