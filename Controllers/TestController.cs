using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;
using System.Security.Claims;

namespace Demilingua.Controllers
{
    [Authorize]
    public class TestController : Controller
    {
        private readonly ApiService _apiService;

        public TestController(ApiService apiService)
        {
            _apiService = apiService;
        }

        // 1. Carga la vista vacía con los datos de sesión necesarios
        public IActionResult Index(int cursoId, int idiomaId)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            
            var model = new TestSessionViewModel
            {
                CursoId = cursoId,
                IdiomaId = idiomaId,
                UserId = userId
            };
            return View(model);
        }

        // --- Endpoints JSON para que JavaScript los consuma (AJAX) ---

        [HttpGet]
        public async Task<IActionResult> GetRandomTest(int cursoId)
        {
            // Llama a Java: /api/test?cursoId=...
            var test = await _apiService.GetRandomTestAsync(cursoId);
            return Json(test);
        }

        [HttpGet]
        public async Task<IActionResult> GetExercises(int testId)
        {
            // Llama a Java: /api/exercises?testId=...
            // Este método debes agregarlo a ApiService si no lo pusiste antes
            // Retorna la lista de ejercicios (Map<String, String>)
            var ejercicios = await _apiService.GetExercisesAsync(testId);
            return Json(ejercicios);
        }

        [HttpPost]
        public async Task<IActionResult> SubmitPoints([FromBody] PuntosSubmission request)
        {
            // Llama a Java: POST /api/puntos
            var success = await _apiService.SavePointsAsync(request.UsuarioId, request.IdiomaId, request.Puntos);
            return Json(new { success = success });
        }

        // DTO pequeño para recibir el POST de puntos
        public class PuntosSubmission
        {
            public int UsuarioId { get; set; }
            public int IdiomaId { get; set; }
            public int Puntos { get; set; }
        }
    }
}
