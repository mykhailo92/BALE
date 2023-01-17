function drawCircleAtPosition(x, y) {
    var canvas = document.getElementById("circles")
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    var ctx = canvas.getContext("2d");
    ctx.beginPath();
    ctx.arc(x, y, 30, 0, 2 * Math.PI);
    ctx.stroke();
}
