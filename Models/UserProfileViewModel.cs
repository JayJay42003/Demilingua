using System.ComponentModel.DataAnnotations;

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

        // Campos opcionales para cambio de contraseña (solo en Edit)
        [DataType(DataType.Password)]
        public string? Password { get; set; } // etPassword

        [DataType(DataType.Password)]
        [Compare("Password", ErrorMessage = "Las contraseñas no coinciden")]
        public string? ConfirmPassword { get; set; } // etConfirmarContrasena
    }
}
