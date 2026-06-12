package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.SubjectMapper;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.repository.SubjectRepository;
import DFP.collegeWithAI.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public SubjectDto addSubject(SubjectDto subjectDto) {
        if (subjectDto == null) {
            throw new BadRequestException("Los datos de la asignatura no pueden ser nulos");
        }

        Subject subject = subjectMapper.toEntity(subjectDto);
        if (subject == null) {
            throw new BadRequestException("Materia no válida");
        }

        if (subject.getDepartment() != null && subject.getDepartment().getDepartmentName() != null) {
            Department managedDepartment = departmentRepository.findByDepartmentName(subject.getDepartment().getDepartmentName());
            if (managedDepartment == null) {
                throw new ResourceNotFoundException("Departmento no encontrado: " + subject.getDepartment().getDepartmentName());
            }
            subject.setDepartment(managedDepartment);
        }

        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(savedSubject);
    }

    @Override
    public List<SubjectDto> findAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subjectMapper::toDto)
                .toList();
    }

    @Override
    public List<Subject> findAllFull() {
        return subjectRepository.findAll();
    }

    @Override
    public SubjectDto findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con el ID: " + id));
        return subjectMapper.toDto(subject);
    }

    @Override
    public SubjectDto findByName(String name) {
        Subject subject = subjectRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con el nombre: " + name));
        return subjectMapper.toDto(subject);
    }

    @Override
    public SubjectDto findByInternCode(String internCode) {
        Subject subject = subjectRepository.findByInternCode(internCode)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con el código interno: " + internCode));
        return subjectMapper.toDto(subject);
    }

    @Override
    public List<SubjectDto> findByIsElective(Boolean isElective) {
        List<Subject> subjects = subjectRepository.findByIsElective(isElective);
        return subjects.stream().map(subjectMapper::toDto).toList();
    }

    @Override
    public List<SubjectDto> findByTotalHours(Integer totalHours) {
        List<Subject> subjects = subjectRepository.findByTotalHoursLessThanEqual(totalHours);
        return subjects.stream().map(subjectMapper::toDto).toList();
    }

    @Override
    public List<SubjectDto> findByDepartmentName(String departmentName) {
        List<Subject> subjects = subjectRepository.findByDepartment_DepartmentName(departmentName);
        return subjects.stream().map(subjectMapper::toDto).toList();
    }

    @Override
    public SubjectDto updateSubject(Long id, SubjectDto subjectDto) {
        if (subjectDto == null) {
            throw new BadRequestException("Los datos para actualizar la asignatura no pueden ser nulos");
        }

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con el ID: " + id));

        subjectMapper.updateEntityFromDto(subjectDto, subject);

        if (subject.getDepartment() != null && subject.getDepartment().getDepartmentName() != null) {
            Department managedDepartment = departmentRepository.findByDepartmentName(subject.getDepartment().getDepartmentName());
            if (managedDepartment == null) {
                throw new ResourceNotFoundException("Departmento no encontrado: " + subject.getDepartment().getDepartmentName());
            }
            subject.setDepartment(managedDepartment);
        }

        Subject updatedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(updatedSubject);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con el ID: " + id));
        subjectRepository.delete(subject);
    }
}
