package DFP.collegeWithAI.dto.response;

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
public class SubjectDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

    @NotBlank(message = "El codigo interno no puede estar vacio")
    private String internCode;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;

    @NotNull(message = "isElective no puede estar vacio")
    private Boolean isElective;

    @NotNull(message = "El total de horas no puede estar vacio")
    private Integer totalHours;

    @NotBlank(message = "El departmento no puede estar vacio")
    private String departmentName;
}
