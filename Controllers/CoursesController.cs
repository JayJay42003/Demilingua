using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;

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

        // GET: /Courses?idiomaId=1&nombre=Español
        public async Task<IActionResult> Index(int idiomaId, string nombre)
        {
            ViewBag.IdiomaNombre = nombre ?? "Idioma";
            ViewBag.IdiomaId = idiomaId;

            // Llama a endpoint /api/courses?idiomaId=X
            var cursos = await _apiService.GetCursosAsync(idiomaId);
            
            return View(cursos);
        }
    }
}
