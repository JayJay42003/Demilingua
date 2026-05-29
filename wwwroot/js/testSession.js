// wwwroot/js/testSession.js

let ejercicios = [];
let indice = 0;
let puntuacionTotal = 0;
let vidas = 3;
let testId = 0;
let resultados = []; // {contenido, correcta, usuario, acertado}

document.addEventListener("DOMContentLoaded", async () => {
    try {
        const respTest = await fetch(`/Test/GetRandomTest?cursoId=${cursoId}`);
        const testData = await respTest.json();

        if (!testData.id || testData.id == "0") { throw new Error("No hay tests disponibles"); }

        testId = parseInt(testData.id);
        
        document.getElementById("lblTitulo").innerText = testData.titulo;

        const respEj = await fetch(`/Test/GetExercises?testId=${testData.id}`);
        ejercicios = await respEj.json();

        if (ejercicios.length === 0) { throw new Error("Este test no tiene ejercicios"); }

        document.getElementById("loader").style.display = "none";
        document.getElementById("cardPregunta").style.display = "flex"; // flex for full height
        mostrarEjercicio();

    } catch (error) {
        Swal.fire('Error', error.message, 'error').then(() => {
            window.location.href = `/Courses?idiomaId=${idiomaId}`;
        });
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        var card = document.getElementById('cardPregunta');
        if (!card || card.style.display === 'none') return;
        var ops = document.querySelectorAll('#opcionesContainer .btn');
        if (ops.length > 0) {
            var keys = ['1','2','3','4'];
            var idx = keys.indexOf(e.key);
            if (idx >= 0 && idx < ops.length) { ops[idx].click(); return; }
        }
        if (e.key === 'Enter') {
            var vis = document.getElementById('etRespuestaVisible');
            if (vis && vis.style.display !== 'none') {
                document.getElementById('btnSiguiente').click();
            }
        }
        if (e.key === 'Escape') {
            if (confirm('Salir del test?')) window.location.href = '/Courses/Index?idiomaId=' + idiomaId;
        }
    });
});

function mostrarEjercicio() {
    if (indice >= ejercicios.length) {
        finalizarTest();
        return;
    }

    const ej = ejercicios[indice];
    document.getElementById("tvContenido").innerText = ej.contenido;
    
    // Progress bar
    const progressPercent = ((indice) / ejercicios.length) * 100;
    document.getElementById("progressBar").style.width = `${progressPercent}%`;

    const opcionesContainer = document.getElementById("opcionesContainer");
    const inputContainer = document.getElementById("inputContainer");
    const micContainer = document.getElementById("micContainer");
    
    opcionesContainer.innerHTML = '';
    opcionesContainer.style.display = "none";
    inputContainer.style.display = "none";
    micContainer.style.display = "none";

    document.getElementById("btnSiguiente").style.display = "block";
    document.getElementById("btnSiguiente").onclick = validarYContinuar;

    if (ej.tipo === "opcion_multiple" && ej.opciones) {
        try {
            const ops = JSON.parse(ej.opciones);
            opcionesContainer.style.display = "block";
            document.getElementById("btnSiguiente").style.display = "none"; // Hide standard next button, click option instead
            
            ops.forEach(op => {
                const btn = document.createElement("button");
                btn.className = "btn btn-outline-primary btn-lg w-100 mb-2 p-3 text-start";
                btn.innerText = op;
                btn.onclick = () => {
                    document.getElementById("etRespuesta").value = op;
                    validarYContinuar();
                };
                opcionesContainer.appendChild(btn);
            });
        } catch (e) {
            console.error("Error parsing options", e);
        }
    } else if (ej.tipo === "pronunciacion") {
        micContainer.style.display = "block";
        document.getElementById("btnSiguiente").style.display = "none"; // Wait for speech
        
        const btnMic = document.getElementById("btn-mic");
        btnMic.onclick = iniciarReconocimientoVoz;
        document.getElementById("micStatus").innerText = "Haz clic en el micrófono para hablar";
    } else {
        // traduccion, completar
        inputContainer.style.display = "block";
        document.getElementById("etRespuesta").value = "";
        document.getElementById("etRespuestaVisible").value = "";
        document.getElementById("etRespuestaVisible").focus();
    }
}

function iniciarReconocimientoVoz() {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
        Swal.fire('Error', 'Tu navegador no soporta reconocimiento de voz.', 'error');
        return;
    }
    
    const recognition = new SpeechRecognition();
    // Default es-ES, if you have language context you can change this
    recognition.lang = 'es-ES'; 
    
    const micStatus = document.getElementById("micStatus");
    const btnMic = document.getElementById("btn-mic");
    
    recognition.onstart = function() {
        micStatus.innerText = "Escuchando...";
        btnMic.classList.replace("btn-outline-danger", "btn-danger");
    };
    
    recognition.onresult = function(event) {
        const transcript = event.results[0][0].transcript;
        document.getElementById("etRespuesta").value = transcript;
        micStatus.innerText = `Escuchado: "${transcript}"`;
        setTimeout(() => validarYContinuar(), 1000);
    };
    
    recognition.onerror = function(event) {
        micStatus.innerText = "Error al escuchar, intenta de nuevo.";
        btnMic.classList.replace("btn-danger", "btn-outline-danger");
    };
    
    recognition.onend = function() {
        btnMic.classList.replace("btn-danger", "btn-outline-danger");
    };
    
    recognition.start();
}

async function validarYContinuar() {
    const ej = ejercicios[indice];
    const respuestaUsuario = document.getElementById("etRespuesta").value.trim().toLowerCase();
    const respuestaCorrecta = ej.respuesta.trim().toLowerCase();
    const puntosPregunta = parseInt(ej.puntos);

    if (respuestaUsuario === "") return;

    // Normalizar para ignorar acentos y puntuación
    const normalizar = (str) => str.normalize("NFD").replace(/[\u0300-\u036f]/g, "").replace(/[.,\/#!$%\^&\*;:{}=\-_`~()]/g,"").trim();

    if (normalizar(respuestaUsuario) === normalizar(respuestaCorrecta)) {
        puntuacionTotal += puntosPregunta;
        document.getElementById("lblPuntos").innerText = puntuacionTotal;
        mostrarToast('Correcto!', 'success');
        resultados.push({ contenido: ej.contenido, correcta: respuestaCorrecta, usuario: respuestaUsuario, acertado: true });
    } else {
        vidas = Math.max(vidas - 1, 0);
        actualizarCorazones();
        await fetch(`/Test/SubtractLife?usuarioId=${userId}`, { method: 'POST' });
        resultados.push({ contenido: ej.contenido, correcta: respuestaCorrecta, usuario: respuestaUsuario, acertado: false });
        
        mostrarToast(`Incorrecto. La respuesta era: ${respuestaCorrecta}`, 'danger');

        if (vidas <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Game Over',
                text: 'Te has quedado sin vidas.',
                confirmButtonText: 'Volver'
            }).then(() => {
                window.location.href = `/Courses/Index?idiomaId=${idiomaId}`;
            });
            return;
        }
    }

    indice++;
    setTimeout(mostrarEjercicio, 2000);
}

function actualizarCorazones() {
    const container = document.getElementById("corazonesContainer");
    container.innerHTML = '';
    for(let i=0; i<3; i++) {
        const iElement = document.createElement("i");
        iElement.className = i < vidas ? "bi bi-heart-fill text-danger mx-1 fs-4" : "bi bi-heart text-secondary mx-1 fs-4";
        if (i === vidas && vidas < 3) iElement.classList.add('heart-breaking');
        container.appendChild(iElement);
    }
}

function mostrarToast(mensaje, tipo) {
    const toastEl = document.getElementById('testToast');
    const toastBody = document.getElementById('testToastBody');
    
    toastEl.className = `toast align-items-center text-bg-${tipo} border-0 w-100`;
    toastBody.innerText = mensaje;
    
    const toast = new bootstrap.Toast(toastEl, { delay: 1500 });
    toast.show();
}

async function finalizarTest() {
    document.getElementById("cardPregunta").style.display = "none";
    document.getElementById("progressBar").style.width = "100%";
    
    if (typeof addDailyXP === 'function') addDailyXP(puntuacionTotal);
    
    var aciertos = resultados.filter(function(r) { return r.acertado; }).length;
    var total = resultados.length;
    
    const loader = document.getElementById("loader");
    loader.style.display = "block";
    loader.innerHTML = `
        <div class="text-center" style="max-width:500px;margin:0 auto;">
            <h1 class="display-1 text-success mb-3"><i class="bi bi-trophy-fill"></i></h1>
            <h2>Leccion Completada!</h2>
            <p class="lead"><span id="xpCounter">0</span> XP ganados</p>
            <p class="text-muted" id="resultSummary">${aciertos}/${total} aciertos</p>
            <div class="mt-3 text-start" style="max-height:200px;overflow-y:auto;">
                ${resultados.map(function(r, i) {
                    return '<div class="d-flex align-items-center mb-2 p-2 rounded ' + (r.acertado ? 'bg-success bg-opacity-10' : 'bg-danger bg-opacity-10') + '">' +
                        '<span class="me-2">' + (r.acertado ? '\u2705' : '\u274C') + '</span>' +
                        '<div><small class="fw-bold">' + r.contenido + '</small><br>' +
                        '<small class="text-muted">Respuesta: ' + r.correcta + '</small></div></div>';
                }).join('')}
            </div>
        </div>
    `;

    animateCounter("xpCounter", 0, puntuacionTotal, 1200);
    
    if (typeof confetti !== 'undefined') {
        confetti({ particleCount: 80, spread: 70, origin: { y: 0.6 } });
        setTimeout(function() { confetti({ particleCount: 40, spread: 90, origin: { y: 0.5 } }); }, 400);
    }

    await fetch('/Test/SubmitPoints', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usuarioId: userId, idiomaId: idiomaId, puntos: puntuacionTotal })
    });

    if (testId > 0) {
        await fetch(`/Test/CompleteTest?usuarioId=${userId}&testId=${testId}&puntuacion=${puntuacionTotal}`, { method: 'POST' });
    }
    await fetch(`/Test/UpdateStreak?usuarioId=${userId}&xp=${puntuacionTotal}`, { method: 'POST' });
    
    setTimeout(() => {
        window.location.href = `/Courses/Index?idiomaId=${idiomaId}`;
    }, 3000);
}

function animateCounter(id, from, to, duration) {
    const el = document.getElementById(id);
    if (!el) return;
    const start = performance.now();
    const range = to - from;
    function step(ts) {
        const elapsed = ts - start;
        const progress = Math.min(elapsed / duration, 1);
        el.textContent = Math.floor(from + range * progress);
        if (progress < 1) requestAnimationFrame(step);
    }
    requestAnimationFrame(step);
}