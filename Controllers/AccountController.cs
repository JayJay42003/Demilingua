using Demilingua.Models;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace Demilingua.Controllers
{
    public class AccountController : Controller
    {
        private readonly ApiService _apiService;

        public AccountController(ApiService apiService)
        {
            _apiService = apiService;
        }

        // GET: Muestra el formulario de login
        [HttpGet]
        [AllowAnonymous]
        public IActionResult Login(string? returnUrl = null)
        {
            ViewData["ReturnUrl"] = returnUrl;
            return View(new LoginViewModel());
        }

        // POST: Procesa los datos del formulario
        [HttpPost]
        [AllowAnonymous]
        public async Task<IActionResult> Login(LoginViewModel model, string? returnUrl = null)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            // 1. Llamar a tu API de Java
            var result = await _apiService.LoginAsync(model.Correo, model.Password);
            if (result.status == "ok")
            {
                // 2. Crear los datos de la sesión (Claims)
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.NameIdentifier, result.user_id ?? ""),
                    new Claim(ClaimTypes.Name, result.nombre ?? model.Correo),
                    new Claim(ClaimTypes.Email, model.Correo)
                };

                var identity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
                var principal = new ClaimsPrincipal(identity);
                // 3. Iniciar sesión en la cookie
                await HttpContext.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme, principal);

                if (!string.IsNullOrEmpty(returnUrl) && Url.IsLocalUrl(returnUrl))
                {
                    return Redirect(returnUrl);
                }
                return RedirectToAction("Index", "Home");
            }

            // Si falla, mostramos error
            ViewBag.Error = result.message ?? "Credenciales inválidas";
            return View(model);
        }

        [Authorize]
        [HttpPost]  
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            return RedirectToAction("Login", "Account");
        }
    }
}