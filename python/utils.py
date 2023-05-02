import os
import sys

import cv2
# import dlib
import numpy as np
from screeninfo import get_monitors

# import scipy.io as sio
# from matplotlib import pyplot as plt

params = cv2.SimpleBlobDetector_Params()
params.filterByInertia = False
params.filterByConvexity = False
blob_detector = cv2.SimpleBlobDetector(params)
screen_x = get_monitors()[0].width
screen_y = get_monitors()[0].height
# These are for the colored printing...
os.system("")  # Allows Color Coding in Console
color_dict = {
    "red": '\033[91m',
    "green": '\033[92m',
    "yellow": '\033[93m',
    "blue": '\033[94m',
    "magenta": '\033[95m'
}
base_dir = os.path.dirname(__file__)


# Prints a Message to Java, the flush() is required to ensure that Java immediately gets the Message.
def printToJava(text, type="PLAIN"):
    print(type + "::" + str(text))
    sys.stdout.flush()


# Show an Image using the OpenCV Library. Useful for debugging
def show_cv2_frame(name, image):
    if isinstance(image, str):
        return False
    if isinstance(image, list):
        image_concat = image[0]
        for part in image[1:len(image)]:
            image_concat = np.concatenate((image_concat, part), axis=1)
        image = image_concat
    # cv2.imshow(name, image)
    return True

# Normalizes the Image so that all Values in the Image are between 0 and 1
def normalize(image):
    min_value, max_value = image.min(), image.max()
    return (image - min_value) / (max_value - min_value)
