const gridCount = 5;
const clicksToFinishPoint = 1;
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

var body = document.body,
    html = document.documentElement;
var debug = document.getElementById("debug");
var canvas = document.getElementById("draw_canvas");
const context = canvas.getContext('2d');
canvas.width = window.innerWidth*0.9;
canvas.height = window.innerHeight;
context.globalCompositeOperation='destination-over';
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
        console.log(clickedArray[pointID]);
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
        console.log(sum)
    })
    if (sum <= 0) {
        document.getElementById("callibration-box").remove();
        let waitingElement = document.createElement("div");
        waitingElement.setAttribute("id", "waiting-box");
        waitingElement.classList.add("modal");
        waitingElement.style.cssText += "display:block;";
        let loadingSymbol = document.createElement("div");
        loadingSymbol.classList.add("loading-symbol");
        loadingSymbol.id = "loading-screen";
        waitingElement.appendChild(loadingSymbol)
        // var canvas = document.getElementById("circles");
        // canvas.style.cssText += "display:block";
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
            var normalizedTopPosition = (topPos) / (displayHeight - yOffset * 2) * (displayHeight - yOffset * 2) + yOffset;
            var normalizedLeftPosition = (leftPos) / (displayWidth - xOffset * 2) * (displayWidth - xOffset * 2) + xOffset;
            drawCallibrationPoint(normalizedTopPosition, normalizedLeftPosition, id);
            id++;
        }
    }
}

function setBubblePosition(parent, bubble) {
    let rect = parent.getBoundingClientRect();
    bubble.style.top = rect.top + rect.height * .2;
    bubble.style.left = rect.right;
    bubble.style.maxHeight = rect.height * .5;
}

function linkHelpButtons() {
    let aoiHelpButtons = document.querySelectorAll("button[helpForAoI]");

    for (let aoiHelpButton of aoiHelpButtons) {
        if (typeof aoiHelpButton.onclick != "function") {
            aoiHelpButton.onclick = function (event) {
                var source = event.target;
                aoiKey = source.getAttribute('helpforAoI');
                let aoiHelpSections = document.querySelectorAll("[helpTextForAoI=\'" + aoiKey + "\']");
                for (let aoiHelpSection of aoiHelpSections) {
                    let aoi = document.querySelector("[aoi=\'" + aoiKey + "\']");
                    setBubblePosition(aoi.parentNode, aoiHelpSection);
                    if (aoiHelpSection.style.display === "block") {
                        aoiHelpSection.style.display = "none";
                    } else if (aoiHelpSection.style.display == "") {
                        aoiHelpSection.style.display = "block";
                    } else {
                        aoiHelpSection.style.display = "block";
                    }
                }
            }
        }
    }
}


var waiting = document.createElement('div');
waiting.classList.add("modal")
document.body.appendChild(waiting);
linkHelpButtons()
drawCallibrationPoints()


function fitDone() {
    let loading_screen = document.getElementById("waiting-box")
    loading_screen.style.cssText += "display:none;";
}

function drawPointForDemo(x, y, scroll) {
    const radius = 10;
    canvas.height = Math.max(body.getBoundingClientRect().height, html.getBoundingClientRect().height);
    debug.innerText= "X: " + x + ", Y:" + y +", Scroll: "+scroll+"\n Height: "+canvas.height;
    context.beginPath();
    context.arc(x, (y+scroll), radius, 0, 2 * Math.PI, false);
    context.fillStyle = 'rgba(0,252,0,0.5)';
    context.fill();
    context.lineWidth = 5;
    context.strokeStyle = 'rgba(0,107,0,0.50)';
    context.stroke();
}


function getElementFromPosition(x, y) {
    let doc = document.documentElement;
    let element = document.elementFromPoint(x, y);
    var scroll_top = (window.scrollY || doc.scrollTop)  - (doc.clientTop || 0);
    drawPointForDemo(x, y, scroll_top);
    if (element.tagName === "BODY") {
        return
    }
    let old_color;
    if (element.style.backgroundColor !== "red") {
        old_color = element.style.backgroundColor;
    }
    element.style.backgroundColor = "red";
    element.style.backgroundColor = old_color;
    return element;
}


function showHelpForAoI(key) {
    let aoiHelpList = document.querySelectorAll('[helpForAoI= \'' + key + '\']');
    for (let aoiHelp of aoiHelpList) {
        aoiHelp.style.display = "block";
    }
    return aoiHelpList;
}
