from threaded_camera import Camera


class CameraHandler:
    def __init__(self):
        self.camera_list = []
        self.start_cameras()

    def start_cameras(self):
        camera_index = 0
        cam_is_available = True
        while cam_is_available:
            camera = Camera(camera_index)
            if not camera.cam_is_available():
                cam_is_available = False
                continue
            else:
                camera_index += 1
                self.camera_list.append(camera)
                camera.start()

    def get_images(self):
        images = []
        try:
            for camera in self.camera_list:
                image = camera.get_image()
                images.append(image)
            return images
        except TypeError:
            return []

    def release_cameras(self):
        for camera in self.camera_list:
            camera.stop()
