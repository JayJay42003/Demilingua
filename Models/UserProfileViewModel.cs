using System.ComponentModel.DataAnnotations;
using System.Collections.Generic;

namespace Demilingua.Models
{
    public class UserProfileViewModel
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "El nombre es obligatorio")]
        public string Nombre { get; set; } // tvName / etNombre

        [Required(ErrorMessage = "El correo es obligatorio")]
        [EmailAddress]
        public string Correo { get; set; } // tvEmail / etEmail

        [DataType(DataType.Password)]
        public string? Password { get; set; } // etPassword

        [DataType(DataType.Password)]
        [Compare("Password", ErrorMessage = "Las contrasenas no coinciden")]
        public string? ConfirmPassword { get; set; } // etConfirmarContrasena

        // --- Nuevos campos de perfil enriquecido ---
        public int Vidas { get; set; }
        public int RachaActual { get; set; }
        public int DivisionId { get; set; }
        public string DivisionNombre { get; set; }
        public int TotalXp { get; set; }
        public string TopIdiomaNombre { get; set; }
        public int TopIdiomaXp { get; set; }
        public int NumeroAmigos { get; set; }
        public int XpDivisionActual { get; set; }
        public int XpSiguienteDivision { get; set; }
        public int PorcentajeDivision { get; set; }
        public List<IdiomaProgresoItem> IdiomasProgreso { get; set; } = new List<IdiomaProgresoItem>();
        public List<LogroItem> Logros { get; set; } = new List<LogroItem>();
    }
}
