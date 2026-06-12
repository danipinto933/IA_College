export const CATEGORIES = [
  { 
    name: 'Estudiantes', 
    basePath: 'students', 
    icon: '🎓',
    methods: [
      { label: 'Todos', path: '', needsParam: false },
      { label: 'Completos', path: '/full', needsParam: false },
      { label: 'Por ID', path: '/', needsParam: true, paramName: 'ID' },
      { label: 'Por Nombre', path: '/name/', needsParam: true, paramName: 'Nombre' },
      { label: 'Por Apellido', path: '/surname/', needsParam: true, paramName: 'Apellido' },
      { label: 'Por DNI', path: '/dni/', needsParam: true, paramName: 'DNI' },
      { label: 'Por Email', path: '/email/', needsParam: true, paramName: 'Email' },
      { label: 'Por Teléfono', path: '/phone/', needsParam: true, paramName: 'Teléfono' },
      { label: 'Por Turno', path: '/schedule/', needsParam: true, paramName: 'Turno (MORNING/AFTERNOON)' },
      { label: 'Por Fecha Ingreso', path: '/dateJoining/', needsParam: true, paramName: 'Fecha (yyyy-MM-dd)' }
    ]
  },
  { 
    name: 'Profesores', 
    basePath: 'professors', 
    icon: '👨‍🏫',
    methods: [
      { label: 'Todos', path: '', needsParam: false },
      { label: 'Por ID', path: '/', needsParam: true, paramName: 'ID' },
      { label: 'Por Nombre', path: '/name/', needsParam: true, paramName: 'Nombre' },
      { label: 'Por Apellido', path: '/surname/', needsParam: true, paramName: 'Apellido' },
      { label: 'Por DNI', path: '/dni/', needsParam: true, paramName: 'DNI' },
      { label: 'Por Email', path: '/email/', needsParam: true, paramName: 'Email' },
      { label: 'Por Teléfono', path: '/phone/', needsParam: true, paramName: 'Teléfono' },
      { label: 'Por Turno', path: '/schedule/', needsParam: true, paramName: 'Turno' },
      { label: 'Por Salario', path: '/salary/', needsParam: true, paramName: 'Salario' },
      { label: 'Por Departamento', path: '/department/', needsParam: true, paramName: 'Nombre del Departamento' }
    ]
  },
  { 
    name: 'Departamentos', 
    basePath: 'departments', 
    icon: '🏢',
    methods: [
      { label: 'Todos', path: '', needsParam: false },
      { label: 'Completos', path: '/full', needsParam: false },
      { label: 'Por ID', path: '/', needsParam: true, paramName: 'ID' },
      { label: 'Por Nombre', path: '/name/', needsParam: true, paramName: 'Nombre' }
    ]
  },
  { 
    name: 'Asignaturas', 
    basePath: 'subjects', 
    icon: '📚',
    methods: [
      { label: 'Todas', path: '', needsParam: false },
      { label: 'Completas', path: '/full', needsParam: false },
      { label: 'Por ID', path: '/', needsParam: true, paramName: 'ID' },
      { label: 'Por Nombre', path: '/name/', needsParam: true, paramName: 'Nombre' },
      { label: 'Por Código Interno', path: '/internCode/', needsParam: true, paramName: 'Código Interno' },
      { label: 'Por Electiva', path: '/isElective/', needsParam: true, paramName: 'Es Electiva (true/false)' },
      { label: 'Por Horas Totales', path: '/totalHours/', needsParam: true, paramName: 'Horas Totales' },
      { label: 'Por Departamento', path: '/department/', needsParam: true, paramName: 'Nombre del Departamento' }
    ]
  },
  { 
    name: 'Aulas', 
    basePath: 'classrooms', 
    icon: '🏫',
    methods: [
      { label: 'Todas', path: '', needsParam: false },
      { label: 'Por ID', path: '/', needsParam: true, paramName: 'ID' },
      { label: 'Por Email Estudiante', path: '/student/', needsParam: true, paramName: 'Email del Estudiante' },
      { label: 'Por Código Asignatura', path: '/subject/', needsParam: true, paramName: 'Código de Asignatura' },
      { label: 'Por Email Profesor', path: '/professor/', needsParam: true, paramName: 'Email del Profesor' }
    ]
  }
];
