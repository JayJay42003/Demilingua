namespace Demilingua.Models
{
    // Para pasar a la vista inicialmente
    public class TestSessionViewModel
    {
        public int CursoId { get; set; }
        public int IdiomaId { get; set; }
        public int UserId { get; set; }
    }

    // Replica la estructura JSON que devuelve /api/exercises
    public class EjercicioDto
    {
        public string ejercicioId { get; set; }
        public string tipo { get; set; }
        public string puntos { get; set; }
        public string contenido { get; set; } // Pregunta
        public string respuesta { get; set; } // Respuesta correcta
        public string opciones { get; set; }  // JSON string ["a","b"]
    }
}
