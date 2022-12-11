
let audio = new Audio();
let currentTrack;

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

function getControlLabels() {
    return document.querySelectorAll(".reading,.save,.preamble-button");
}

function changeInnerTextById(id, text) {
    document.getElementById(id).innerText = text;
}

function playAudio(reading) {
    let index = reading.getAttribute("track-number");
    if (audio.autoplay && index === currentTrack) {
        stopAudioPlaying();
    } else if (audio.autoplay && index !== currentTrack) {
        return;
    } else {
        startAudioPlaying(index);
    }
    audio.addEventListener("ended", function () {
        audio.autoplay = false;
        document.getElementById('reading-button-' + index).innerText = "Vorlesen";
    });
}

function startAudioPlaying(index) {
    let delayInMilliseconds = 200;
    setTimeout(function() {
        audio.src = 'audio/task_' + index + '.mp3';
        audio.autoplay = true;
        audio.currentTime = 0;
        currentTrack = index;
        document.getElementById('reading-button-' + index).innerText = "Vorlesen stoppen";},
        delayInMilliseconds);
}

function stopAudioPlaying() {
    let delayInMilliseconds = 200;
    setTimeout(function() {
            audio.pause();
            audio.currentTime = 0;
            audio.autoplay = false;
            document.getElementById('reading-button-' + currentTrack).innerText = "Vorlesen";},
        delayInMilliseconds);
}

let modal = document.getElementById("modal-window");
let span = document.getElementsByClassName("close")[0];

function showVideo(video) {
    modal.style.display = "block";
    let number = video.getAttribute('track-number');
    let videoElem = document.getElementById('video-' + number);
    videoElem.setAttribute('src', 'video/video-' + number + '.mp4');

    let delayInMilliseconds = 250;
    setTimeout(function() {
            videoElem.setAttribute('autoplay', 'autoplay');},
        delayInMilliseconds);
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside the modal, close it
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

function enableNextButton() {
    window.javaBridge.setNextButtonDisabled(false);
}