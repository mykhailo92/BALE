function changeBgColor() {
    document.body.style.backgroundColor = document.getElementById('color').value;
    document.getElementsByTagName('aufgabe');
    return 'test';
}

/**
 * @return: All Sections in the current HTML
 */
function getContainer() {
    return document.querySelectorAll(".section,.info_and_slide,.slide_vorschau");
}

function getSlides() {
    return document.querySelectorAll(".slide");
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