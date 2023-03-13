import threading

import cv2


class Camera(threading.Thread):

    def run(self):
        self.camera = cv2.VideoCapture(self.camCode, cv2.CAP_DSHOW)
        while self.running:
            _, self.image = self.camera.read()

    def __init__(self, cameracode):
        super().__init__()
        self.camera = ""
        self.camCode = cameracode
        self.image = None
        self.running = True
        self.start()

    def stop(self):
        self.camera.release()
        self.running = False

    def get_image(self):
        return self.image
