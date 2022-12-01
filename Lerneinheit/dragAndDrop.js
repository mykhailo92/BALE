
const cards = [...document.querySelectorAll('.draggable-card')];

const dropFields = [...document.querySelectorAll('.drop-field')];

const dropStarts = [...document.querySelectorAll('.drop-start')];

let answersMap = new Map([
    ['drop-1', false],
    ['drop-2', false],
    ['drop-3', false],
]);

let draggedId;

function onDragStart(event) {
    draggedId = event.target.id;
    event.dataTransfer.setData("text", draggedId);
}

function onDragEnd(event) {
}

function onDrop(event) {
    event.preventDefault();
    const target = event.target.id;

    if (target.split('-').at(0) === draggedId.split('-').at(0)) {
        return;
    }
    else if (event.target.className === 'drop-start') {
        event.target.style.background = '';
    }
    else if (target !== draggedId) {
        if (isCorrect(event)) {
            event.target.style.background = '#6ceb75';
            answersMap.set(target, true);
        } else {
            event.target.style.background = '#d51c00';
            answersMap.set(target, false);
        }
    }
    let data = event.dataTransfer.getData("text");
    event.target.appendChild(document.getElementById(data));
}

function onDragEnter(event) {
}

function onDragLeave(event) {
   // event.target.style.background = '';
}

function onDragOver(event) {
    if (!event.target.hasChildNodes()) {
        event.preventDefault();
    }
}

function isCorrect(event) {
    return draggedId.split('-').at(1) === event.target.id.split('-').at(1);
}

function addEventListeners() {
    cards.forEach((card) => {
        card.addEventListener('dragstart', onDragStart);
        card.addEventListener('dragend', onDragEnd);
    });
    dropFields.forEach((dropField) =>{
        dropField.addEventListener('drop', onDrop);
        dropField.addEventListener('dragenter', onDragEnter);
        dropField.addEventListener('dragleave', onDragLeave);
        dropField.addEventListener('dragover', onDragOver);
    });
    dropStarts.forEach((dropStart) => {
        dropStart.addEventListener('drop', onDrop);
        dropStart.addEventListener('dragover', allowDrop);
    });}

addEventListeners();

function allowDrop(event) {
    event.preventDefault();
}

function save() {
    for (const [key, value] of answersMap.entries()) {
        if (value === true && document.getElementById(key).hasChildNodes()) {
            document.getElementById(key).style.background = '#6ceb75';
        } else {
            document.getElementById(key).style.background = '#d51c00';
        }
    }
}