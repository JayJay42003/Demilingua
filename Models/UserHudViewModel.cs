namespace Demilingua.Models
{
    public class UserHudViewModel
    {
        public int Vidas { get; set; }
        public int RachaActual { get; set; }
        public int TotalXp { get; set; }
        public int DivisionId { get; set; }
        public string DivisionNombre => DivisionId switch
        {
            1 => "Bronce",
            2 => "Plata",
            3 => "Oro",
            4 => "Platino",
            5 => "Diamante",
            _ => "-"
        };
    }
}
