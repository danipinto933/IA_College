package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.ProfessorMapper;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.repository.ProfessorRepository;
import DFP.collegeWithAI.repository.DepartmentRepository;
import DFP.collegeWithAI.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfessorMapper professorMapper;
    private final StudentRepository studentRepository;

    @Override
    public Professor addProfessor(ProfessorDto professorDto) {
        if (professorDto == null) {
            throw new BadRequestException("Los datos del profesor no pueden ser nulos");
        }

        Professor professor = professorMapper.toEntity(professorDto);
        if (professor == null) {
            throw new BadRequestException("Profesor no válido");
        }

        if (professor.getDepartment() != null && professor.getDepartment().getDepartmentName() != null) {
            Department managedDepartment = departmentRepository.findByDepartmentName(professor.getDepartment().getDepartmentName());
            if (managedDepartment == null) {
                throw new ResourceNotFoundException("Departmento no encontrado: " + professor.getDepartment().getDepartmentName());
            }
            professor.setDepartment(managedDepartment);
        }

        return professorRepository.save(professor);
    }

    @Override
    public List<ProfessorDto> findAllProfessors() {
        List<Professor> professors = professorRepository.findAll();
        return professors.stream()
                .map(professorMapper::toDto)
                .toList();
    }

    @Override
    public ProfessorDto findById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el ID: " + id));
        return professorMapper.toDto(professor);
    }

    @Override
    public List<ProfessorDto> findByName(String name) {
        List<Professor> professors = professorRepository.findByName(name);
        return professors.stream().map(professorMapper::toDto).toList();
    }

    @Override
    public List<ProfessorDto> findBySurname(String surname) {
        List<Professor> professors = professorRepository.findBySurname(surname);
        return professors.stream().map(professorMapper::toDto).toList();
    }

    @Override
    public ProfessorDto findByDni(String dni) {
        Professor professor = professorRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el DNI: " + dni));
        return professorMapper.toDto(professor);
    }

    @Override
    public ProfessorDto findByEmail(String email) {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el email: " + email));
        return professorMapper.toDto(professor);
    }

    @Override
    public ProfessorDto findByPhone(Integer phone) {
        Professor professor = professorRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el teléfono: " + phone));
        return professorMapper.toDto(professor);
    }

    @Override
    public List<ProfessorDto> findBySchedule(Schedule schedule) {
        List<Professor> professors = professorRepository.findBySchedule(schedule);
        return professors.stream().map(professorMapper::toDto).toList();
    }

    @Override
    public List<ProfessorDto> findBySalary(Double salary) {
        List<Professor> professors = professorRepository.findBySalary(salary);
        return professors.stream().map(professorMapper::toDto).toList();
    }

    @Override
    public List<ProfessorDto> findByDepartmentName(String departmentName) {
        List<Professor> professors = professorRepository.findByDepartment_DepartmentName(departmentName);
        return professors.stream().map(professorMapper::toDto).toList();
    }

    @Override
    public ProfessorDto updateProfessor(Long id, ProfessorDto professorDto) {
        if (professorDto == null) {
            throw new BadRequestException("Los datos para actualizar el profesor no pueden ser nulos");
        }

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el ID: " + id));

        professorMapper.updateEntityFromDto(professorDto, professor);

        if (professor.getDepartment() != null && professor.getDepartment().getDepartmentName() != null) {
            Department managedDepartment = departmentRepository.findByDepartmentName(professor.getDepartment().getDepartmentName());
            if (managedDepartment == null) {
                throw new ResourceNotFoundException("Departmento no encontrado: " + professor.getDepartment().getDepartmentName());
            }
            professor.setDepartment(managedDepartment);
        }

        Professor updatedProfessor = professorRepository.save(professor);
        return professorMapper.toDto(updatedProfessor);
    }

    @Override
    public void deleteProfessor(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el ID: " + id));
        professorRepository.delete(professor);
    }

    @Override
    public ProfessorDto assignStudentToProfessor(Long professorId, Long studentId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el ID: " + professorId));
        
        DFP.collegeWithAI.model.Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el ID: " + studentId));

        if (professor.getStudents() == null) {
            professor.setStudents(new java.util.ArrayList<>());
        }
        
        if (!professor.getStudents().contains(student)) {
            professor.getStudents().add(student);
            professorRepository.save(professor);
        }
        
        return professorMapper.toDto(professor);
    }

    @Override
    public ProfessorDto removeStudentFromProfessor(Long professorId, Long studentId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el ID: " + professorId));
        
        DFP.collegeWithAI.model.Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el ID: " + studentId));

        if (professor.getStudents() != null && professor.getStudents().contains(student)) {
            professor.getStudents().remove(student);
            professorRepository.save(professor);
        }
        
        return professorMapper.toDto(professor);
    }
}
