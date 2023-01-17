import os
import sys

import cv2
import numpy as np
import pyautogui
from tensorflow.keras.layers import *
from tensorflow.keras.models import *
from tensorflow.keras.optimizers import *


def printToJava(text, type="PLAIN"):
    print(type + "::" + text)
    sys.stdout.flush()


root = "D:/eyetrackertest/"
printToJava("DATA ROOT FOLDER: " + root)
cascade = cv2.CascadeClassifier("D:/uni/ba/bale/src/main/resources/de/bale/eyetracking/haarcascade_eye.xml")
printToJava("CASCADECLASSIFIER: " + "D:/uni/ba/bale/src/main/resources/de/bale/eyetracking/haarcascade_eye.xml")
video_capture = cv2.VideoCapture(0)


def normalize(x):
    minn, maxx = x.min(), x.max()
    return (x - minn) / (maxx - minn)


def scan(image_size=(32, 32)):
    _, frame = video_capture.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    boxes = cascade.detectMultiScale(gray, 1.3, 10)
    if len(boxes) == 2:
        eyes = []
        for box in boxes:
            x, y, w, h = box
            eye = frame[y:y + h, x:x + w]
            eye = cv2.resize(eye, image_size)
            eye = normalize(eye)
            eye = eye[10:-10, 5:-5]
            eyes.append(eye)
        return (np.hstack(eyes) * 255).astype(np.uint8)
    else:
        return None


width, height = pyautogui.size()
width -= 1
height -= 1
printToJava("Resolution: {} X {}".format(width, height))
filepaths = os.listdir(root)
X, Y = [], []
printToJava("Number of Learning Data: {}".format(len(filepaths)))
for filepath in filepaths:
    x, y, _, _ = filepath.split(' ')
    x = float(x) / width
    y = float(y) / height
    X.append(cv2.imread(root + filepath))
    Y.append([x, y])
X = np.array(X) / 255.0
Y = np.array(Y)
print(X.shape, Y.shape)

model = Sequential()
model.add(Conv2D(32, 3, 2, activation='relu', input_shape=(12, 44, 3)))
model.add(Conv2D(64, 2, 2, activation='relu'))
model.add(Flatten())
model.add(Dense(32, activation='relu'))
model.add(Dense(2, activation='sigmoid'))
model.compile(optimizer="adam", loss="mean_squared_error")
model.summary()

epochs = 200
printToJava("Beginning fitting Model, epochs = {}".format(epochs))
for epoch in range(epochs):
    model.fit(X, Y, batch_size=32)
    print("{}/{} epochs".format(epoch,epochs))

printToJava("Tracking can now start")
while True:
    eyes = scan()
    if not eyes is None:
        eyes = np.expand_dims(eyes / 255.0, axis=0)
        x, y = model.predict(eyes)[0]
        x = round(x * width)
        y = round(y * height)
        text = "X:{} Y:{}".format(x, y)
        printToJava(text, "EYETRACKING")
        # pyautogui.moveTo(x * width, y * height)
