using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;

namespace Demilingua.Controllers
{
    [Authorize]
    public class ProfileController : Controller
    {
        private readonly ApiService _apiService;

        public ProfileController(ApiService apiService)
        {
            _apiService = apiService;
        }

        // GET: Ver Perfil
        public IActionResult Index()
        {
            var model = new UserProfileViewModel
            {
                Nombre = User.FindFirst(ClaimTypes.Name)?.Value ?? "Usuario",
                Correo = User.FindFirst(ClaimTypes.Email)?.Value ?? "correo@test.com"
            };
            return View(model);
        }

        // GET: Formulario Editar
        public IActionResult Edit()
        {
            var model = new UserProfileViewModel
            {
                // Obtenemos ID del Claims guardado en Login
                Id = int.Parse(User.FindFirst("UserId")?.Value ?? "0"),
                Nombre = User.FindFirst(ClaimTypes.Name)?.Value ?? "",
                Correo = User.FindFirst(ClaimTypes.Email)?.Value ?? ""
            };
            return View(model);
        }

        // POST: Guardar cambios
        [HttpPost]
        public async Task<IActionResult> Edit(UserProfileViewModel model)
        {
            // Validamos solo Nombre y Correo, ignoramos password si está vacío
            if (string.IsNullOrEmpty(model.Nombre) || string.IsNullOrEmpty(model.Correo))
            {
                ModelState.AddModelError("", "Nombre y Correo son obligatorios");
                return View(model);
            }

            if (!string.IsNullOrEmpty(model.Password) && model.Password != model.ConfirmPassword)
            {
                ModelState.AddModelError("ConfirmPassword", "Las contraseñas no coinciden");
                return View(model);
            }

            // Llamada a la API (endpoint /api/user/update)
            var resultado = await _apiService.UpdateUserAsync(model.Id, model.Nombre, model.Correo, model.Password);

            if (resultado == "ok")
            {
                // Al cambiar datos críticos, cerramos sesión para obligar a reloguear y actualizar cookies
                await HttpContext.SignOutAsync();
                return RedirectToAction("Login", "Account");
            }

            ViewBag.Error = "Error al actualizar: " + resultado;
            return View(model);
        }
    }
}
