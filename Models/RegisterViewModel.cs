using System.ComponentModel.DataAnnotations;

namespace Demilingua.Models
{
    public class RegisterViewModel
    {
        [Required]
        public string Nombre { get; set; } // etNombre

        [Required]
        [EmailAddress]
        public string Correo { get; set; } // etCorreo

        [Required]
        [MinLength(6, ErrorMessage = "Mínimo 6 caracteres")]
        [DataType(DataType.Password)]
        public string Password { get; set; } // etContrasena

        [Required]
        [DataType(DataType.Password)]
        [Compare("Password", ErrorMessage = "Las contraseñas no coinciden")]
        public string ConfirmPassword { get; set; } // etConfirmarContrasena
    }
}
