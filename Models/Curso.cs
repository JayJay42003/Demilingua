namespace Demilingua.Models
{
    public class Curso
    {
        public int Id { get; set; }
        public int IdiomaId { get; set; }
        public string Nombre { get; set; }      // tvNombre
        public string Descripcion { get; set; } // tvDescripcion
        public string Dificultad { get; set; }  // tvDificultad
    }
}
