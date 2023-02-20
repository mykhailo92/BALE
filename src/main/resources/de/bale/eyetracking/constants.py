import dlib


def get_shape_68_left_eye():
    return [36, 37, 38, 39, 40, 41]


def get_shape_68_right_eye():
    return [42, 43, 44, 45, 46, 47]


def get_detector():
    return dlib.get_frontal_face_detector()


def get_shape_68_predictor():
    return dlib.shape_predictor('shape_68.dat')
