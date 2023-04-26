import argparse
import threading

import cv2
import mediapipe as mp
import numpy as np
from pynput import mouse

import utils
from basic_model import fit_basic_model
from camera_handler import CameraHandler


def setup_arg_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument("--image_count", type=int, default=50,
                        help="How many images should be used to callibrate the camera.")

    args = parser.parse_args()

    return args.image_count


def start_predicting(event: threading.Event):
    global is_running, model
    is_running = True
    while not event.isSet():
        images = camera_handler.get_images()
        x_coordinates = []
        y_coordinates = []
        failed = 0
        for image in images:
            both_eyes_image, eye_points = detect_eyes_from_image(image)
            both_eyes_image = np.expand_dims(both_eyes_image, axis=0)
            eye_points = np.expand_dims(eye_points, axis=0)
            try:
                if both_eyes_image.shape != (1, 36, 120, 3):
                    failed += 1
                    continue
                # x, y = model.predict([both_eyes_image, eye_points], verbose=0)[0]
                x, y = model.predict(both_eyes_image, verbose=0)[0]
                x *= utils.screen_x
                y *= utils.screen_y
                x_coordinates.append(x)
                y_coordinates.append(y)
            except ValueError:
                pass
        count = len(x_coordinates) - failed
        if count <= 0:
            continue
        x = sum(x_coordinates) / (len(x_coordinates) - failed)
        y = sum(y_coordinates) / (len(y_coordinates) - failed)
        utils.printToJava("{} {}".format(x, y), "EYETRACKING")


global model
global ignore_clicks
global listener
stop_event = threading.Event()

points = []
both_eyes = []
left_eyes = []
right_eyes = []
max_image_count = setup_arg_parser()
estimated_positions = []
mp_face_mesh = mp.solutions.face_mesh
face_mesh = mp_face_mesh.FaceMesh(min_detection_confidence=0.5, min_tracking_confidence=0.5)
is_running = False
ignore_clicks = False
camera_handler = CameraHandler()
input_is_running = True


def on_click(x, y, button, pressed):
    global model, is_running, ignore_clicks
    if not pressed or ignore_clicks:
        return
    click_thread = threading.Thread(target=threaded_click, args=(x, y,))
    click_thread.start()


def get_eye_rectangle(image, face_landmarks, keypoints):
    img_h, img_w, img_c = image.shape
    min_x, min_y, max_x, max_y = img_w, img_h, 0, 0
    for id in keypoints:
        x = int(face_landmarks.landmark[id].x * img_w)
        y = int(face_landmarks.landmark[id].y * img_h)
        if x < min_x: min_x = x
        if x > max_x: max_x = x
        if y < min_y: min_y = y
        if y > max_y: max_y = y
    return min_x, min_y, max_x, max_y


def detect_eyes_from_image(image):
    try:
        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    except cv2.error:
        return np.array([0]), np.array([0])  # image was empty
    face_mesh_image = face_mesh.process(image)
    image = cv2.cvtColor(image_rgb, cv2.COLOR_RGB2BGR)
    both_eyes_image = ""
    eye_points = ""
    if face_mesh_image.multi_face_landmarks:
        for face_landmarks in face_mesh_image.multi_face_landmarks:
            eye_image = image.copy()
            mesh_image = image.copy()
            mp.solutions.drawing_utils.draw_landmarks(mesh_image, face_landmarks,
                                                      mp.solutions.face_mesh.FACEMESH_CONTOURS,
                                                      mp.solutions.drawing_utils.DrawingSpec(
                                                          color=(255, 0, 255),
                                                          thickness=1,
                                                          circle_radius=1
                                                      ))
            left_eye = [33, 246, 161, 160, 159, 158, 157, 173, 133, 153, 154, 153, 145, 144, 163, 7, 33]
            right_eye = [362, 398, 384, 385, 386, 387, 388, 466, 263, 249, 390, 373, 374, 380, 381, 382, 362]
            left_min_x, left_min_y, left_max_x, left_max_y = get_eye_rectangle(eye_image, face_landmarks, left_eye)
            right_min_x, right_min_y, right_max_x, right_max_y = get_eye_rectangle(eye_image, face_landmarks, right_eye)
            eye_points = [left_min_x, left_min_y, left_max_x, left_max_y, right_min_x, right_min_y, right_max_x,
                          right_max_y]
            left_eye_image = image[left_min_y:left_max_y, left_min_x:left_max_x]
            right_eye_image = image[right_min_y:right_max_y, right_min_x:right_max_x]
            size = (60, 36)
            left_eye_image = cv2.resize(left_eye_image, size, interpolation=cv2.INTER_AREA)
            right_eye_image = cv2.resize(right_eye_image, size, interpolation=cv2.INTER_AREA)

            left_eye_image = utils.normalize(left_eye_image)
            right_eye_image = utils.normalize(right_eye_image)
            left_eyes.append(left_eye_image)
            right_eyes.append(right_eye_image)
            left_eye_array = np.array(left_eye_image)
            right_eye_array = np.array(right_eye_image)
            both_eyes_image = np.concatenate((left_eye_array, right_eye_array), axis=1)
            both_eyes.append(both_eyes_image)
    return both_eyes_image, eye_points


def reset_globals():
    global points, estimated_positions, both_eyes
    points, estimated_positions, both_eyes = [], [], []


def threaded_click(x, y):
    global model, is_running, ignore_clicks, listener
    images = camera_handler.get_images()
    for image in images:
        if image is None:
            return
        if not is_running:
            pre_length = len(both_eyes)
            _, eye_coordinates = detect_eyes_from_image(image)

            if pre_length < len(both_eyes):
                points.append(eye_coordinates)
                estimated_positions.append([x / utils.screen_x, y / utils.screen_y])
                utils.printToJava("{} {}".format(x / utils.screen_x, y / utils.screen_y), "EYETRACKING_CALLIBRATION")
            else:
                print("INVALID IMAGE")


def prepare_and_start_eyetracking():
    global ignore_clicks, model
    ignore_clicks = True
    estimated_positions_array = np.array(estimated_positions)
    both_eyes_array = np.array(both_eyes)
    points_array = np.array(points)
    left_eyes_array = np.array(left_eyes)
    right_eyes_array = np.array(right_eyes)
    reset_globals()
    utils.printToJava("Start Fit", "EYETRACKING_FIT_START")
    model = fit_basic_model([both_eyes_array, points_array], estimated_positions_array, 10000)
    # model = fit_basic_model([flleft_eyes_array, right_eyes_array], estimated_positions_array, 10000)
    ignore_clicks = False
    utils.printToJava("End Fit", "EYETRACKING_FIT_STOP")
    listener.stop()
    predict_thread = threading.Thread(target=start_predicting, args=(stop_event,))
    predict_thread.start()


def threaded_input_handler():
    global ignore_clicks, model, is_running, stop_event, listener

    utils.printToJava("Eyetracker has started.", "EYETRACKING_RUNNING")
    ignore_clicks = True
    while input_is_running:
        camera_handler.start_cameras()
        listener = mouse.Listener(on_click=on_click)
        listener.start()
        start_callibration, end_callibrating, stop_running = "", "", ""
        while start_callibration != "start":
            start_callibration = input("type 'start' to start callibration: \n")
        utils.printToJava("Ready for Callibration", "EYETRACKING_INFO")
        ignore_clicks = False
        while end_callibrating != "end" and end_callibrating != "stop":
            end_callibrating = input("type 'end' to finish callibration: \n")
        if end_callibrating == "end":
            prepare_and_start_eyetracking()
        while stop_running != "stop" and end_callibrating != "stop":
            stop_running = input("type 'stop' to stop Eyetracking:  \n")
        utils.printToJava("Stopping Eyetracking", "EYETRACKING_STOP")
        is_running = False
        stop_event.set()
        camera_handler.release_cameras()
        stop_event = threading.Event()
        ignore_clicks = True
        reset_globals()
        model = None


input_thread = threading.Thread(target=threaded_input_handler)
input_thread.start()
