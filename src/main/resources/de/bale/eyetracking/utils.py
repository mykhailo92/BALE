import threading
from datetime import datetime

import numpy as np

def getDateTimeString():
    date = datetime.now()
    formated_date = date.strftime("%m_%d_%Y %H_%M_%S")
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