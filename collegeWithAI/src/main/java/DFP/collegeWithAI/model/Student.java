package DFP.collegeWithAI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "students")
public class Student {
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

    @NotNull(message = "La fecha de ingreso no puede estar vacia")
    @Temporal(TemporalType.DATE)
    private Date dateJoining;

    @ManyToMany(mappedBy = "students")
    private java.util.List<Professor> professors;

    @ManyToMany
    @JoinTable(
        name = "student_subject",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private java.util.List<Subject> subjects;
}
