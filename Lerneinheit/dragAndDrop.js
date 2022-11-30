
const cards = [...document.querySelectorAll('.draggable-card')];

const dropFields = [...document.querySelectorAll('.drop-field')];

let draggedId;
let answersMap = new Map([
    ['drop-1', null],
    ['drop-2', null],
    ['drop-3', null],
]);

function onDragStart(event) {
    draggedId = event.target.id;
    event.dataTransfer.setData("text", event.target.id);
}

function onDragEnd(event) {
}

function onDrop(event) {
    event.preventDefault();
    const target = event.target.id;
    if (target.split('-').at(0) === draggedId.split('-').at(0)) { return; }
    if (target !== draggedId) {
        if (isCorrect(event)) {
            event.target.style.background = '#6ceb75';
            answersMap.set(event.target.id, true);
        } else {
            event.target.style.background = '#d51c00';
        }
    }
    let data = event.dataTransfer.getData("text");
    event.target.appendChild(document.getElementById(data));
}

function onDragEnter(event) {
}

function onDragLeave(event) {
    event.target.style.background = '';
}

function onDragOver(event) {
    event.preventDefault();
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
}

addEventListeners();

function save() {
    for (const [key, value] of answersMap.entries()) {
        if (value === true) {
            document.getElementById("" + key).style.background = '#6ceb75';
        } else {
            document.getElementById("" + key).style.background = '#d51c00';
        }
    }
}