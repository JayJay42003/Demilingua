using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using Demilingua.Models;
using System.Text.Json;
using System.Text;
using System;

namespace Demilingua.Controllers
{
    public class ApiService
    {
        private readonly HttpClient _httpClient;
        private readonly Microsoft.AspNetCore.Http.IHttpContextAccessor _httpContextAccessor;

        public ApiService(HttpClient httpClient, Microsoft.AspNetCore.Http.IHttpContextAccessor httpContextAccessor)
        {
            _httpClient = httpClient;
            _httpContextAccessor = httpContextAccessor;

            var token = _httpContextAccessor.HttpContext?.User?.FindFirst("Token")?.Value;
            if (!string.IsNullOrEmpty(token))
            {
                _httpClient.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            }
        }

        public async Task<List<RankingItem>> GetRankingAsync()
        {
            var items = new List<RankingItem>();
            var response = await _httpClient.GetAsync("/api/ranking");
            if (!response.IsSuccessStatusCode) return items;
            
            var json = await response.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            if (json == null) return items;
            
            foreach (var row in json)
            {
                items.Add(new RankingItem
                {
                    Usuario = row.GetValueOrDefault("nombre") ?? string.Empty,
                    Idioma = row.GetValueOrDefault("division") ?? string.Empty,
                    Puntos = int.TryParse(row.GetValueOrDefault("racha"), out var p) ? p : 0
                });
            }
            return items;
        }

        public async Task<List<RankingItem>> GetRankingByDivisionAsync(int divisionId)
        {
            var items = new List<RankingItem>();
            var response = await _httpClient.GetAsync($"/api/ranking/division/{divisionId}");
            if (!response.IsSuccessStatusCode) return items;
            
            var json = await response.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            if (json == null) return items;
            
            foreach (var row in json)
            {
                items.Add(new RankingItem
                {
                    Usuario = row.GetValueOrDefault("nombre") ?? string.Empty,
                    Idioma = $"Division {divisionId}",
                    Puntos = int.TryParse(row.GetValueOrDefault("racha"), out var p) ? p : 0
                });
            }
            return items;
        }

        public async Task<(string status, string nombre, string user_id, string? message, string? token)> LoginAsync(string correo, string password)
        {
            var payload = new { correo = correo, contrasena = password };
            var resp = await _httpClient.PostAsJsonAsync("/api/auth/login", payload);

            if (!resp.IsSuccessStatusCode)
                return ("error", "", "", "Error de conexin", null);

            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, JsonElement>>();
            if (data == null) return ("error", "", "", "Respuesta invlida", null);

            string status = data.ContainsKey("status") ? data["status"].GetString() ?? "error" : "error";
            string message = data.ContainsKey("message") ? data["message"].GetString() : null;
            string nombre = data.ContainsKey("nombre") ? data["nombre"].GetString() ?? "" : "";
            string token = data.ContainsKey("token") ? data["token"].GetString() : null;

            string userId = "";
            if (data.ContainsKey("user_id")) {
                var uid = data["user_id"];
                if (uid.ValueKind == JsonValueKind.Number) userId = uid.GetInt32().ToString();
                else if (uid.ValueKind == JsonValueKind.String) userId = uid.GetString() ?? "";
            }

            return (status, nombre, userId, message, token);
        }

        public async Task<(string status, string? message)> RegisterAsync(string nombre, string correo, string password)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("nombre", nombre),
                new KeyValuePair<string, string>("correo", correo),
                new KeyValuePair<string, string>("contrasena", password)
            });
            var resp = await _httpClient.PostAsync("/api/users", content);
            if (!resp.IsSuccessStatusCode) return ("error", "Error de conexin");
            
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return (data?.GetValueOrDefault("status") ?? "error", data?.GetValueOrDefault("message"));
        }

        public async Task<string> UpdateUserAsync(int id, string nombre, string correo)
        {
            var url = $"/api/users/{id}?nombre={Uri.EscapeDataString(nombre)}&correo={Uri.EscapeDataString(correo)}";
            var resp = await _httpClient.PutAsync(url, null);
            if (!resp.IsSuccessStatusCode) return "error";
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return data?.GetValueOrDefault("status") ?? "error";
        }

        public async Task<UserProfileInfo?> GetUserProfileAsync(int usuarioId)
        {
            var resp = await _httpClient.GetAsync($"/api/gamification/status/{usuarioId}");
            if (!resp.IsSuccessStatusCode) return null;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            if (data == null) return null;
            
            return new UserProfileInfo
            {
                Id = usuarioId,
                Vidas = int.TryParse(data.GetValueOrDefault("vidas"), out var vidas) ? vidas : 0,
                RachaActual = int.TryParse(data.GetValueOrDefault("racha"), out var racha) ? racha : 0,
                DivisionId = int.TryParse(data.GetValueOrDefault("division_id"), out var div) ? div : 0
            };
        }

        public async Task<List<Idioma>> GetIdiomasAsync()
        {
            var idiomas = new List<Idioma>();
            var resp = await _httpClient.GetAsync("/api/idiomas");
            if (!resp.IsSuccessStatusCode) return idiomas;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, object>>>();
            if (data == null) return idiomas;
            foreach (var row in data)
            {
                var nombre = row.GetValueOrDefault("nombre")?.ToString() ?? "";
                idiomas.Add(new Idioma
                {
                    Id = int.TryParse(row.GetValueOrDefault("id")?.ToString(), out var id) ? id : 0,
                    Name = nombre,
                    FlagCode = MapFlagCode(nombre)
                });
            }
            return idiomas;
        }

        private static string MapFlagCode(string nombre)
        {
            return nombre.ToLower() switch
            {
                "inglés" or "ingles" => "gb",
                "francés" or "frances" => "fr",
                "alemán" or "aleman" => "de",
                "italiano" => "it",
                "portugués" or "portugues" => "br",
                _ => "es"
            };
        }

        public async Task<List<Curso>> GetCursosAsync(int idiomaId)
        {
            var cursos = new List<Curso>();
            var resp = await _httpClient.GetAsync($"/api/cursos/{idiomaId}");
            if (!resp.IsSuccessStatusCode) return cursos;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, object>>>();
            if (data == null) return cursos;
            foreach (var row in data)
            {
                cursos.Add(new Curso
                {
                    Id = int.TryParse(row.GetValueOrDefault("id")?.ToString(), out var id) ? id : 0,
                    Nombre = row.GetValueOrDefault("nombre")?.ToString() ?? string.Empty,
                    Descripcion = row.GetValueOrDefault("descripcion")?.ToString() ?? string.Empty,
                    Dificultad = row.GetValueOrDefault("dificultad")?.ToString() ?? string.Empty,
                    IdiomaId = int.TryParse(row.GetValueOrDefault("idioma_id")?.ToString(), out var iid) ? iid : idiomaId
                });
            }
            return cursos;
        }

        public async Task<Dictionary<string, object>> GetRandomTestAsync(int cursoId)
        {
            var resp = await _httpClient.GetAsync($"/api/tests/{cursoId}");
            if (!resp.IsSuccessStatusCode)
            {
                return new Dictionary<string, object> { ["id"] = "0", ["titulo"] = "", ["curso_id"] = cursoId.ToString() };
            }
            var list = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, object>>>();
            if (list == null || list.Count == 0)
            {
                return new Dictionary<string, object> { ["id"] = "0", ["titulo"] = "", ["curso_id"] = cursoId.ToString() };
            }
            return list[0];
        }

        public async Task<List<EjercicioDto>> GetExercisesAsync(int testId)
        {
            var list = new List<EjercicioDto>();
            var resp = await _httpClient.GetAsync($"/api/tests/ejercicios/{testId}");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, object>>>();
            if (data == null) return list;
            foreach (var row in data)
            {
                var objetos = row.GetValueOrDefault("objetos") as List<object>;
                var firstObj = objetos?.FirstOrDefault() as Dictionary<string, object>;

                list.Add(new EjercicioDto
                {
                    ejercicioId = row.GetValueOrDefault("id")?.ToString() ?? string.Empty,
                    tipo = row.GetValueOrDefault("tipo")?.ToString() ?? string.Empty,
                    puntos = row.GetValueOrDefault("puntuacion")?.ToString() ?? "0",
                    contenido = firstObj?.GetValueOrDefault("contenido")?.ToString() ?? string.Empty,
                    respuesta = firstObj?.GetValueOrDefault("respuesta_correcta")?.ToString() ?? string.Empty,
                    opciones = firstObj?.GetValueOrDefault("opciones")?.ToString()
                });
            }
            return list;
        }

        public async Task<bool> SavePointsAsync(int usuarioId, int idiomaId, int puntos)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("usuarioId", usuarioId.ToString()),
                new KeyValuePair<string, string>("idiomaId", idiomaId.ToString()),
                new KeyValuePair<string, string>("puntos", puntos.ToString())
            });
            var resp = await _httpClient.PostAsync("/api/gamification/add-xp", content);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> SubtractLifeAsync(int usuarioId)
        {
            var url = $"/api/gamification/perder-vida?usuarioId={usuarioId}";
            var resp = await _httpClient.PostAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<List<Dictionary<string, string>>> GetFriendsAsync(int usuarioId)
        {
            var list = new List<Dictionary<string, string>>();
            var resp = await _httpClient.GetAsync($"/api/friends/{usuarioId}");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            return data ?? list;
        }

        public async Task<bool> AddFriendAsync(int usuarioId1, int usuarioId2)
        {
            var url = $"/api/friends?usuarioId1={usuarioId1}&usuarioId2={usuarioId2}";
            var resp = await _httpClient.PostAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> AcceptFriendAsync(int usuarioId1, int usuarioId2)
        {
            var url = $"/api/friends?usuarioId1={usuarioId1}&usuarioId2={usuarioId2}";
            var resp = await _httpClient.PutAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> DeleteFriendAsync(int usuarioId1, int usuarioId2)
        {
            var url = $"/api/friends?usuarioId1={usuarioId1}&usuarioId2={usuarioId2}";
            var resp = await _httpClient.DeleteAsync(url);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<List<Dictionary<string, string>>> GetAllUsersAsync()
        {
            var list = new List<Dictionary<string, string>>();
            var resp = await _httpClient.GetAsync("/api/users");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            return data ?? list;
        }

        public async Task<bool> DeleteUserAsync(int id)
        {
            var resp = await _httpClient.DeleteAsync($"/api/users/{id}");
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", System.StringComparison.OrdinalIgnoreCase);
        }

        public async Task<List<Dictionary<string, string>>> GetUserProgressAsync(int usuarioId)
        {
            var list = new List<Dictionary<string, string>>();
            var resp = await _httpClient.GetAsync($"/api/progress/{usuarioId}");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            return data ?? list;
        }

        // --- CRUD Idiomas ---
        public async Task<bool> CreateIdiomaAsync(string nombre)
        {
            var url = $"/api/idiomas?nombre={Uri.EscapeDataString(nombre)}";
            var resp = await _httpClient.PostAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> UpdateIdiomaAsync(int id, string nombre)
        {
            var url = $"/api/idiomas/{id}?nombre={Uri.EscapeDataString(nombre)}";
            var resp = await _httpClient.PutAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> DeleteIdiomaAsync(int id)
        {
            var resp = await _httpClient.DeleteAsync($"/api/idiomas/{id}");
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        // --- CRUD Cursos ---
        public async Task<bool> CreateCursoAsync(int idiomaId, string nombre, string descripcion, string dificultad)
        {
            var url = $"/api/cursos?idiomaId={idiomaId}&nombre={Uri.EscapeDataString(nombre)}&descripcion={Uri.EscapeDataString(descripcion)}&dificultad={Uri.EscapeDataString(dificultad)}";
            var resp = await _httpClient.PostAsync(url, null);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> DeleteCursoAsync(int id)
        {
            var resp = await _httpClient.DeleteAsync($"/api/cursos/{id}");
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<List<Dictionary<string, object>>> GetTestsByCursoAsync(int cursoId)
        {
            var list = new List<Dictionary<string, object>>();
            var resp = await _httpClient.GetAsync($"/api/tests/{cursoId}");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, object>>>();
            return data ?? list;
        }

        public async Task<bool> CreateTestAsync(int cursoId, string titulo)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("cursoId", cursoId.ToString()),
                new KeyValuePair<string, string>("titulo", titulo)
            });
            var resp = await _httpClient.PostAsync("/api/test", content);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", System.StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> DeleteTestAsync(int testId)
        {
            var resp = await _httpClient.DeleteAsync($"/api/test/{testId}");
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", System.StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> CompleteTestAsync(int usuarioId, int testId, int puntuacion)
        {
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("usuarioId", usuarioId.ToString()),
                new KeyValuePair<string, string>("testId", testId.ToString()),
                new KeyValuePair<string, string>("puntuacion", puntuacion.ToString())
            });
            var resp = await _httpClient.PostAsync("/api/progress/test", content);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", System.StringComparison.OrdinalIgnoreCase);
        }

        public async Task<bool> UpdateStreakAsync(int usuarioId, int xp)
        {
            // Update Streak logic. Backend automatically does it in add-xp maybe, or we return true
            return true;
        }

        public async Task<string?> EvaluateLeagueAsync(int usuarioId)
        {
            // Dummy for now to satisfy compiler
            return "2";
        }

        public async Task<List<int>> GetCompletedTestsAsync(int usuarioId)
        {
            return new List<int>();
        }
    }
}
