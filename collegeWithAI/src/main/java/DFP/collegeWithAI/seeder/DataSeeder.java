package DFP.collegeWithAI.seeder;

import DFP.collegeWithAI.model.*;
import DFP.collegeWithAI.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final ClassroomRepository classroomRepository;

    public DataSeeder(DepartmentRepository departmentRepository,
            SubjectRepository subjectRepository,
            StudentRepository studentRepository,
            ProfessorRepository professorRepository,
            ClassroomRepository classroomRepository) {
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedDepartments();
        seedSubjects();
        seedStudents();
        seedProfessors();
        seedClassrooms();
    }

    private void seedDepartments() {
        List<String> names = Arrays.asList(
                "Matemáticas",
                "Física",
                "Química",
                "Filosofía",
                "Lengua Española",
                "Informática",
                "Economía");

        for (String name : names) {
            if (departmentRepository.findByDepartmentName(name) == null) {
                Department department = Department.builder()
                        .departmentName(name)
                        .build();
                departmentRepository.save(department);
            }
        }
    }

    private void seedSubjects() {
        Department mat = departmentRepository.findByDepartmentName("Matemáticas");
        Department fis = departmentRepository.findByDepartmentName("Física");
        Department qui = departmentRepository.findByDepartmentName("Química");
        Department fil = departmentRepository.findByDepartmentName("Filosofía");
        Department eco = departmentRepository.findByDepartmentName("Economía");
        Department inf = departmentRepository.findByDepartmentName("Informática");

        createSubject("Cálculo I", "MAT001", "Fundamentos de cálculo diferencial e integral", 60, false, mat);
        createSubject("Álgebra Lineal", "MAT002", "Estudio de vectores, matrices y sistemas lineales", 60, true, mat);

        createSubject("Física Mecánica", "FIS001", "Leyes de Newton y cinemática", 60, false, fis);
        createSubject("Termodinámica", "FIS002", "Estudio del calor y la energía", 60, true, fis);

        createSubject("Química Orgánica", "QUI001", "Estudio de compuestos de carbono", 60, false, qui);
        createSubject("Química Inorgánica", "QUI002", "Estudio de elementos y compuestos minerales", 60, true, qui);

        createSubject("Introducción a la Filosofía", "FIL001", "Historia del pensamiento occidental", 45, false, fil);
        createSubject("Ética", "FIL002", "Estudio de la moral y la conducta humana", 45, true, fil);

        createSubject("Macroeconomía", "ECO001", "Estudio de la economía a nivel global", 60, false, eco);
        createSubject("Microeconomía", "ECO002", "Estudio del comportamiento de agentes económicos", 60, true, eco);

        createSubject("Programación I", "INF001", "Fundamentos de programación en Java", 80, false, inf);
        createSubject("Estructuras de Datos", "INF002", "Organización y gestión de datos", 80, true, inf);
    }

    private void seedStudents() {
        // Asignaturas
        Subject mat1 = subjectRepository.findByInternCode("MAT001").orElse(null);
        Subject mat2 = subjectRepository.findByInternCode("MAT002").orElse(null);
        Subject fis1 = subjectRepository.findByInternCode("FIS001").orElse(null);
        Subject fis2 = subjectRepository.findByInternCode("FIS002").orElse(null);
        Subject qui1 = subjectRepository.findByInternCode("QUI001").orElse(null);
        Subject fil1 = subjectRepository.findByInternCode("FIL001").orElse(null);
        Subject inf1 = subjectRepository.findByInternCode("INF001").orElse(null);

        String[] names = { "Juan", "Maria", "Pedro", "Lucia", "Luis", "Elena", "Mario", "Sofia", "Diego", "Paula" };

        // 5 estudiantes en asignaturas distintas
        Subject[] diffSubjects = { mat1, mat2, fis1, fis2, qui1 };
        for (int i = 0; i < 5; i++) {
            createStudent(names[i], "Sánchez", "DNI" + i, names[i].toLowerCase() + "@student.com", 600000066 + i,
                    Schedule.MORNING, createDate(2024, i + 1), List.of(diffSubjects[i]));
        }

        // 2 estudiantes en la misma asignatura (Filosofía)
        for (int i = 5; i < 7; i++) {
            createStudent(names[i], "Gómez", "DNI" + (100 + i), names[i].toLowerCase() + "@student.com", 700000077 + i,
                    Schedule.AFTERNOON, createDate(2024, i + 1), List.of(fil1));
        }

        // 3 estudiantes en la misma asignatura (Informática)
        for (int i = 7; i < 10; i++) {
            createStudent(names[i], "Pérez", "DNI" + (200 + i), names[i].toLowerCase() + "@student.com", 800000088 + i,
                    Schedule.MORNING, createDate(2024, i + 1), List.of(inf1));
        }
    }

    private void seedProfessors() {
        createProfessor("Antonio", "García", "PROFDNI01", "antonio.garcia@college.edu", 611223344, Schedule.MORNING,
                30000.0, "Matemáticas");
        createProfessor("Beatriz", "López", "PROFDNI02", "beatriz.lopez@college.edu", 611223345, Schedule.AFTERNOON,
                31000.0, "Matemáticas");

        createProfessor("Carmen", "Ruiz", "PROFDNI03", "carmen.ruiz@college.edu", 622334455, Schedule.MORNING, 30000.0,
                "Física");
        createProfessor("David", "Sánchez", "PROFDNI04", "david.sanchez@college.edu", 622334456, Schedule.AFTERNOON,
                31000.0, "Física");

        createProfessor("Elena", "Martín", "PROFDNI05", "elena.martin@college.edu", 633445566, Schedule.MORNING,
                30000.0, "Química");
        createProfessor("Francisco", "Jiménez", "PROFDNI06", "francisco.jimenez@college.edu", 633445567,
                Schedule.AFTERNOON, 31000.0, "Química");

        createProfessor("Gloria", "Ortiz", "PROFDNI07", "gloria.ortiz@college.edu", 644556677, Schedule.MORNING,
                29000.0, "Filosofía");
        createProfessor("Héctor", "Castro", "PROFDNI08", "hector.castro@college.edu", 644556678, Schedule.AFTERNOON,
                29500.0, "Filosofía");

        createProfessor("Isabel", "Blanco", "PROFDNI09", "isabel.blanco@college.edu", 655667788, Schedule.MORNING,
                30500.0, "Lengua Española");
        createProfessor("Javier", "Torres", "PROFDNI10", "javier.torres@college.edu", 655667789, Schedule.AFTERNOON,
                31500.0, "Lengua Española");

        createProfessor("Lucía", "Navarro", "PROFDNI11", "lucia.navarro@college.edu", 666778899, Schedule.MORNING,
                32000.0, "Informática");
        createProfessor("Manuel", "Vidal", "PROFDNI12", "manuel.vidal@college.edu", 666778890, Schedule.AFTERNOON,
                33000.0, "Informática");

        createProfessor("Nuria", "Serrano", "PROFDNI13", "nuria.serrano@college.edu", 677889900, Schedule.MORNING,
                30000.0, "Economía");
        createProfessor("Oscar", "Medina", "PROFDNI14", "oscar.medina@college.edu", 677889901, Schedule.AFTERNOON,
                30500.0, "Economía");
    }

    private void seedClassrooms() {
        // Alumnos
        Student juan = studentRepository.findByEmail("juan@student.com").orElse(null);
        Student maria = studentRepository.findByEmail("maria@student.com").orElse(null);
        Student pedro = studentRepository.findByEmail("pedro@student.com").orElse(null);
        Student lucia_s = studentRepository.findByEmail("lucia@student.com").orElse(null);
        Student luis = studentRepository.findByEmail("luis@student.com").orElse(null);
        Student elena_s = studentRepository.findByEmail("elena@student.com").orElse(null);
        Student mario = studentRepository.findByEmail("mario@student.com").orElse(null);
        Student sofia = studentRepository.findByEmail("sofia@student.com").orElse(null);

        // Profesores
        Professor antonio = professorRepository.findByEmail("antonio.garcia@college.edu").orElse(null);
        Professor beatriz = professorRepository.findByEmail("beatriz.lopez@college.edu").orElse(null);
        Professor carmen = professorRepository.findByEmail("carmen.ruiz@college.edu").orElse(null);
        Professor david = professorRepository.findByEmail("david.sanchez@college.edu").orElse(null);
        Professor elena_p = professorRepository.findByEmail("elena.martin@college.edu").orElse(null);
        Professor francisco = professorRepository.findByEmail("francisco.jimenez@college.edu").orElse(null);
        Professor gloria = professorRepository.findByEmail("gloria.ortiz@college.edu").orElse(null);
        Professor lucia_p = professorRepository.findByEmail("lucia.navarro@college.edu").orElse(null);

        // Asignaturas
        Subject mat1 = subjectRepository.findByInternCode("MAT001").orElse(null);
        Subject mat2 = subjectRepository.findByInternCode("MAT002").orElse(null);
        Subject fis1 = subjectRepository.findByInternCode("FIS001").orElse(null);
        Subject fis2 = subjectRepository.findByInternCode("FIS002").orElse(null);
        Subject qui1 = subjectRepository.findByInternCode("QUI001").orElse(null);
        Subject qui2 = subjectRepository.findByInternCode("QUI002").orElse(null);
        Subject fil1 = subjectRepository.findByInternCode("FIL001").orElse(null);
        Subject inf1 = subjectRepository.findByInternCode("INF001").orElse(null);

        createClassroom(juan, mat1, antonio, 8.5);
        createClassroom(maria, mat2, beatriz, 9.0);
        createClassroom(pedro, fis1, carmen, 7.5);
        createClassroom(lucia_s, fis2, david, 8.0);
        createClassroom(luis, qui1, elena_p, 6.5);
        createClassroom(elena_s, qui2, francisco, 7.0);
        createClassroom(mario, fil1, gloria, 9.5);
        createClassroom(sofia, inf1, lucia_p, 10.0);
    }

    private void createClassroom(Student student, Subject subject, Professor professor, Double grade) {
        if (student != null && subject != null && professor != null) {
            Classroom classroom = Classroom.builder()
                    .student(student)
                    .subject(subject)
                    .professor(professor)
                    .grade(grade)
                    .build();
            classroomRepository.save(classroom);
        }
    }

    private Date createDate(int year, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.OCTOBER, day, 10, 0, 0);
        return cal.getTime();
    }

    private Student createStudent(String name, String surname, String dni, String email, int phone, Schedule schedule,
            Date joiningDate, List<Subject> subjects) {
        return studentRepository.findByEmail(email).orElseGet(() -> {
            Student s = Student.builder()
                    .name(name).surname(surname).dni(dni).email(email)
                    .phone(phone).schedule(schedule).dateJoining(joiningDate)
                    .subjects(subjects)
                    .build();
            return studentRepository.save(s);
        });
    }

    private void createProfessor(String name, String surname, String dni, String email, int phone, Schedule schedule,
            double salary, String deptName) {
        if (professorRepository.findByEmail(email).isEmpty()) {
            Department dept = departmentRepository.findByDepartmentName(deptName);
            if (dept != null) {
                Professor professor = Professor.builder()
                        .name(name)
                        .surname(surname)
                        .dni(dni)
                        .email(email)
                        .phone(phone)
                        .schedule(schedule)
                        .salary(salary)
                        .department(dept)
                        .build();
                professorRepository.save(professor);
            }
        }
    }

    private void createSubject(String name, String code, String desc, int hours, boolean isElective, Department dept) {
        if (subjectRepository.findByInternCode(code).isEmpty()) {
            if (dept != null) {
                Subject subject = Subject.builder()
                        .name(name)
                        .internCode(code)
                        .description(desc)
                        .totalHours(hours)
                        .isElective(isElective)
                        .department(dept)
                        .build();
                subjectRepository.save(subject);
            }
        }
    }
}
