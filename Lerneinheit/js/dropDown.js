
const dropdown = document.querySelectorAll('.dropdown');
dropdown.forEach(function (item) {
    item.addEventListener('click', function () {
        item.classList.toggle("active")
    })
});

function show(value, index) {
    if (value === 'right') {
        let img = document.createElement('img');
        img.src = 'icons/arrow-right.png';
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-right.png' );
    } else if (value === 'left') {
        let img = document.createElement('img');
        img.src = 'icons/arrow-left.png';
        document.getElementById("text-box-" + index)
            .setAttribute('src','icons/arrow-left.png' );
    }
}