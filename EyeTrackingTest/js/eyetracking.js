const gridCount = 8;
const clicksToFinishPoint = 2;
const viewportHeight = window.innerHeight;
const viewportWidth = window.innerWidth;
const xOffset = viewportWidth * 0.05;
const yOffset = viewportHeight * 0.05;
const displayHeight = viewportHeight;
const displayWidth = viewportWidth;
const clickedArray = []

for (let index = 0; index < gridCount * gridCount; index++) {
    clickedArray[index] = clicksToFinishPoint;
}

function drawCallibrationPoint(top, left, id) {
    let circle = document.createElement('div');
    circle.classList.add("callibration-circle");
    let cross = document.createElement("div")
    circle.style.cssText += "top:  " + top + "px;left:" + left + "px;";//transform: translate(-50%, -50%);";
    circle.setAttribute("pointID", id)
    cross.classList.add("cross");
    cross.innerText = "+";
    circle.addEventListener('click', function (event) {
        let pointID = this.getAttribute("pointID");
        if (clickedArray[pointID] > 0) {
            clickedArray[pointID] -= 1
        }
        if (clickedArray[pointID] <= 0) {
            this.style.cssText += "background-color:green;"
            checkIfCallibrationIsDone();
        }
        // document.getElementById("test").innerText += clickedArray[pointID];
    });
    circle.appendChild(cross);
    document.getElementById("callibration-box").appendChild(circle);
}

function checkIfCallibrationIsDone() {
    let sum = 0
    clickedArray.forEach((point) => {
        sum += point
    })
    if (sum <= 0) {
        document.getElementById("callibration-box").remove();
        let waitingElement = document.createElement("div");
        waitingElement.setAttribute("id", "waiting-box");
        waitingElement.classList.add("modal");
        waitingElement.style.cssText += "display:block;";
        let loadingSymbol = document.createElement("div");
        loadingSymbol.classList.add("loading-symbol");
        waitingElement.appendChild(loadingSymbol)
        var canvas = document.getElementById("circles");
        canvas.style.cssText += "display:block";
        document.body.prepend(waitingElement);
        window.javaBridge.callibrationDone();
    }
}

function drawCallibrationPoints() {
    const y_step_per_grid = displayHeight / gridCount;
    const x_step_per_grid = displayWidth / gridCount;
    let id = 0

    for (let grid_index_x = 0; grid_index_x < gridCount; grid_index_x++) {
        for (let grid_index_y = 0; grid_index_y < gridCount; grid_index_y++) {
            var topPos = (grid_index_y) * y_step_per_grid;
            var leftPos = (grid_index_x) * x_step_per_grid;
            var normalizedTopPosition = (topPos) / (displayHeight - yOffset * 2) * (displayHeight - yOffset*2) + yOffset;
            var normalizedLeftPosition = (leftPos) / (displayWidth - xOffset * 2) * (displayWidth - xOffset*2) + xOffset;
            drawCallibrationPoint(normalizedTopPosition, normalizedLeftPosition, id);
            id++;
        }
    }
}

var waiting = document.createElement('div');
waiting.classList.add("modal")
document.body.appendChild(waiting);

drawCallibrationPoints()

function drawCircleAtPosition(x, y) {
    var canvas = document.getElementById("circles");
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    var ctx = canvas.getContext("2d");
    ctx.beginPath();
    ctx.arc(x, y, 30, 0, 2 * Math.PI);
    ctx.stroke();
}
