const gridCount = 6;
const clicksToFinishPoint = 1;
const viewportHeight = window.innerHeight;
const viewportWidth = window.innerWidth;
const xOffset = viewportWidth * 0.1;
const yOffset = viewportHeight * 0.1;
const displayHeight = viewportHeight - yOffset;
const displayWidth = viewportWidth - xOffset;
const clickedArray = []

for (let index = 0; index < gridCount * gridCount; index++) {
    clickedArray[index] = clicksToFinishPoint;
}

function drawCallibrationPoint(top, left, id) {
    let circle = document.createElement('div');
    circle.classList.add("circle");
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
        window.javaBridge.callibrationDone();
    }
}

function drawCallibrationPoints() {
    const y_step_per_grid = displayHeight / gridCount;
    const x_step_per_grid = displayWidth / gridCount;
    // document.getElementById("test").innerText += "" +
    //     "ViewHeight: " + viewportHeight + "\n" +
    //     "ViewWidth: " + viewportWidth + "\n" +
    //     "STEP X: " + x_step_per_grid + "\n" +
    //     "STEP Y: " + y_step_per_grid + "\n" +
    //     "X Offset: " + x_offset + "\n" +
    //     "Y Offset: " + y_offset + "\n" +
    //     "display x: " + displayWidth + "\n" +
    //     "display y: " + displayHeight + "\n"
    let id = 0
    for (let grid_index_x = 0; grid_index_x < gridCount; grid_index_x++) {
        for (let grid_index_y = 0; grid_index_y < gridCount; grid_index_y++) {
            top_pos = yOffset + (grid_index_y) * y_step_per_grid;
            left_pos = xOffset + (grid_index_x) * x_step_per_grid;
            // document.getElementById("test").innerText += top_pos + "  " + left_pos + "\n"
            drawCallibrationPoint(top_pos, left_pos, id);
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
