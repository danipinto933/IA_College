package DFP.collegeWithAI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull(message = "El departmento no puede estar vacio")
    private Department department;
}
