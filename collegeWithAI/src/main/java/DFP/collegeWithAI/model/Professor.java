package DFP.collegeWithAI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name= "professors")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El horario no puede estar vacio")
    private Schedule schedule;

    @NotNull(message = "El salario no puede estar vacio")
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull(message = "El departmento no puede estar vacio")
    private Department department;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "professor_student",
        joinColumns = @JoinColumn(name = "professor_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private java.util.List<Student> students;
}
