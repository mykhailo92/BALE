
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

function saveText (element) {
    let number = element.getAttribute('number');
    let target = document.getElementById("text-area-" + number);
    let text = target.value;

    if (text.length > 0) {
        enableNextButton();
        target.disabled = true;
        element.disabled = true;
    } else {
        target.placeholder = "Schreibe bitte deine Antwort hier mit der Tastatur um weiterzugehen";
    }
}

function showVideo(video) {
    let number = video.getAttribute('track-number');
    let modal = document.getElementById("modal-window-" + number);
    let span = document.getElementsByClassName("close")[number-1];
    let videoElem = document.getElementById('video-' + number);
    modal.style.display = "block";
    videoElem.setAttribute('src', 'video/video-' + number + '.mp4');
    videoElem.setAttribute('autoplay', 'autoplay');

    /* When the user clicks on <span> (x), close the modal */
    span.onclick = function() {
        modal.style.display = "none";
    }

    /* When the user clicks anywhere outside the modal, close it */
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
}

function enableNextButton() {
    window.javaBridge.setNextButtonDisabled(false);
}

function disableNextButton() {
    window.javaBridge.setNextButtonDisabled(true);
}