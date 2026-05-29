using Microsoft.AspNetCore.Mvc;
using Demilingua.Models;

namespace Demilingua.Controllers
{
    public class HomeController : Controller
    {
        private readonly ApiService _apiService;

        public HomeController(ApiService apiService)
        {
            _apiService = apiService;
        }

        public async Task<IActionResult> Index(int? division)
        {
            // 1. Idiomas desde la API real
            var idiomas = await _apiService.GetIdiomasAsync();

            // 2. Ranking segun filtro
            List<RankingItem> ranking;
            if (division.HasValue && division.Value >= 1 && division.Value <= 5)
            {
                ranking = await _apiService.GetRankingByDivisionAsync(division.Value);
            }
            else
            {
                ranking = await _apiService.GetRankingAsync();
            }

            // 3. Nombre de la division seleccionada
            ViewBag.DivisionActual = division;
            ViewBag.NombresDivision = new[] { "Bronce", "Plata", "Oro", "Platino", "Diamante" };

            var model = new HomeViewModel
            {
                Idiomas = idiomas,
                Ranking = ranking
            };

            return View(model);
        }
    }
}
