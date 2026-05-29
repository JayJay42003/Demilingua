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

        // 1. Carga la vista vacia con los datos de sesion necesarios
        public async Task<IActionResult> Index(int cursoId, int idiomaId)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");

            // Verificar vidas antes de permitir el test
            var profile = await _apiService.GetUserProfileAsync(userId);
            if (profile != null && profile.Vidas <= 0)
            {
                TempData["Error"] = "Te has quedado sin vidas. Espera a que se recarguen.";
                return RedirectToAction("Index", "Courses", new { idiomaId });
            }
            
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
            // Este m�todo debes agregarlo a ApiService si no lo pusiste antes
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

        [HttpPost]
        public async Task<IActionResult> CompleteTest(int usuarioId, int testId, int puntuacion)
        {
            var ok = await _apiService.CompleteTestAsync(usuarioId, testId, puntuacion);
            return Json(new { success = ok });
        }

        [HttpPost]
        public async Task<IActionResult> SubtractLife(int usuarioId)
        {
            var ok = await _apiService.SubtractLifeAsync(usuarioId);
            return Json(new { success = ok });
        }

        [HttpPost]
        public async Task<IActionResult> UpdateStreak(int usuarioId, int xp)
        {
            var ok = await _apiService.UpdateStreakAsync(usuarioId, xp);
            return Json(new { success = ok });
        }

        [HttpPost]
        public async Task<IActionResult> EvaluateLeague(int usuarioId)
        {
            var divisionId = await _apiService.EvaluateLeagueAsync(usuarioId);
            return Json(new { divisionId = divisionId ?? string.Empty });
        }

        // DTO peque�o para recibir el POST de puntos
        public class PuntosSubmission
        {
            public int UsuarioId { get; set; }
            public int IdiomaId { get; set; }
            public int Puntos { get; set; }
        }
    }
}
