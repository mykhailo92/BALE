import os
import shutil
from datetime import datetime

import cv2
import numpy as np
from pynput.mouse import Listener

root = "D:/eyetrackertest/"
if os.path.isdir(root):
    resp = ""
    while not resp in ["Y", "N"]:
        resp = "N"
    if resp == "Y":
        shutil.rmtree(root)
        os.mkdir(root)

cascade = cv2.CascadeClassifier("D:/uni/ba/bale/src/main/resources/de/bale/eyetracking/haarcascade_eye.xml")
video_capture = cv2.VideoCapture(0)


# Normalization helper function
def normalize(x):
    minn, maxx = x.min(), x.max()
    return (x - minn) / (maxx - minn)


# Eye cropping function
def scan(image_size=(32, 32)):
    _, frame = video_capture.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    boxes = cascade.detectMultiScale(gray, 1.3, 10)
    if len(boxes) == 2:
        eyes = []
        for box in boxes:
            x, y, w, h = box
            eye = gray[y:y + h, x:x + w]
            eye = cv2.resize(eye, image_size)
            eye = normalize(eye)
            eye = eye[10:-10, 5:-5]
            eyes.append(eye)
        return (np.hstack(eyes) * 255).astype(np.uint8)
    else:
        return None


def getDateTimeString():
    date = datetime.now()
    formated_date = date.strftime("%m_%d_%Y %H_%M_%S")
    return formated_date


def on_click(x, y, button, pressed):
    # if the action was a mouse PRESS (not a RELEASE)
    if pressed:
        # Crop the eyes
        eyes = scan()
        print("scanning done")
        # If the function returned None, something went wrong
        if not eyes is None:
            timestamp = getDateTimeString()
            print(root + "{} {} {}.jpeg".format(x, y, timestamp))
            # Save the image
            filename = root + "{} {} {}.jpeg".format(x, y, timestamp)
            cv2.imwrite(filename, eyes)


with Listener(on_click=on_click) as listener:
    listener.join()
