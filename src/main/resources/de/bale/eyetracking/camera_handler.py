import cv2

from threaded_camera import Camera


class CameraHandler:
    def __init__(self):
        self.camera_list = []
        self.start_cameras()

    def start_cameras(self):
        camera_index = 0
        cam_is_available = True
        while cam_is_available:
            camera = cv2.VideoCapture(camera_index)
            if camera is None or not camera.isOpened():
                cam_is_available = False
                continue
            camera_index += 1
            self.camera_list.append(camera)

    def get_images(self):
        images = []
        for camera in self.camera_list:
            _, image = camera.read()
            images.append(image)
        return images
