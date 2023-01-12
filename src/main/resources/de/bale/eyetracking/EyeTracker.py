# import sys
#
# import numpy as np
# import cv2
# import dlib
#
#
#
# def printToJava(text):
#     print(text)
#     sys.stdout.flush()
#
#
# def shape_to_np(shape, dtype="int"):
#     # initialize the list of (x, y)-coordinates
#     coords = np.zeros((68, 2), dtype=dtype)
#     # loop over the 68 facial landmarks and convert them
#     # to a 2-tuple of (x, y)-coordinatesex
#     for i in range(0, 68):
#         coords[i] = (shape.part(i).x, shape.part(i).y)
#     # return the list of (x, y)-coordinates
#     return coords
#
#
# def eye_on_mask(mask, side):
#     points = [shape[i] for i in side]
#     points = np.array(points, dtype=np.int32)
#     mask = cv2.fillConvexPoly(mask, points, 255)
#     return mask
#
#
# def contouring(thresh, mid, img, right=False):
#     cnts, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
#     try:
#         cnt = max(cnts, key=cv2.contourArea)
#         M = cv2.moments(cnt)
#         cx = int(M['m10'] / M['m00'])
#         cy = int(M['m01'] / M['m00'])
#         if right:
#             cx += mid
#         cv2.circle(img, (cx, cy), 4, (0, 0, 255), 2)
#     except:
#         pass
#
# sys.stdout.flush()
# sys.stderr.flush()
# sys.stdin.flush()
# printToJava(sys.version)
#
# detector = dlib.get_frontal_face_detector()
# predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')
#
# left = [36, 37, 38, 39, 40, 41]
# right = [42, 43, 44, 45, 46, 47]
#
# cap = cv2.VideoCapture(0)
# ret, img = cap.read()
# thresh = img.copy()
#
# cv2.namedWindow('image')
# kernel = np.ones((9, 9), np.uint8)
#
#
# def nothing(x):
#     pass
#
#
# cv2.createTrackbar('threshold', 'image', 0, 255, nothing)
# while (True):
#     ret, img = cap.read()
#     gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
#     rects = detector(gray, 1)
#     for rect in rects:
#         shape = predictor(gray, rect)
#         shape = shape_to_np(shape)
#         mask = np.zeros(img.shape[:2], dtype=np.uint8)
#         mask = eye_on_mask(mask, left)
#         mask = eye_on_mask(mask, right)
#         mask = cv2.dilate(mask, kernel, 5)
#         eyes = cv2.bitwise_and(img, img, mask=mask)
#         mask = (eyes == [0, 0, 0]).all(axis=2)
#         eyes[mask] = [255, 255, 255]
#         mid = (shape[42][0] + shape[39][0]) // 2
#         eyes_gray = cv2.cvtColor(eyes, cv2.COLOR_BGR2GRAY)
#         threshold = cv2.getTrackbarPos('threshold', 'image')
#         _, thresh = cv2.threshold(eyes_gray, threshold, 255, cv2.THRESH_BINARY)
#         thresh = cv2.erode(thresh, None, iterations=2)  # 1
#         thresh = cv2.dilate(thresh, None, iterations=4)  # 2
#         thresh = cv2.medianBlur(thresh, 3)  # 3
#         thresh = cv2.bitwise_not(thresh)
#         contouring(thresh[:, 0:mid], mid, img)
#         contouring(thresh[:, mid:], mid, img, True)
#         for (x, y) in shape[36:48]:
#             cv2.circle(img, (x, y), 2, (255, 0, 0), -1)
#     # show the image with the face detections + facial landmarks
#     cv2.imshow('eyes', img)
#     cv2.imshow("image", thresh)
#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break
#
# cap.release()
# cv2.destroyAllWindows()
import os

import cv2
import numpy as np
import pyautogui
from keras import Sequential
from keras.layers import Conv2D, Flatten, Dense
from tensorflow.keras.layers import *
from tensorflow.keras.models import *
from tensorflow.keras.optimizers import *

root = "D:/eyetrackertest"
cascade = cv2.CascadeClassifier("D:/Uni/ba/bale/src/main/resources/de/bale/eyetracking/haarcascade_eye.xml")
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


# Note that there are actually 2560x1440 pixels on my screen
# I am simply recording one less, so that when we divide by these
# numbers, we will normalize between 0 and 1. Note that mouse
# coordinates are reported starting at (0, 0), not (1, 1)
width, height = 1918, 1079

filepaths = os.listdir(root)
X, Y = [], []
for filepath in filepaths:
    x, y, _ = filepath.split(' ')
    x = float(x) / width
    y = float(y) / height
    X.append(cv2.imread(root + filepath))
    Y.append([x, y])
X = np.array(X) / 255.0
Y = np.array(Y)
print(X.shape, Y.shape)

model = Sequential()
model.add(Conv2D(32, 3, 3, activation='relu', input_shape=(12, 44, 3)))
model.add(Conv2D(64, 2, 3, activation='relu'))
model.add(Flatten())
model.add(Dense(32, activation='relu'))
model.add(Dense(2, activation='sigmoid'))
model.compile(optimizer="adam", loss="mean_squared_error")
model.summary()

epochs = 200
for epoch in range(epochs):
    model.fit(X, Y, batch_size=32)

while True:
    eyes = scan()
    if not eyes is None:
        eyes = np.expand_dims(eyes / 255.0, axis=0)
        x, y = model.predict(eyes)[0]
        pyautogui.moveTo(x * width, y * height)
