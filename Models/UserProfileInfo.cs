namespace Demilingua.Models
{
    public class UserProfileInfo
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string Correo { get; set; }
        public int Vidas { get; set; }
        public int RachaActual { get; set; }
        public int DivisionId { get; set; }
        public int TotalXp { get; set; }
    }
}
