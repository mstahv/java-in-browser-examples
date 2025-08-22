import {tetris} from "./teavm/tetris.js"

window.addEventListener("load", () => {

    const client = tetris();

    const div = document.createElement("div");
    div.appendChild(document.createElement("p").appendChild(document.createTextNode("Game created")));
    document.body.appendChild(div);
    document.body.appendChild(client.wrapper);

    client.wrapper.addEventListener("game-over-event", (e) => {
        const gameOverEvent = e.detail;
        alert("Game Over! Score: " + gameOverEvent.score);
    });

    client.wrapper.addEventListener("game-state-event", (e) => {
        const gameStateEvent = e.detail;
        div.textContent = "Game State Updated: " + gameStateEvent.score;
    });



});
