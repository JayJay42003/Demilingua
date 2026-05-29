// wwwroot/js/site.js

// --- Dark mode toggle ---
(function() {
    var toggle = document.getElementById('darkToggle');
    var icon = document.getElementById('darkIcon');
    if (!toggle || !icon) return;
    var isDark = localStorage.getItem('theme') === 'dark';
    if (isDark) { document.documentElement.setAttribute('data-theme', 'dark'); icon.className = 'bi bi-sun-fill'; }
    toggle.addEventListener('click', function() {
        var dark = document.documentElement.getAttribute('data-theme') === 'dark';
        document.documentElement.setAttribute('data-theme', dark ? '' : 'dark');
        icon.className = dark ? 'bi bi-moon-fill' : 'bi bi-sun-fill';
        localStorage.setItem('theme', dark ? 'light' : 'dark');
    });
})();

// --- Bootstrap tooltips ---
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function(el) {
        new bootstrap.Tooltip(el);
    });
});

// --- Global toast ---
function showGlobalToast(msg, type) {
    type = type || 'info';
    var container = document.getElementById('globalToastContainer');
    if (!container) return;
    var id = 'gt-' + Date.now();
    container.insertAdjacentHTML('beforeend',
        '<div id="' + id + '" class="toast align-items-center text-bg-' + type + ' border-0" role="alert">' +
        '<div class="d-flex"><div class="toast-body">' + msg + '</div>' +
        '<button class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button></div></div>');
    var el = document.getElementById(id);
    var toast = new bootstrap.Toast(el, { delay: 3000 });
    toast.show();
    el.addEventListener('hidden.bs.toast', function() { el.remove(); });
}

// --- Confirm modal ---
function showConfirm(msg, callback) {
    document.getElementById('confirmMsg').textContent = msg;
    var modal = new bootstrap.Modal(document.getElementById('confirmModal'));
    document.getElementById('confirmOk').onclick = function() {
        modal.hide();
        if (callback) callback();
    };
    modal.show();
}

// --- Daily goal ---
(function() {
    var goalBox = document.getElementById('dailyGoalBox');
    if (!goalBox) return;
    var goal = parseInt(localStorage.getItem('dailyGoal') || '50');
    var textEl = document.getElementById('dailyGoalText');
    if (textEl) textEl.textContent = goal;
    var today = new Date().toDateString();
    var stored = JSON.parse(localStorage.getItem('dailyXP') || '{"date":"","xp":0}');
    if (stored.date !== today) { stored = { date: today, xp: 0 }; localStorage.setItem('dailyXP', JSON.stringify(stored)); }
    var pct = Math.min(Math.round(stored.xp / goal * 100), 100);
    var bar = document.getElementById('dailyGoalBar');
    if (bar) bar.style.width = pct + '%';
})();
function setDailyGoal() {
    var g = prompt('Cuanto XP quieres ganar hoy?', localStorage.getItem('dailyGoal') || '50');
    if (g && parseInt(g) > 0) { localStorage.setItem('dailyGoal', g); location.reload(); }
}
function addDailyXP(xp) {
    var stored = JSON.parse(localStorage.getItem('dailyXP') || '{"date":"","xp":0}');
    stored.xp += xp;
    localStorage.setItem('dailyXP', JSON.stringify(stored));
}

// --- Scroll to top button ---
(function() {
    var btn = document.getElementById('scrollTopBtn');
    if (!btn) return;
    window.addEventListener('scroll', function() {
        btn.style.display = window.scrollY > 400 ? 'flex' : 'none';
    });
})();
