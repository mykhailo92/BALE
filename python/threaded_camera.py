import threading

import cv2


# A Singular threaded Camera
class Camera(threading.Thread):

    def run(self):
        while self.running:
            _, self.image = self.camera.read()

    def __init__(self, cameracode):
        super().__init__()
        self.camCode = cameracode
        self.camera = cv2.VideoCapture(self.camCode, cv2.CAP_DSHOW)
        self.image = None
        self.running = True

    def cam_is_available(self):
        if self.camera is None or not self.camera.isOpened():
            return False
        else:
            return True

    def stop(self):
        self.camera.release()
        self.running = False

    def get_image(self):
        return self.image

    def get_camera(self):
        return self.camera
