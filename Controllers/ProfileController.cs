using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Demilingua.Models;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;
using System.Linq;

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
        public async Task<IActionResult> Index()
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var model = new UserProfileViewModel
            {
                Id = userId,
                Nombre = User.FindFirst(ClaimTypes.Name)?.Value ?? "Usuario",
                Correo = User.FindFirst(ClaimTypes.Email)?.Value ?? "correo@test.com"
            };

            if (userId > 0)
            {
                // 1. Status: vidas, racha, division_id
                var profile = await _apiService.GetUserProfileAsync(userId);
                if (profile != null)
                {
                    model.Vidas = profile.Vidas;
                    model.RachaActual = profile.RachaActual;
                    model.DivisionId = profile.DivisionId;
                }

                // 2. Progreso por idioma
                var progress = await _apiService.GetUserProgressAsync(userId);
                
                // 3. Idiomas para traducir nombres
                var idiomas = await _apiService.GetIdiomasAsync();
                var idiomaMap = new Dictionary<int, string>();
                foreach (var idioma in idiomas)
                    idiomaMap[idioma.Id] = idioma.Name;

                // 4. Calcular total y progreso por idioma
                model.TotalXp = 0;
                model.TopIdiomaXp = 0;
                model.TopIdiomaNombre = "-";
                model.IdiomasProgreso = new System.Collections.Generic.List<IdiomaProgresoItem>();

                foreach (var row in progress)
                {
                    var idiomaId = int.TryParse(row.GetValueOrDefault("idioma_id"), out var iid) ? iid : 0;
                    var puntos = int.TryParse(row.GetValueOrDefault("puntos"), out var pts) ? pts : 0;
                    var nombre = idiomaMap.ContainsKey(idiomaId) ? idiomaMap[idiomaId] : $"Idioma #{idiomaId}";

                    model.TotalXp += puntos;
                    model.IdiomasProgreso.Add(new IdiomaProgresoItem
                    {
                        IdiomaId = idiomaId,
                        IdiomaNombre = nombre,
                        Puntos = puntos
                    });

                    if (puntos > model.TopIdiomaXp)
                    {
                        model.TopIdiomaXp = puntos;
                        model.TopIdiomaNombre = nombre;
                    }
                }

                // Calcular porcentajes de cada idioma respecto al total
                foreach (var item in model.IdiomasProgreso)
                {
                    item.PorcentajeDelTotal = model.TotalXp > 0
                        ? (int)Math.Round((double)item.Puntos / model.TotalXp * 100)
                        : 0;
                }

                // 5. Division mapping
                var (divNombre, xpActual, xpSiguiente, porcentaje) = CalcularDivision(model.DivisionId, model.TotalXp);
                model.DivisionNombre = divNombre;
                model.XpDivisionActual = xpActual;
                model.XpSiguienteDivision = xpSiguiente;
                model.PorcentajeDivision = porcentaje;

                // 6. Amigos
                var friends = await _apiService.GetFriendsAsync(userId);
                model.NumeroAmigos = friends.Count;

                // 7. Logros / Insignias
                model.Logros = new List<LogroItem>
                {
                    new LogroItem { Nombre = "Primeros pasos", Icono = "\ud83d\udc76", Descripcion = "Consigue 100 XP", Desbloqueado = model.TotalXp >= 100 },
                    new LogroItem { Nombre = "Estudiante", Icono = "\ud83d\udcda", Descripcion = "Consigue 1000 XP", Desbloqueado = model.TotalXp >= 1000 },
                    new LogroItem { Nombre = "Experto", Icono = "\ud83c\udf93", Descripcion = "Consigue 5000 XP", Desbloqueado = model.TotalXp >= 5000 },
                    new LogroItem { Nombre = "Leyenda", Icono = "\ud83d\udc51", Descripcion = "Consigue 10000 XP", Desbloqueado = model.TotalXp >= 10000 },
                    new LogroItem { Nombre = "Racha 3", Icono = "\ud83d\udd25", Descripcion = "3 dias seguidos", Desbloqueado = model.RachaActual >= 3 },
                    new LogroItem { Nombre = "Racha 7", Icono = "\ud83d\udd25", Descripcion = "7 dias seguidos", Desbloqueado = model.RachaActual >= 7 },
                    new LogroItem { Nombre = "Racha 30", Icono = "\ud83d\udfe3", Descripcion = "30 dias seguidos", Desbloqueado = model.RachaActual >= 30 },
                    new LogroItem { Nombre = "Poliglota", Icono = "\ud83c\udf0d", Descripcion = "Practica 3 idiomas", Desbloqueado = model.IdiomasProgreso.Count >= 3 },
                    new LogroItem { Nombre = "Social", Icono = "\ud83e\udd1d", Descripcion = "Ten 5 amigos", Desbloqueado = model.NumeroAmigos >= 5 },
                    new LogroItem { Nombre = "Diamante", Icono = "\ud83d\udc8e", Descripcion = "Alcanza division Diamante", Desbloqueado = model.DivisionId >= 5 },
                };
            }

            return View(model);
        }

        private static (string nombre, int xpActual, int xpSiguiente, int porcentaje) CalcularDivision(int divisionId, int totalXp)
        {
            var divisiones = new (int id, string nombre, int xpMinimo)[] {
                (1, "Bronce", 0),
                (2, "Plata", 500),
                (3, "Oro", 1500),
                (4, "Platino", 3000),
                (5, "Diamante", 5000)
            };

            var actual = divisiones.FirstOrDefault(d => d.id == divisionId);
            if (actual == default) actual = divisiones[0];

            var siguiente = divisiones.FirstOrDefault(d => d.xpMinimo > actual.xpMinimo);

            string nombre = actual.nombre;
            int xpActual = actual.xpMinimo;
            int xpSiguiente = siguiente != default ? siguiente.xpMinimo : actual.xpMinimo;
            int porcentaje;

            if (xpSiguiente <= xpActual)
            {
                porcentaje = 100;
            }
            else
            {
                var progreso = totalXp - xpActual;
                var rango = xpSiguiente - xpActual;
                porcentaje = Math.Clamp((int)Math.Round((double)progreso / rango * 100), 0, 100);
            }

            return (nombre, xpActual, xpSiguiente, porcentaje);
        }

        // GET: Formulario Editar
        public IActionResult Edit()
        {
            var model = new UserProfileViewModel
            {
                // Obtenemos ID del Claims guardado en Login
                Id = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0"),
                Nombre = User.FindFirst(ClaimTypes.Name)?.Value ?? "",
                Correo = User.FindFirst(ClaimTypes.Email)?.Value ?? ""
            };
            return View(model);
        }

        // POST: Guardar cambios
        [HttpPost]
        public async Task<IActionResult> Edit(UserProfileViewModel model)
        {
            // Validamos solo Nombre y Correo, ignoramos password si est� vac�o
            if (string.IsNullOrEmpty(model.Nombre) || string.IsNullOrEmpty(model.Correo))
            {
                ModelState.AddModelError("", "Nombre y Correo son obligatorios");
                return View(model);
            }

            if (!string.IsNullOrEmpty(model.Password) && model.Password != model.ConfirmPassword)
            {
                ModelState.AddModelError("ConfirmPassword", "Las contrase�as no coinciden");
                return View(model);
            }

            // Llamada a la API (endpoint /api/users/{id})
            var resultado = await _apiService.UpdateUserAsync(model.Id, model.Nombre, model.Correo);

            if (resultado == "ok")
            {
                // Al cambiar datos cr�ticos, cerramos sesi�n para obligar a reloguear y actualizar cookies
                await HttpContext.SignOutAsync();
                return RedirectToAction("Login", "Account");
            }

            ViewBag.Error = "Error al actualizar: " + resultado;
            return View(model);
        }
    }
}
