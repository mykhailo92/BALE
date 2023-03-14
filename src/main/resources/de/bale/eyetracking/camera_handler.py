import cv2


class CameraHandler:
    def __init__(self):
        self.camera_list = []
        self.cameras_are_open = False
        self.start_cameras()

    def start_cameras(self):
        cam_is_available = True
        camera_count=0
        if self.cameras_are_open:
            return
        while cam_is_available:
            camera = cv2.VideoCapture(camera_count)
            if camera is None or not camera.isOpened():
                print("got no cam {}".format(camera_count))
                cam_is_available = False
                continue
            print("appending {}".format(camera_count))
            camera_count += 1
            self.camera_list.append(camera)
        self.cameras_are_open = True

    def get_images(self):
        images = []
        index = 0
        for camera in self.camera_list:
            _, image = camera.read()
            images.append(image)
            index += 1
        return images

    def release_all_camera(self):
        self.cameras_are_open = False
        for camera in self.camera_list:
            print("shuttind down cam")
            camera.release()
            self.camera_list.remove(camera)
