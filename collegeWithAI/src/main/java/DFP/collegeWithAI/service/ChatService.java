package DFP.collegeWithAI.service;

import java.util.stream.Collectors;
import DFP.collegeWithAI.repository.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final ChatClient chatClient;

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;
    private final ClassroomRepository classroomRepository;

    public ChatService(ChatClient.Builder builder,
            DepartmentRepository departmentRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository,
            ProfessorRepository professorRepository,
            ClassroomRepository classroomRepository) {

        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.professorRepository = professorRepository;
        this.classroomRepository = classroomRepository;

        this.chatClient = builder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultFunctions(
                        "createDepartment",
                        "updateDepartment",
                        "deleteDepartment",
                        "listDepartments",
                        "listDepartmentsFull",
                        "findDepartmentById",
                        "findDepartmentByName",
                        "createStudent",
                        "updateStudent",
                        "deleteStudent",
                        "listStudents",
                        "listStudentsFull",
                        "findStudentById",
                        "findStudentsByName",
                        "findStudentsBySurname",
                        "findStudentByDni",
                        "findStudentByEmail",
                        "findStudentByPhone",
                        "findStudentsBySchedule",
                        "findStudentsByDateJoining",
                        "createSubject",
                        "updateSubject",
                        "deleteSubject",
                        "listSubjects",
                        "listSubjectsFull",
                        "findSubjectById",
                        "findSubjectByName",
                        "findSubjectByInternCode",
                        "findSubjectsByIsElective",
                        "findSubjectsByTotalHours",
                        "findSubjectsByDepartment",
                        "createProfessor",
                        "updateProfessor",
                        "deleteProfessor",
                        "listProfessors",
                        "findProfessorById",
                        "findProfessorsByName",
                        "findProfessorsBySurname",
                        "findProfessorByDni",
                        "findProfessorByEmail",
                        "findProfessorByPhone",
                        "findProfessorsBySchedule",
                        "findProfessorsBySalary",
                        "findProfessorsByDepartment",
                        "assignStudentToProfessor",
                        "removeStudentFromProfessor",
                        "createClassroom",
                        "listClassrooms",
                        "findClassroomById",
                        "findClassroomsByStudentEmail",
                        "findClassroomsBySubjectCode",
                        "findClassroomsByProfessorEmail",
                        "updateClassroomGrade",
                        "deleteClassroom"
                )
                .defaultSystem(
                        """
                                Eres el asistente administrativo de CollegeWithAI.

                                PROTOCOLO OBLIGATORIO DE SEGURIDAD (2 PASOS) PARA ACCIONES DE ESCRITURA:

                                PASO 1: SOLICITUD (El usuario pide CREAR, ACTUALIZAR o ELIMINAR).
                                - Acción: NO llames a ninguna función de escritura.
                                - Respuesta: Pregunta explícitamente "¿Deseas que [realice la acción]? Por favor, confirma con un 'Sí'".

                                PASO 2: CONFIRMACIÓN (El usuario responde al Paso 1).
                                - Acción: Solo si el usuario confirma (dice "Sí", "OK", "Adelante"), llama a la función correspondiente de escritura.
                                - Cita: Usa ÚNICAMENTE las palabras del usuario en este PASO 2 para el campo 'citaDeConfirmacionDelUsuario'.

                                REGLAS CRÍTICAS:
                                - Tienes PROHIBIDO usar el mensaje del PASO 1 (la solicitud inicial) como cita de confirmación.
                                - Si llamas a una función de escritura en el mismo turno en que el usuario hizo la solicitud inicial, estarás cometiendo una violación de seguridad.
                                - Si recibes un error de "Falta cita", no intentes inventarla; detente y pregunta al usuario.
                                - Para LEER o LISTAR datos (ej. ver departmentos), llama a la función de lectura correspondiente directamente (como listDepartments) y responde sin pedir permiso.
                                """)
                .build();
    }

    public String consulta(String mensaje) {
        StringBuilder sb = new StringBuilder("ÍNDICE RÁPIDO DE ENTIDADES EN BD:\n");
        sb.append("- Departmentos (ID:Nombre): ").append(departmentRepository.findAll().stream()
                .map(d -> d.getId() + ":" + d.getDepartmentName()).collect(Collectors.joining(", "))).append("\n");
        sb.append("- Estudiantes (ID:Nombre): ").append(studentRepository.findAll().stream()
                .map(s -> s.getId() + ":" + s.getName()).collect(Collectors.joining(", "))).append("\n");
        sb.append("- Asignaturas (ID:Nombre): ").append(subjectRepository.findAll().stream()
                .map(s -> s.getId() + ":" + s.getName()).collect(Collectors.joining(", "))).append("\n");
        sb.append("- Profesores (ID:Nombre): ").append(professorRepository.findAll().stream()
                .map(p -> p.getId() + ":" + p.getName()).collect(Collectors.joining(", "))).append("\n");
        sb.append("- Clases (ID:Materia): ")
                .append(classroomRepository.findAll().stream()
                        .map(c -> c.getId() + ":" + (c.getSubject() != null ? c.getSubject().getName() : "N/A"))
                        .collect(Collectors.joining(", ")))
                .append("\n");

        try {
            return chatClient.prompt()
                    .advisors(a -> a.param("chat_memory_conversation_id", "default-session"))
                    .system(s -> s.text(sb.toString()))
                    .user(mensaje)
                    .call()
                    .content();
        } catch (Exception e) {
            logger.error("Error al consultar la IA: {}", e.getMessage());
            return "Lo siento, hubo un error al procesar tu solicitud.";
        }
    }
}