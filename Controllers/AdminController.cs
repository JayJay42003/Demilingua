using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Linq;

namespace Demilingua.Controllers
{
    [Authorize(Roles = "Admin")]
    public class AdminController : Controller
    {
        private readonly ApiService _apiService;

        public AdminController(ApiService apiService)
        {
            _apiService = apiService;
        }

        public async Task<IActionResult> Index()
        {
            var users = await _apiService.GetAllUsersAsync();
            ViewBag.TotalUsers = users.Count;
            return View(users);
        }

        // --- Usuarios ---

        [HttpPost]
        public async Task<IActionResult> DeleteUser(int userId)
        {
            await _apiService.DeleteUserAsync(userId);
            return RedirectToAction("Index");
        }

        // --- Tests ---

        public async Task<IActionResult> ManageTests(int? cursoId)
        {
            var cursos = await CargarTodosCursosAsync();
            ViewBag.Cursos = cursos;
            var cid = cursoId ?? (cursos.Count > 0 ? cursos[0].Id : 0);
            ViewBag.CursoId = cid;
            if (cid > 0)
            {
                var tests = await _apiService.GetTestsByCursoAsync(cid);
                return View(tests);
            }
            return View(new List<Dictionary<string, object>>());
        }

        [HttpPost]
        public async Task<IActionResult> CreateTest(int cursoId, string titulo)
        {
            await _apiService.CreateTestAsync(cursoId, titulo);
            return RedirectToAction("ManageTests", new { cursoId });
        }

        [HttpPost]
        public async Task<IActionResult> DeleteTest(int testId, int cursoId)
        {
            await _apiService.DeleteTestAsync(testId);
            return RedirectToAction("ManageTests", new { cursoId });
        }

        // --- Idiomas ---

        public async Task<IActionResult> ManageIdiomas()
        {
            var idiomas = await _apiService.GetIdiomasAsync();
            return View(idiomas);
        }

        [HttpPost]
        public async Task<IActionResult> CreateIdioma(string nombre)
        {
            await _apiService.CreateIdiomaAsync(nombre);
            return RedirectToAction("ManageIdiomas");
        }

        [HttpPost]
        public async Task<IActionResult> UpdateIdioma(int id, string nombre)
        {
            await _apiService.UpdateIdiomaAsync(id, nombre);
            return RedirectToAction("ManageIdiomas");
        }

        [HttpPost]
        public async Task<IActionResult> DeleteIdioma(int id)
        {
            await _apiService.DeleteIdiomaAsync(id);
            return RedirectToAction("ManageIdiomas");
        }

        // --- Cursos ---

        public async Task<IActionResult> ManageCourses()
        {
            var idiomas = await _apiService.GetIdiomasAsync();
            ViewBag.Idiomas = idiomas;
            var allCursos = await CargarTodosCursosAsync();
            return View(allCursos);
        }

        [HttpPost]
        public async Task<IActionResult> CreateCurso(int idiomaId, string nombre, string descripcion, string dificultad)
        {
            await _apiService.CreateCursoAsync(idiomaId, nombre, descripcion, dificultad);
            return RedirectToAction("ManageCourses");
        }

        [HttpPost]
        public async Task<IActionResult> DeleteCurso(int id)
        {
            await _apiService.DeleteCursoAsync(id);
            return RedirectToAction("ManageCourses");
        }

        // --- Helper ---

        private async Task<List<Models.Curso>> CargarTodosCursosAsync()
        {
            var idiomas = await _apiService.GetIdiomasAsync();
            var todos = new List<Models.Curso>();
            foreach (var idioma in idiomas)
            {
                var cursos = await _apiService.GetCursosAsync(idioma.Id);
                todos.AddRange(cursos);
            }
            return todos;
        }
    }
}
