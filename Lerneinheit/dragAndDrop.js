function allowDrop(event) {
    event.preventDefault();
}

function drag(event) {
    event.dataTransfer.setData("text", event.target.id);
}

function drop(event) {
    event.preventDefault();
    let data = event.dataTransfer.getData("text");
    event.target.appendChild(document.getElementById(data));
}

function onDragStart(event) {

}

function onDragEnd() {

}

function onDrop() {

}

function onDragEnter() {

}

function onDragLeave() {

}

function onDragOver() {

}

function resetCards() {

}

function addEventListeners() {
    const resetButton = document.querySelector( '');
    const cards = document.querySelector('.draggable-card');
    const dropField = document.querySelector('.drop-field')

    cards.addEventListener('dragstart', onDragStart);
    cards.addEventListener('dragend', onDragEnd);
    dropField.addEventListener('drop', onDrop);
    dropField.addEventListener('dragenter', onDragEnter);
    dropField.addEventListener('dragleave', onDragLeave);
    dropField.addEventListener('dragover', onDragOver);
    resetButton.addEventListener('click', resetCards);
}