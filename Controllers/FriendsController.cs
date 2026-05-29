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
            var apiFriends = await _apiService.GetFriendsAsync(userId);
            var friendsList = new List<Dictionary<string, string>>();
            foreach (var f in apiFriends)
            {
                var u1Str = f.GetValueOrDefault("usuarioId1") ?? f.GetValueOrDefault("usuario_id_1") ?? "0";
                var u2Str = f.GetValueOrDefault("usuarioId2") ?? f.GetValueOrDefault("usuario_id_2") ?? "0";

                // If it's returning composite keys or something like that, try to parse
                int.TryParse(u1Str, out int id1);
                int.TryParse(u2Str, out int id2);

                int amigoId = id1 == userId ? id2 : id1;
                bool canAccept = id2 == userId;

                // Fallback if the backend sends an explicit amigo_id
                if (f.ContainsKey("amigo_id") && int.TryParse(f["amigo_id"], out var aId))
                {
                    amigoId = aId;
                }

                // Fallback for missing id parsing
                if (amigoId == 0) {
                     var id1Alt = f.Keys.FirstOrDefault(k => k.Contains("id_1") || k.Contains("Id1"));
                     var id2Alt = f.Keys.FirstOrDefault(k => k.Contains("id_2") || k.Contains("Id2"));
                     if (id1Alt != null) int.TryParse(f[id1Alt], out id1);
                     if (id2Alt != null) int.TryParse(f[id2Alt], out id2);

                     // Check for nested "id" object from JPA composite keys (like "{"usuarioId1":13,"usuarioId2":1}")
                     if (id1 == 0 && id2 == 0 && f.ContainsKey("id")) 
                     {
                         var idJson = f["id"];
                         try {
                             var parsedId = System.Text.Json.JsonDocument.Parse(idJson).RootElement;
                             if (parsedId.TryGetProperty("usuarioId1", out var p1) || parsedId.TryGetProperty("usuario_id_1", out p1)) 
                                 int.TryParse(p1.ToString(), out id1);
                             if (parsedId.TryGetProperty("usuarioId2", out var p2) || parsedId.TryGetProperty("usuario_id_2", out p2)) 
                                 int.TryParse(p2.ToString(), out id2);
                         } catch {}
                     }

                     // Check for fully populated entity relationships (like "{"usuario1":{"id":13},"usuario2":{"id":1}}")
                     if (id1 == 0 && id2 == 0)
                     {
                         var u1Key = f.Keys.FirstOrDefault(k => k.Equals("usuario1", System.StringComparison.OrdinalIgnoreCase));
                         var u2Key = f.Keys.FirstOrDefault(k => k.Equals("usuario2", System.StringComparison.OrdinalIgnoreCase));
                         if (u1Key != null) { try { if (System.Text.Json.JsonDocument.Parse(f[u1Key]).RootElement.TryGetProperty("id", out var px)) int.TryParse(px.ToString(), out id1); } catch {}}
                         if (u2Key != null) { try { if (System.Text.Json.JsonDocument.Parse(f[u2Key]).RootElement.TryGetProperty("id", out var py)) int.TryParse(py.ToString(), out id2); } catch {}}
                     }

                     amigoId = id1 == userId ? id2 : id1;
                     canAccept = id2 == userId;
                }

                if (amigoId == 0) continue;

                var dict = new Dictionary<string, string>();
                dict["amigo_id"] = amigoId.ToString();
                dict["estado"] = f.GetValueOrDefault("estado") ?? "PENDIENTE";
                dict["can_accept"] = canAccept ? "true" : "false";

                friendsList.Add(dict);
            }

            ViewBag.Friends = friendsList;
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
