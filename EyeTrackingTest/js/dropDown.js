
const dropdown = document.querySelectorAll('.dropdown');
const textBox = document.querySelectorAll('.text-box');

dropdown.forEach(function (item) {
    item.addEventListener('click', function () {
        item.classList.toggle("active")
    })
});

function show(element) {

    let value = element.getAttribute('direction');
    let index = element.getAttribute('number');
    let isCorrect = element.getAttribute('isCorrect');
    let temp = (isCorrect === 'true') ? 'true' : 'false'

    textBox.item(index-1).setAttribute('isCorrect', temp);
    document.getElementById("text-box-" + index)
        .setAttribute("style", "background-color: #fffff")

    if (value === 'right') {
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-right.png');
    } else if (value === 'left') {
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-left.png');
    }
}

function checkAnswer() {
    let count = 0;
    attempts++
    textBox.forEach(function (value) {
            if (!value.hasAttribute('src')) {
                value.setAttribute("style", "background-color: #d51c00");
            } else if (value.hasAttribute('isCorrect')){
                if (value.getAttribute('isCorrect') === 'true') {
                    value.setAttribute("style", "background-color: #6ceb75");
                    count++
                } else {
                    value.setAttribute("style", "background-color: #d51c00");
                }
                if (count === textBox.length) {
                    enableNextButton();
                    document.getElementById('dropdown-save-button').disabled = true;
                    window.javaBridge.saveFeedback("Drop-down task",getDateTime(),attempts,"Done");
                    attempts = 0;
                }
            }
        })
}