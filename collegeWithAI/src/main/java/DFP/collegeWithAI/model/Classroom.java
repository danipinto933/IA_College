package DFP.collegeWithAI.model;

import jakarta.persistence.*;
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
@Table(name= "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @NotNull(message = "El estudiante no puede estar vacio")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @NotNull(message = "La asignatura no puede estar vacia")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    @NotNull(message = "El profesor no puede estar vacio")
    private Professor professor;

    private Double grade; // No le ponemos NotNull para que pueda matricularse sin nota inicial
}
