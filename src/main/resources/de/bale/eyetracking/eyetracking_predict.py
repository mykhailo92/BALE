import os
import sys
from datetime import datetime

import cv2
import dlib
import numpy as np
import pyautogui
from tensorflow.keras.layers import *
from tensorflow.keras.models import *
from tensorflow.keras.optimizers import *


def printToJava(text, type="PLAIN"):
    print(type + "::" + text)
    sys.stdout.flush()


def getTrainingData(root):
    width, height = pyautogui.size()
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
    return X, Y


def getDateTimeString():
    date = datetime.now()
    formated_date = date.strftime("%m_%d_%Y %H_%M_%S_%f")
    return formated_date


def shape_to_np(shape, dtype="int"):
    # initialize the list of (x, y)-coordinates
    coords = np.zeros((68, 2), dtype=dtype)
    # loop over the 68 facial landmarks and convert them
    # to a 2-tuple of (x, y)-coordinates
    for i in range(0, 68):
        coords[i] = (shape.part(i).x, shape.part(i).y)
    # return the list of (x, y)-coordinates
    return coords


def drawBoxAround(img, shape, right=False) -> object:
    point = 36
    if right:
        point = point + 6
    min_x = shape[point][0]
    min_y = shape[point][1]
    max_x = shape[point][0]
    max_y = shape[point][1]
    for (x, y) in shape[point:point + 6]:
        if x < min_x:
            min_x = x
        elif x > max_x:
            max_x = x
        if y < min_y:
            min_y = y
        elif y > max_y:
            max_y = y
    cropped_image = img.copy();
    cv2.rectangle(img, (min_x, min_y), (max_x, max_y), (255, 255, 0), 1)
    cropped_image = cropped_image[min_y:max_y, min_x:max_x]
    # cropped_image = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2GRAY)
    return cropped_image


def create_model():
    model = Sequential()
    model.add(Conv2D(32, 3, 2, activation='relu', input_shape=(12, 88, 3)))
    model.add(Conv2D(64, 2, 2, activation='relu'))
    model.add(Flatten())
    model.add(Dense(32, activation='relu'))
    model.add(Dense(2, activation='sigmoid'))
    model.compile(optimizer="adam", loss="mean_squared_error")
    model.summary()
    return model


def train_model(model, X, Y, epochs=200):
    printToJava("Beginning fitting Model, epochs = {}".format(epochs))
    for epoch in range(epochs):
        model.fit(X, Y, batch_size=32)
        printToJava("{}/{} epochs".format(epoch + 1, epochs))


printToJava("test")
width, height = pyautogui.size()
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('D:/Uni/ba/bale/src/main/resources/de/bale/eyetracking/shape_68.dat')
X, Y = getTrainingData("D:/eyetrackertest/")
model = create_model()
printToJava("start training")
train_model(model, X, Y)
left = [36, 37, 38, 39, 40, 41]
right = [42, 43, 44, 45, 46, 47]
cap = cv2.VideoCapture(0)
max = 20
while (True):
    ret, img = cap.read()
    # cv2.imshow('eyes', img)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    rects = detector(gray, 1)
    for rect in rects:
        shape = predictor(gray, rect)
        shape = shape_to_np(shape)
        left_eye_cropped = drawBoxAround(img, shape)
        right_eye_cropped = drawBoxAround(img, shape, right=True)
        size = (44, 12)
        left_eye_cropped = cv2.resize(left_eye_cropped, size, interpolation=cv2.INTER_AREA)
        right_eye_cropped = cv2.resize(right_eye_cropped, size, interpolation=cv2.INTER_AREA)
        both_eyes_cropped = np.concatenate((left_eye_cropped, right_eye_cropped), axis=1)
        # cv2.imshow("test",both_eyes_cropped)
        current_image = np.expand_dims(both_eyes_cropped / 255.0, axis=0)

        x, y = model.predict(current_image)[0]
        text = "X:{} Y:{}".format(x * width, y * height)
        printToJava(text,"EYETRACKING")
    # max -= 1
    # print(max)
    # if max == 0:
    #     break
    # if cv2.waitKey(1) & 0xFF == ord('q'):
    #     break

# cap.release()