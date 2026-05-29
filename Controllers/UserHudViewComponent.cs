using Demilingua.Models;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using System.Threading.Tasks;

namespace Demilingua.Controllers
{
    public class UserHudViewComponent : ViewComponent
    {
        private readonly ApiService _apiService;

        public UserHudViewComponent(ApiService apiService)
        {
            _apiService = apiService;
        }

        public async Task<IViewComponentResult> InvokeAsync()
        {
            var model = new UserHudViewModel
            {
                Vidas = 0,
                RachaActual = 0,
                TotalXp = 0,
                DivisionId = 0
            };

            var userIdClaim = UserClaimsPrincipal?.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (int.TryParse(userIdClaim, out var userId) && userId > 0)
            {
                var profile = await _apiService.GetUserProfileAsync(userId);
                if (profile != null)
                {
                    model.Vidas = profile.Vidas;
                    model.RachaActual = profile.RachaActual;
                    model.TotalXp = profile.TotalXp;
                    model.DivisionId = profile.DivisionId;
                }
            }

            return View(model);
        }
    }
}
