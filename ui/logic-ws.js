let sessionId = null;
let socket = null;

function api(path) {
    const base = CONFIG.API_HTTP_BASE_URL
        .replace(/\/$/, "");
    return `${base}${path}`;
}

function wsApi(path) {
    const base = CONFIG.API_WS_BASE_URL
        .replace(/\/$/, "");
    return `${base}${path}`;
}

function createGame() {
    fetch(api('/sessions'), { method: 'POST' })
        .then(handleError)
        .then(r => r.json())
        .then(data => {
            sessionId = data.id || data.sessionId;
            document.getElementById('startBtn').disabled = false;

            loadState(); // initial state
            connectWebSocket();
        })
        .catch(err => console.error(err));
}

function startSimulation() {
    fetch(api(`/sessions/${sessionId}/simulate`), { method: 'POST' })
        .then(handleError)
        .catch(err => console.error(err));
}

function connectWebSocket() {
    if (socket) return;

    socket = new WebSocket(wsApi(`/sessions/${sessionId}`));

    socket.onmessage = event => {
        console.log("WebSocket receive data");
        const data = JSON.parse(event.data);
        renderState(data);
    };

    socket.onclose = () => {
        console.log("WebSocket closed");
        socket = null;
    };

    socket.onerror = err => {
        console.error("WebSocket error", err);
    };
}

function disconnectWebSocket() {
    if (socket) {
        socket.close();
        socket = null;
    }
}

function loadState() {
    fetch(api(`/sessions/${sessionId}`))
        .then(handleError)
        .then(r => r.json())
        .then(renderState)
        .catch(err => console.error(err));
}

function handleError(response) {
    if (!response.ok) {
        return response.json().then(data => {
            const msg = data.message;
            showError(`Error ${data.status}: ${msg}`);
            throw new Error(msg);
        });
    }
    return response;
}

function showError(message) {
    const errorEl = document.getElementById('errorMessage');
    errorEl.textContent = message;
    errorEl.classList.add('show');

    setTimeout(() => {
        errorEl.classList.remove('show');
    }, 5000);
}

function renderState(data) {
    renderBoard(data.game.state);
    renderStatus(data.game.status);
    renderMoves(data.moves);

    if (data.game.status !== 'IN_PROGRESS') {
        disconnectWebSocket();
    }
}

function renderBoard(state) {
    const board = document.getElementById('board');
    board.innerHTML = '';

    for (let i = 0; i < 9; i++) {
        const cell = document.createElement('div');
        cell.className = 'cell';
        cell.textContent = state[i] === '_' ? '' : state[i];
        board.appendChild(cell);
    }
}

function renderStatus(status) {
    document.getElementById('status').textContent = 'Status: ' + status;
}

function renderMoves(moves) {
    const ul = document.getElementById('moves');
    ul.innerHTML = '';
    moves.forEach((m, i) => {
        const li = document.createElement('li');
        li.textContent = `${i + 1}. ${m.symbol} → ${m.position}`;
        ul.appendChild(li);
    });
}
