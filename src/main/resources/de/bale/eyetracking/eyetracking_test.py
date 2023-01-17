from datetime import datetime

import cv2
import dlib
import numpy as np
from pynput import mouse

root = "D:/eyetrackertest/"
current_image = ""


def getDateTimeString():
    date = datetime.now()
    formated_date = date.strftime("%m_%d_%Y %H_%M_%S")
    return formated_date


def on_click(x, y, button, pressed):
    if pressed:
        # Crop the eyes
        eyes = current_image
        print("scanning done")
        # If the function returned None, something went wrong
        if not eyes is None:
            timestamp = getDateTimeString()
            print(root + "{} {} {}.jpeg".format(x, y, timestamp))
            # Save the image
            filename = root + "{} {} {}.jpeg".format(x, y, timestamp)
            cv2.imwrite(filename, eyes)


def shape_to_np(shape, dtype="int"):
    # initialize the list of (x, y)-coordinates
    coords = np.zeros((68, 2), dtype=dtype)
    # loop over the 68 facial landmarks and convert them
    # to a 2-tuple of (x, y)-coordinates
    for i in range(0, 68):
        coords[i] = (shape.part(i).x, shape.part(i).y)
    # return the list of (x, y)-coordinates
    return coords


def eye_on_mask(mask, side):
    points = [shape[i] for i in side]
    points = np.array(points, dtype=np.int32)
    mask = cv2.fillConvexPoly(mask, points, 255)
    return mask


def contouring(thresh, mid, img, right=False):
    cnts, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    try:
        cnt = max(cnts, key=cv2.contourArea)
        M = cv2.moments(cnt)
        cx = int(M['m10'] / M['m00'])
        cy = int(M['m01'] / M['m00'])
        if right:
            cx += mid
        # cv2.circle(img, (cx, cy), 4, (0, 0, 255), 2)
    except:
        pass


detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('shape_68.dat')

left = [36, 37, 38, 39, 40, 41]
right = [42, 43, 44, 45, 46, 47]

cap = cv2.VideoCapture(0)
ret, img = cap.read()
thresh = img.copy()

cv2.namedWindow('image')
kernel = np.ones((9, 9), np.uint8)


def nothing(x):
    pass


cv2.createTrackbar('threshold', 'image', 0, 255, nothing)


def drawBoxAround(img, right=False) -> object:
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


listener = mouse.Listener(on_click=on_click)
listener.start()

while (True):
    ret, img = cap.read()
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    rects = detector(gray, 1)
    for rect in rects:
        shape = predictor(gray, rect)
        shape = shape_to_np(shape)
        mask = np.zeros(img.shape[:2], dtype=np.uint8)
        mask = eye_on_mask(mask, left)
        mask = eye_on_mask(mask, right)
        mask = cv2.dilate(mask, kernel, 5)
        eyes = cv2.bitwise_and(img, img, mask=mask)
        mask = (eyes == [0, 0, 0]).all(axis=2)
        eyes[mask] = [255, 255, 255]
        mid = (shape[42][0] + shape[39][0]) // 2
        eyes_gray = cv2.cvtColor(eyes, cv2.COLOR_BGR2GRAY)
        threshold = cv2.getTrackbarPos('threshold', 'image')
        _, thresh = cv2.threshold(eyes_gray, threshold, 255, cv2.THRESH_BINARY)
        thresh = cv2.erode(thresh, None, iterations=2)  # 1
        thresh = cv2.dilate(thresh, None, iterations=4)  # 2
        thresh = cv2.medianBlur(thresh, 3)  # 3
        thresh = cv2.bitwise_not(thresh)
        contouring(thresh[:, 0:mid], mid, img)
        contouring(thresh[:, mid:], mid, img, True)
        left_eye_cropped = drawBoxAround(img)
        right_eye_cropped = drawBoxAround(img, right=True)
        size = (100, 50)
        left_eye_cropped = cv2.resize(left_eye_cropped, size, interpolation=cv2.INTER_AREA)
        right_eye_cropped = cv2.resize(right_eye_cropped, size, interpolation=cv2.INTER_AREA)
        both_eyes_cropped = np.concatenate((left_eye_cropped, right_eye_cropped), axis=1)
        current_image = both_eyes_cropped
        cv2.imshow("cropped", both_eyes_cropped)

    # show the image with the face detections + facial landmarks
    cv2.imshow('eyes', img)
    cv2.imshow("image", thresh)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
