import os

import cv2
import numpy as np
from pynput import mouse

from constants import get_detector, get_shape_68_predictor
from utils import shape_to_np, getDateTimeString


def test_webcam(source):
    print("test:", source)
    cap = cv2.VideoCapture(source)
    if cap is None or not cap.isOpened():
        return None
    return cap


root = "D:/eyetrackertest/"
captures = list()
index = 0
cap = test_webcam(index)
current_image = list()
while cap is not None:
    captures.append(cap)
    index += 1
    cap = test_webcam(index)
    current_image.append("")


def on_click(x, y, button, pressed):
    if pressed:
        index = 0;
        for eyes in current_image:
            timestamp = getDateTimeString()
            print(root + "{} {} {}.jpeg".format(x, y, timestamp))
            # Save the image
            filename = root + "{} {} Camera_{}_on_{}.jpeg".format(x, y, index, timestamp)
            index += 1
            cv2.imwrite(filename, eyes)


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
    cropped_image = cv2.cvtColor(cropped_image, cv2.COLOR_BGR2GRAY)
    return cropped_image


listener = mouse.Listener(on_click=on_click)
listener.start()

isExist = os.path.exists(root)
if not isExist:
    os.makedirs(root)

detector = get_detector()
predictor = get_shape_68_predictor()


def detect_eyes(faces, gray, img):
    for face in faces:
        shape = predictor(gray, face)
        shape = shape_to_np(shape)
        left_eye_cropped = drawBoxAround(img, shape)
        right_eye_cropped = drawBoxAround(img, shape, right=True)
        size = (44, 12)
        left_eye_cropped = cv2.resize(left_eye_cropped, size, interpolation=cv2.INTER_AREA)
        right_eye_cropped = cv2.resize(right_eye_cropped, size, interpolation=cv2.INTER_AREA)
        both_eyes_cropped = np.concatenate((left_eye_cropped, right_eye_cropped), axis=1)
        cv2.imshow("cropped", both_eyes_cropped)
        return both_eyes_cropped


breaker = False
while (True):
    if breaker:
        break
    format_index = 0
    for cap in captures:
        _, img = cap.read()
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        faces = detector(gray, 1)
        current_image[format_index] = detect_eyes(faces, gray, img)
        cv2.imshow('eyes {}'.format(format_index), img)
        format_index += 1
        if cv2.waitKey(1) & 0xFF == ord('q'):
            breaker = True
            break
