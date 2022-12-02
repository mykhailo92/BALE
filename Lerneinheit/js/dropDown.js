
const dropdown = document.querySelectorAll('.dropdown');
dropdown.forEach(function (item) {
    item.addEventListener('click', function () {
        item.classList.toggle("active")
    })
});

function show(value, index) {
    if (value === 'right') {
        switch (index) {
            case 1: document.getElementById("text-box-" + index).value = '————————————————————>'; break
            case 2: document.getElementById("text-box-" + index).value = '————————————————————>';
        }
    } else if (value === 'left') {
        switch (index) {
            case 1: document.getElementById("text-box-" + index).value = '<————————————————————'; break
            case 2: document.getElementById("text-box-" + index).value = '<————————————————————';
        }
    }
}