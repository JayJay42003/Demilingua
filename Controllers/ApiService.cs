using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using Demilingua.Models;
using System.Text.Json;
using System.Text;

namespace Demilingua.Controllers
{
    public class ApiService
    {
        private readonly HttpClient _httpClient;

        public ApiService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<List<RankingItem>> GetRankingAsync()
        {
            var items = new List<RankingItem>();
            Console.WriteLine($"[ApiService] GET /api/ranking -> BaseAddress={_httpClient.BaseAddress}");
            var response = await _httpClient.GetAsync("/api/ranking");
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"[ApiService] /api/ranking failed: {(int)response.StatusCode} {response.ReasonPhrase}");
                return items;
            }
            var json = await response.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            Console.WriteLine($"[ApiService] /api/ranking items: {json?.Count ?? 0}");
            if (json == null) return items;
            foreach (var row in json)
            {
                items.Add(new RankingItem
                {
                    Usuario = row.GetValueOrDefault("usuario") ?? string.Empty,
                    Idioma = row.GetValueOrDefault("idioma") ?? string.Empty,
                    Puntos = int.TryParse(row.GetValueOrDefault("puntos"), out var p) ? p : 0
                });
            }
            return items;
        }

        public async Task<(string status, string nombre, string user_id, string? message)> LoginAsync(string correo, string password)
        {
            var payload = new Dictionary<string, string>
            {
                ["username"] = correo,
                ["password"] = password
            };
            Console.WriteLine($"[ApiService] POST /api/login -> BaseAddress={_httpClient.BaseAddress}, username={correo}");
            var resp = await _httpClient.PostAsJsonAsync("/api/login", payload);
            if (!resp.IsSuccessStatusCode)
            {
                Console.WriteLine($"[ApiService] /api/login failed: {(int)resp.StatusCode} {resp.ReasonPhrase}");
                return ("error", "", "", "Error de conexión");
            }
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            Console.WriteLine($"[ApiService] /api/login response: status={data?.GetValueOrDefault("status")}, user_id={data?.GetValueOrDefault("user_id")}, nombre={data?.GetValueOrDefault("nombre")}");
            if (data == null)
            {
                return ("error", "", "", "Respuesta inválida");
            }
            return (
                data.GetValueOrDefault("status") ?? "error",
                data.GetValueOrDefault("nombre") ?? string.Empty,
                data.GetValueOrDefault("user_id") ?? string.Empty,
                data.GetValueOrDefault("message")
            );
        }

        public async Task<(string status, string? message)> RegisterAsync(string nombre, string correo, string password)
        {
            var payload = new Dictionary<string, string>
            {
                ["nombre"] = nombre,
                ["correo"] = correo,
                ["contrasena"] = password
            };
            var resp = await _httpClient.PostAsJsonAsync("/api/register", payload);
            if (!resp.IsSuccessStatusCode)
            {
                return ("error", "Error de conexión");
            }
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            var status = data?.GetValueOrDefault("status") ?? "error";
            var message = data?.GetValueOrDefault("message");
            return (status, message);
        }

        public async Task<string> UpdateUserAsync(int id, string nombre, string correo, string? password)
        {
            var payload = new Dictionary<string, string>
            {
                ["id"] = id.ToString(),
                ["nombre"] = nombre,
                ["correo"] = correo,
                ["contrasena"] = password ?? string.Empty
            };
            var resp = await _httpClient.PostAsJsonAsync("/api/user/update", payload);
            if (!resp.IsSuccessStatusCode) return "error";
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return data?.GetValueOrDefault("status") ?? "error";
        }

        public async Task<List<Curso>> GetCursosAsync(int idiomaId)
        {
            var cursos = new List<Curso>();
            var resp = await _httpClient.GetAsync($"/api/courses?idiomaId={idiomaId}");
            if (!resp.IsSuccessStatusCode) return cursos;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            if (data == null) return cursos;
            foreach (var row in data)
            {
                cursos.Add(new Curso
                {
                    Id = int.TryParse(row.GetValueOrDefault("id"), out var id) ? id : 0,
                    Nombre = row.GetValueOrDefault("nombre") ?? string.Empty,
                    Descripcion = row.GetValueOrDefault("descripcion") ?? string.Empty,
                    Dificultad = row.GetValueOrDefault("dificultad") ?? string.Empty,
                    IdiomaId = int.TryParse(row.GetValueOrDefault("idioma_id"), out var iid) ? iid : idiomaId
                });
            }
            return cursos;
        }

        public async Task<Dictionary<string, string>> GetRandomTestAsync(int cursoId)
        {
            var resp = await _httpClient.GetAsync($"/api/test?cursoId={cursoId}");
            if (!resp.IsSuccessStatusCode)
            {
                return new Dictionary<string, string>
                {
                    ["id"] = "0",
                    ["titulo"] = string.Empty,
                    ["curso_id"] = cursoId.ToString()
                };
            }
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return data ?? new Dictionary<string, string>
            {
                ["id"] = "0",
                ["titulo"] = string.Empty,
                ["curso_id"] = cursoId.ToString()
            };
        }

        public async Task<List<EjercicioDto>> GetExercisesAsync(int testId)
        {
            var list = new List<EjercicioDto>();
            var resp = await _httpClient.GetAsync($"/api/exercises?testId={testId}");
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            if (data == null) return list;
            foreach (var row in data)
            {
                list.Add(new EjercicioDto
                {
                    ejercicioId = row.GetValueOrDefault("ejercicioId") ?? string.Empty,
                    tipo = row.GetValueOrDefault("tipo") ?? string.Empty,
                    puntos = row.GetValueOrDefault("puntos") ?? "0",
                    contenido = row.GetValueOrDefault("contenido") ?? string.Empty,
                    respuesta = row.GetValueOrDefault("respuesta") ?? string.Empty,
                    opciones = row.GetValueOrDefault("opciones")
                });
            }
            return list;
        }

        public async Task<bool> SavePointsAsync(int usuarioId, int idiomaId, int puntos)
        {
            var payload = new Dictionary<string, int>
            {
                ["usuarioId"] = usuarioId,
                ["idiomaId"] = idiomaId,
                ["puntos"] = puntos
            };
            var resp = await _httpClient.PostAsJsonAsync("/api/puntos", payload);
            if (!resp.IsSuccessStatusCode) return false;
            var data = await resp.Content.ReadFromJsonAsync<Dictionary<string, string>>();
            return string.Equals(data?.GetValueOrDefault("status"), "ok", System.StringComparison.OrdinalIgnoreCase);
        }

        // Obtener histórico de puntuaciones
        public async Task<List<PuntuacionItem>> GetPuntuacionesAsync(int? usuarioId = null, int? idiomaId = null)
        {
            var list = new List<PuntuacionItem>();
            var url = "/api/puntuaciones";
            var qs = new List<string>();
            if (usuarioId.HasValue) qs.Add($"usuarioId={usuarioId.Value}");
            if (idiomaId.HasValue) qs.Add($"idiomaId={idiomaId.Value}");
            if (qs.Count > 0) url += "?" + string.Join("&", qs);
            var resp = await _httpClient.GetAsync(url);
            if (!resp.IsSuccessStatusCode) return list;
            var data = await resp.Content.ReadFromJsonAsync<List<Dictionary<string, string>>>();
            if (data == null) return list;
            foreach (var row in data)
            {
                list.Add(new PuntuacionItem
                {
                    UsuarioId = int.TryParse(row.GetValueOrDefault("usuario_id"), out var uid) ? uid : 0,
                    IdiomaId = int.TryParse(row.GetValueOrDefault("idioma_id"), out var iid) ? iid : 0,
                    Puntos = int.TryParse(row.GetValueOrDefault("puntos"), out var p) ? p : 0,
                    Fecha = row.GetValueOrDefault("fecha") ?? string.Empty,
                    Usuario = row.GetValueOrDefault("usuario") ?? string.Empty,
                    Idioma = row.GetValueOrDefault("idioma") ?? string.Empty,
                });
            }
            return list;
        }
    }
}
