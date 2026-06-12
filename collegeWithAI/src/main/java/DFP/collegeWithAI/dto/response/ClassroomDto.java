package DFP.collegeWithAI.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDto {

    @NotNull(message = "El estudiante no puede estar vacio")
    private StudentDto student;

    @NotNull(message = "La asignatura no puede estar vacia")
    private SubjectDto subject;

    @NotNull(message = "El profesor no puede estar vacio")
    private ProfessorDto professor;

    private Double grade;
}
