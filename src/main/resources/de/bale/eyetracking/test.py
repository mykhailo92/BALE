import cv2
import numpy as np
from tensorflow.keras.models import load_model

# Load the pre-trained gaze prediction model
model = load_model("gaze_model.h5")

# Open the webcam
cap = cv2.VideoCapture(0)

while True:
    # Capture a frame from the webcam
    ret, frame = cap.read()

    # Pre-process the frame
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    face = detect_face(gray) # detect_face() is a function that detects the face in the frame
    if face is not None:
        x, y, w, h = face
        face_img = gray[y:y+h, x:x+w]
        face_img = cv2.resize(face_img, (224, 224))
        face_img = face_img / 255.0
        face_img = np.expand_dims(face_img, axis=2)
        face_img = np.expand_dims(face_img, axis=0)

        # Pass the pre-processed frame to the model and get the gaze position
        gaze_position = model.predict(face_img)