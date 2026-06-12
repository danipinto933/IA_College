package DFP.collegeWithAI.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto
{
    @NotBlank(message = "Este campo no puede estar vacio")
    private String departmentName;
}
