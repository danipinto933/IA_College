package DFP.collegeWithAI.dto.response;

import DFP.collegeWithAI.model.Schedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacio")
    private String surname;

    @NotBlank(message = "El DNI no puede estar vacio")
    private String dni;

    @NotBlank(message = "El email no puede estar vacio")
    private String email;

    @NotNull(message = "El telefono no puede estar vacio")
    private Integer phone;

    @NotNull(message = "El horario no puede estar vacio")
    private Schedule schedule;

    @NotNull(message = "El salario no puede estar vacio")
    private Double salary;

    @NotBlank(message = "El departmento no puede estar vacio")
    private String departmentName;
}
