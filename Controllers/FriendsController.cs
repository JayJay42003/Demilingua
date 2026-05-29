using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Linq;
using System.Collections.Generic;

namespace Demilingua.Controllers
{
    [Authorize]
    public class FriendsController : Controller
    {
        private readonly ApiService _apiService;

        public FriendsController(ApiService apiService)
        {
            _apiService = apiService;
        }

        public async Task<IActionResult> Index(string query)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            
            // 1. Get all users for name mapping
            var allUsers = await _apiService.GetAllUsersAsync();
            var userNames = new Dictionary<int, string>();
            foreach (var u in allUsers)
            {
                if (int.TryParse(u.GetValueOrDefault("id"), out var uid))
                    userNames[uid] = u.GetValueOrDefault("nombre") ?? $"Usuario #{uid}";
            }

            // 2. Get current friends
            var friends = await _apiService.GetFriendsAsync(userId);
            ViewBag.Friends = friends;
            ViewBag.UserNames = userNames;

            // 3. Search users if query provided
            if (!string.IsNullOrEmpty(query))
            {
                var searchResults = allUsers
                    .Where(u => {
                        var name = u.GetValueOrDefault("nombre", "");
                        return name.Contains(query, System.StringComparison.OrdinalIgnoreCase) &&
                               u.GetValueOrDefault("id") != userId.ToString();
                    })
                    .ToList();
                ViewBag.SearchResults = searchResults;
            }

            return View();
        }

        [HttpPost]
        public async Task<IActionResult> AddFriend(int amigoId)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var ok = await _apiService.AddFriendAsync(userId, amigoId);
            if (!ok) TempData["Error"] = "No se pudo anadir al amigo";
            return RedirectToAction("Index");
        }

        [HttpPost]
        public async Task<IActionResult> AcceptFriend(int amigoId)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            // For accepting, the requester is amigoId and current user is accepting
            var ok = await _apiService.AcceptFriendAsync(amigoId, userId);
            if (!ok) TempData["Error"] = "No se pudo aceptar la solicitud";
            return RedirectToAction("Index");
        }

        [HttpPost]
        public async Task<IActionResult> DeleteFriend(int amigoId)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var ok = await _apiService.DeleteFriendAsync(userId, amigoId);
            if (!ok) TempData["Error"] = "No se pudo eliminar al amigo";
            return RedirectToAction("Index");
        }
    }
}
