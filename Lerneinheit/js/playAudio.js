
let audio = new Audio();
let currentTrack;

let buttons = document.querySelectorAll('.reading');

function playAudio(reading) {
    let index = reading.getAttribute("track-number");
    if (audio.autoplay && index === currentTrack) {
        stopAudioPlaying(reading);
    } else if (audio.autoplay && index !== currentTrack) {
        return;
    } else {
        startAudioPlaying(index);
        window.javaBridge.saveFeedback(reading.getAttribute('description'),getDateTime(),0,"Reading started");
    }

    audio.addEventListener("ended", function () {
        audio.autoplay = false;
        document.getElementById('reading-button-' + index).innerText = "Vorlesen";
        buttons.forEach(elem => elem.disabled = false);
        enableNextButton();
    });
}

function startAudioPlaying(index) {
    let delayInMilliseconds = 200;

    setTimeout(function() {
        audio.src = 'audio/task_' + index + '.mp3';
        audio.autoplay = true;
        audio.currentTime = 0;
        currentTrack = index;
        document.getElementById('reading-button-' + index).innerText = "Vorlesen stoppen";
    }, delayInMilliseconds);

    audio.addEventListener("play", function () {
        let buttons = document.querySelectorAll('.reading');
        buttons.forEach(elem => elem.disabled = true);
        document.getElementById('reading-button-' + index).disabled = false;
        disableNextButton();
    });
}

function stopAudioPlaying(elem) {
    let delayInMilliseconds = 200;
    setTimeout(function() {
        audio.pause();
        audio.currentTime = 0;
        audio.autoplay = false;
        document.getElementById('reading-button-' + currentTrack).innerText = "Vorlesen";
        buttons.forEach(elem => elem.disabled = false);
        enableNextButton();
        window.javaBridge.saveFeedback(elem.getAttribute('description'), getDateTime(), 0, "Reading stopped");
    }, delayInMilliseconds);
}