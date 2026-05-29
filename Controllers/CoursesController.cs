using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;
using System.Security.Claims;

namespace Demilingua.Controllers
{
    [Authorize]
    public class CoursesController : Controller
    {
        private readonly ApiService _apiService;

        public CoursesController(ApiService apiService)
        {
            _apiService = apiService;
        }

        // GET: /Courses?idiomaId=1&nombre=Ingles
        public async Task<IActionResult> Index(int idiomaId, string nombre)
        {
            var idiomaNombre = string.IsNullOrWhiteSpace(nombre) ? "Idioma" : nombre;
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

            // Verificar vidas del usuario
            var profile = await _apiService.GetUserProfileAsync(userId);
            var vidas = profile?.Vidas ?? 5;
            ViewBag.Vidas = vidas;
            ViewBag.SinVidas = vidas <= 0;

            var cursos = await _apiService.GetCursosAsync(idiomaId);

            var items = new List<CoursePathItem>();
            foreach (var curso in cursos)
            {
                items.Add(new CoursePathItem
                {
                    Curso = curso,
                    TestId = 0,
                    IsCompleted = false,
                    IsUnlocked = vidas > 0 // Bloquear si no tiene vidas
                });
            }

            var model = new CoursePathViewModel
            {
                IdiomaId = idiomaId,
                IdiomaNombre = idiomaNombre,
                Items = items
            };

            return View(model);
        }
    }
}
