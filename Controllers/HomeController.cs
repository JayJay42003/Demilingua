using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;

namespace Demilingua.Controllers
{
    [Authorize] // Protegido: Solo usuarios logueados
    public class HomeController : Controller
    {
        private readonly ApiService _apiService;

        public HomeController(ApiService apiService)
        {
            _apiService = apiService;
        }

        public async Task<IActionResult> Index()
        {
            // 1. Idiomas (Como en MainActivity.java)
            var idiomas = new List<Idioma>
            {
                new Idioma { Id = 1, Name = "Español", FlagCode = "es" },
                new Idioma { Id = 2, Name = "Inglés", FlagCode = "gb" },
                new Idioma { Id = 3, Name = "Francés", FlagCode = "fr" }
            };

            // 2. Ranking desde la API
            var ranking = await _apiService.GetRankingAsync();

            var model = new HomeViewModel
            {
                Idiomas = idiomas,
                Ranking = ranking
            };

            return View(model);
        }
    }
}
