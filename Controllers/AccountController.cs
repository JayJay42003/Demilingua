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
                // 2. Crear los datos de la sesi�n (Claims)
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.NameIdentifier, result.user_id ?? ""),
                    new Claim(ClaimTypes.Name, result.nombre ?? model.Correo),
                    new Claim(ClaimTypes.Email, model.Correo)
                };

                if (!string.IsNullOrEmpty(result.token))
                {
                    claims.Add(new Claim("Token", result.token));
                }

                if (model.Correo == "admin@demilingua.com")
                {
                    claims.Add(new Claim(ClaimTypes.Role, "Admin"));
                }

                var identity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
                var principal = new ClaimsPrincipal(identity);
                // 3. Iniciar sesi�n en la cookie
                await HttpContext.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme, principal);

                if (!string.IsNullOrEmpty(returnUrl) && Url.IsLocalUrl(returnUrl))
                {
                    return Redirect(returnUrl);
                }
                return RedirectToAction("Index", "Home");
            }

            // Si falla, mostramos error
            ViewBag.Error = result.message ?? "Credenciales inv�lidas";
            return View(model);
        }

        [Authorize]
        [HttpPost]  
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            return RedirectToAction("Login", "Account");
        }

        // GET: Muestra formulario de registro
        [HttpGet]
        [AllowAnonymous]
        public IActionResult Register()
        {
            return View(new RegisterViewModel());
        }

        // POST: Procesa el registro
        [HttpPost]
        [AllowAnonymous]
        public async Task<IActionResult> Register(RegisterViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var result = await _apiService.RegisterAsync(model.Nombre, model.Correo, model.Password);
            if (result.status == "ok")
            {
                return RedirectToAction("Login", "Account");
            }

            ViewBag.Error = result.message ?? "No se pudo registrar";
            return View(model);
        }
    }
}
