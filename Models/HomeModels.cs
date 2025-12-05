namespace Demilingua.Models
{
    // Modelo para la vista completa
    public class HomeViewModel
    {
        public List<Idioma> Idiomas { get; set; }
        public List<RankingItem> Ranking { get; set; }
    }

    // Replica LanguageAdapter.java
    public class Idioma 
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string FlagCode { get; set; } // Para usar con librería de banderas css
    }
    
    // Replica RankingItem.java
    public class RankingItem
    {
        public string Usuario { get; set; }
        public string Idioma { get; set; }
        public int Puntos { get; set; }
    }

    // Histórico de puntuaciones
    public class PuntuacionItem
    {
        public int UsuarioId { get; set; }
        public int IdiomaId { get; set; }
        public int Puntos { get; set; }
        public string Fecha { get; set; }
        public string Usuario { get; set; }
        public string Idioma { get; set; }
    }
}
