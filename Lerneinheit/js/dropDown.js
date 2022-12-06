
const dropdown = document.querySelectorAll('.dropdown');
dropdown.forEach(function (item) {
    item.addEventListener('click', function () {
        item.classList.toggle("active")
    })
});

function show(value, index) {
    if (value === 'right') {
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-right.png' );
    } else if (value === 'left') {
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-left.png' );
    }
}