
let audio = new Audio();

function changeBgColor() {
    document.body.style.backgroundColor = document.getElementById('color').value;
    document.getElementsByTagName('aufgabe');
    return 'test';
}

/**
 * @return: All Sections in the current HTML
 */
function getContainer() {
    return document.querySelectorAll(".section,.info-and-slide,.diashow");
}

function getSlides() {
    return document.querySelectorAll(".slide");
}

// document.querySelector('.submit').onclick = getInput;
document.querySelector('.submit').addEventListener('click', getInputText);

function getInputText() {
    document.getElementById('debug').textContent = document.querySelector('.text-area').value;
    return document.querySelector('.text-area').value;
}

async function scrollToBottom() {
    let current = document.body.scrollTop + window.innerHeight;
    let target = document.body.scrollHeight;
    let delta = target - current;
    const duration = 300;
    const steps = duration / 10;
    const stepSize = delta / steps;

    for (let i = 0; i < steps; i++) {
        window.scrollBy(0, stepSize);
        await sleep(duration / steps);
    }
}

function sleep(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
}

function getChapter() {
    return document.querySelectorAll(".chapter");
}

function getElementHeightByID(id) {
    return document.getElementById(id).offsetHeight;
}

function changeInnerTextById(id, text) {
    document.getElementById(id).innerText = text;
}

function playAudio(index) {
    if (audio.autoplay) {
        audio.pause(); audio.currentTime = 0; audio.autoplay = false;
            document.getElementById('reading-button-0').innerText = "Vorlesen";
            document.getElementById('reading-button-1').innerText = "Vorlesen";
            document.getElementById('reading-button-2').innerText = "Vorlesen";
            document.getElementById('reading-button-3').innerText = "Vorlesen";
            document.getElementById('reading-button-4').innerText = "Vorlesen";
    } else {
        switch (index) {
            case 0: audio.src = 'audio/task_0.mp3';
                    audio.autoplay = true;
                    audio.currentTime = 0;
                    document.getElementById('reading-button-0').innerText = "Vorlesen stoppen"; break
            case 1: audio.src = 'audio/task_1.1.mp3';
                    audio.autoplay = true;
                    audio.currentTime = 0;
                    document.getElementById('reading-button-1').innerText = "Vorlesen stoppen"; break
            case 2: audio.src = 'audio/task_1.2.mp3';
                    audio.autoplay = true;
                    audio.currentTime = 0;
                    document.getElementById('reading-button-2').innerText = "Vorlesen stoppen"; break
            case 3: audio.src = 'audio/task_3.mp3';
                    audio.autoplay = true;
                    audio.currentTime = 0;
                    document.getElementById('reading-button-3').innerText = "Vorlesen stoppen"; break
            case 4: audio.src = 'audio/task_4.mp3';
                    audio.autoplay = true;
                    audio.currentTime = 0;
                    document.getElementById('reading-button-4').innerText = "Vorlesen stoppen"; break
        }
    }
}
function getControlLabels() {
    return document.querySelectorAll(".reading,.save,.preamble-button");
}
