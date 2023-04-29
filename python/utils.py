import os
import sys
import time
from datetime import datetime

import cv2
# import dlib
import numpy as np
# import scipy.io as sio
# from matplotlib import pyplot as plt

params = cv2.SimpleBlobDetector_Params()
params.filterByInertia = False
params.filterByConvexity = False
blob_detector = cv2.SimpleBlobDetector(params)
screen_x = 1920
screen_y = 1080
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

# def rotate_image(image, angle):
#     image_center = tuple(np.array(image.shape[1::-1]) / 2)
#     rot_mat = cv2.getRotationMatrix2D(image_center, angle, 1.0)
#     result = cv2.warpAffine(image, rot_mat, image.shape[1::-1], flags=cv2.INTER_LINEAR)
#     return result


# def annotate_annoationLine_gaze(line):
#     splittedLine = line.split(" ")
#     ret_val = dict()
#     ret_val['dim1'] = splittedLine[0]
#     ret_val['dim2'] = splittedLine[1]
#     ret_val['dim3'] = splittedLine[2]
#     ret_val['dim4'] = splittedLine[3]
#     ret_val['dim5'] = splittedLine[4]
#     ret_val['dim6'] = splittedLine[5]
#     ret_val['dim7'] = splittedLine[6]
#     ret_val['dim8'] = splittedLine[7]
#     ret_val['dim9'] = splittedLine[8]
#     ret_val['dim10'] = splittedLine[9]
#     ret_val['dim11'] = splittedLine[10]
#     ret_val['dim12'] = splittedLine[11]
#     ret_val['dim13'] = splittedLine[12]
#     ret_val['dim14'] = splittedLine[13]
#     ret_val['dim15'] = splittedLine[14]
#     ret_val['dim16'] = splittedLine[15]
#     ret_val['dim17'] = splittedLine[16]
#     ret_val['dim18'] = splittedLine[17]
#     ret_val['dim19'] = splittedLine[18]
#     ret_val['dim20'] = splittedLine[19]
#     ret_val['dim21'] = splittedLine[20]
#     ret_val['dim22'] = splittedLine[21]
#     ret_val['dim23'] = splittedLine[22]
#     ret_val['dim24'] = splittedLine[23]
#     ret_val['Screen X'] = splittedLine[24]
#     ret_val['Screen Y'] = splittedLine[25]
#     ret_val['3D GazeTarget X'] = splittedLine[26]
#     ret_val['3D GazeTarget Y'] = splittedLine[27]
#     ret_val['3D GazeTarget Z'] = splittedLine[28]
#     ret_val['dim30'] = splittedLine[29]
#     ret_val['dim31'] = splittedLine[30]
#     ret_val['dim32'] = splittedLine[31]
#     ret_val['dim33'] = splittedLine[32]
#     ret_val['dim34'] = splittedLine[33]
#     ret_val['dim35'] = splittedLine[34]
#     ret_val['dim36'] = splittedLine[35]
#     ret_val['dim37'] = splittedLine[36]
#     ret_val['dim38'] = splittedLine[37]
#     ret_val['dim39'] = splittedLine[38]
#     ret_val['dim40'] = splittedLine[39]
#     ret_val['dim41'] = splittedLine[40]
#     return ret_val


# def annotate_line_face_gaze(line):
#     splitted_line = line.split(" ")
#     ret_val = dict()
#     ret_val['image_path'] = splitted_line[0]
#     ret_val['image'] = splitted_line[2]
#     ret_val['y'] = splitted_line[3]
#     return ret_val


# def format_time(time_passed):
#     ms = time_passed % 1
#     time_passed -= ms
#     ms = np.round(ms, 4)
#     m, s = divmod(int(time_passed), 60)
#     h, m = divmod(m, 60)
#     time_pretty = "{:d}h {:02d}min {:02d}s {}ms".format(h, m, s, ms)
#     return time_pretty


# def get_screen_information(path_to_mat):
#     if path_to_mat != "":
#         ret_val = dict()
#         mat = sio.loadmat(path_to_mat)
#         ret_val['height_mm'] = np.round(mat['height_mm'][0][0], 2)
#         ret_val['height_px'] = mat['height_pixel'][0][0]
#         ret_val['width_mm'] = np.round(mat['width_mm'][0][0], 2)
#         ret_val['width_px'] = mat['width_pixel'][0][0]
#         ret_val['dpmm'] = mat['height_pixel'][0][0] / (mat['height_mm'][0][0])
#         return ret_val


def printToJava(text, type="PLAIN"):
    print(type + "::" + str(text))
    sys.stdout.flush()


# def shape_to_np(shape, dtype="int"):
#     # initialize the list of (image, y)-coordinates
#     coords = np.zeros((68, 2), dtype=dtype)
#     # loop over the 68 facial landmarks and convert them
#     # to a 2-tuple of (image, y)-coordinates
#     for i in range(0, 68):
#         coords[i] = (shape.part(i).x, shape.part(i).y)
#     # return the list of (image, y)-coordinates
#     return coords


# def convert_tuple(tup):
#     x = "X: " + np.format_float_positional(tup[0], precision=2) + " "
#     y = "Y: " + np.format_float_positional(tup[1], precision=2) + " "
#     z = "Z: " + np.format_float_positional(tup[2], precision=2) + " "
#     str = x + y + z
#     return str


# def print_text_in_image(image, text, row=0, column=0, x_abs=-1, y_abs=-1):
#     if x_abs > 0 and y_abs > 0:
#         cv2.putText(image, text, (x_abs, y_abs), font, 1,
#                     (255, 255, 255), 2, cv2.LINE_AA)
#     else:
#         x_pos = row * row_height + image_offset
#         y_pos = column * column_width
#         cv2.putText(image, text, (y_pos, x_pos), font, 1,
#                     (255, 255, 255), 2, cv2.LINE_AA)


# class Timer(object):
#     def __init__(self, name=None):
#         self.name = name
#         self.tround = 0
#         self.total_delta = 0
#         self.number_of_rounds = 0
#
#     def start(self):
#         print("Start", self.name, end="")
#         start_time = datetime.now()
#         formated_time = start_time.strftime("  %d.%m.%Y %H:%M:%S")
#         print(formated_time)
#         self.tstart = time.time()
#         self.tround = self.tstart
#
#     def timestamp(self):
#         my_time = time.time()
#         delta = my_time - self.tround
#         self.tround = my_time
#         self.total_delta += delta
#         self.number_of_rounds += 1
#         return delta
#
#     def average_round_time(self):
#         return int(self.total_delta / self.number_of_rounds)
#
#     def time_since_start(self):
#         return time.time() - self.tstart


# def color_print(color_code, string, end="\n"):
#     if color_code in color_dict:
#         code = color_dict[color_code]
#     else:
#         code = ""
#     print(code, string + '\033[0m', end=end)


# def normalize(image):
#     minn, maxx = image.min(), image.max()
#     return (image - minn) / (maxx - minn)


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


def drawBoxAround(img, shape, right=False) -> object:
    point = 36
    if right:
        point = point + 6
    min_x = shape[point][0]
    min_y = shape[point][1]
    max_x = shape[point][0]
    max_y = shape[point][1]
    for (x, y) in shape[point:point + 6]:
        if x < min_x:
            min_x = x
        elif x > max_x:
            max_x = x
        if y < min_y:
            min_y = y
        elif y > max_y:
            max_y = y
    cropped_image = img.copy()
    # cv2.rectangle(img, (min_x, min_y), (max_x, max_y), (0, 255, 0), 1)
    cropped_image = cropped_image[min_y:max_y, min_x:max_x]
    return cropped_image, min_x, min_y


def draw_face_from_shape(image, shape):
    face_outline = shape[
        np.array([0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,  # Bottom Outline
                  26, 25, 24, 23, 22, 21, 20, 19, 18, 17  # EyeBrows
                  ])]
    left_eye = shape[36:42]
    right_eye = shape[42:48]
    nose = shape[np.array([27, 28, 29, 30, 33, 32, 31, 32, 33, 34, 35, 34, 33, 30, 29, 28])]
    mouth_outline = shape[48:60]
    mouth_inline = shape[60:68]
    segments = [face_outline, left_eye, right_eye, nose, mouth_outline, mouth_inline]
    for segment in segments:
        first_point = segment[0]
        last_point = first_point
        for point in segment:
            cv2.circle(image, point, 1, (255, 255, 255), 1)
            cv2.line(image, last_point, point, (0, 255, 255), 1)
            last_point = point
        cv2.line(image, last_point, first_point, (0, 255, 255), 1)


def save_data_as_mat(input_image, headpose, estimated_position):
    data_struct = {
        "image": input_image,
        "headpose": headpose,
        "estimated_position": estimated_position
    }
    color_print("magenta", "Saving: {}".format(os.path.dirname(__file__) + "/materials/data.mat"))
    sio.savemat(os.path.dirname(__file__) + "/materials/data.mat",
                data_struct)  # Use volumeViewer when reading in Matlab
    return None


def display_images_during_collection(images, file_info, angle_vector, screen_info, gaze_position, normalized_gaze,
                                     timer, counts):
    """
    :param images: List of 3. Contains [full image, Rotated Face, Eyes]
    :param file_info: List of 3. Contains [Participant, Day, File_name]
    :param angle_vector: List of 3. Contains [X, Y, Z] head angle Vector
    :param screen_info: List of 2. Contains [screen_width, screen_height]
    :param gaze_position: List of 2. Contains [X, Y] in absolute Screen coordinates
    :param normalized_gaze: List of 2. Contains [X, Y] in 0 to 1 normalized coordinates
    :param timer: TimerObject
    :param counts: List of 3. Contains [Current Count, Skipped Count, Total Images]
    """
    # Unpack Params
    breaker = False
    screen_width, screen_height = screen_info[0], screen_info[1]
    participant, day, file_name = file_info[0], file_info[1], file_info[2]
    x_pos, y_pos = gaze_position[0], gaze_position[1]
    image, rotated_face, normalized_eyes = images[0], images[1], images[2]
    x_norm, y_norm = normalized_gaze[0], normalized_gaze[1]
    count, skipped_images, total_image_count = counts[0], counts[1], counts[2]
    skipped_percent = np.round(skipped_images / total_image_count * 100, 10)
    percent_done = np.round((count + skipped_images) / total_image_count * 100, 2)

    print_text_in_image(image, participant + " " + day + " #" + file_name, x_abs=30, y_abs=30)
    print_text_in_image(image, "HeadPose", 0)
    print_text_in_image(image, "X: {} Y: {} Z: {}".format(angle_vector[0], angle_vector[1],
                                                          angle_vector[2]), 1)
    print_text_in_image(image, "Gaze Position", 2)
    print_text_in_image(image, "X: {} Y: {}".format(x_pos, y_pos), 3)

    print_text_in_image(image, "{} image {}".format(screen_width, screen_height), y_abs=30, x_abs=200)
    print_text_in_image(image, "Normalised Gaze Position", 2, 1)
    print_text_in_image(image, "X: {} Y: {}".format(x_norm, y_norm), 3, 1)

    print_text_in_image(image, "Time Elapsed:", 5)
    print_text_in_image(image, format_time(timer.time_since_start()), 6)
    print_text_in_image(image, "Image {}/{}".format(count, total_image_count), 7)
    print_text_in_image(image, "({}%)".format(percent_done), 8)

    print_text_in_image(image, "Skipped {}/{}".format(skipped_images, total_image_count), 9)
    print_text_in_image(image, "({}%)".format(skipped_percent), 10)

    try:
        cv2.imshow("rotated", rotated_face)
        cv2.imshow("eyes", normalized_eyes)
        cv2.imshow("image", image)
    except cv2.error as e:
        color_print("red", repr(e))
        color_print("red",
                    "Error while displaying {}".format(participant + " " + day + " " + file_name))
    key = cv2.waitKey(1)
    if key == ord('p'):
        cv2.waitKey(-1)
    if key == ord('q'):
        breaker = True
    return breaker


def plot_from_history(model_history, fig_title, loss_type, patience, second_metric="y_accuracy", validation=True):
    fig, axs = plt.subplots(2)
    fig.suptitle(fig_title + " (" + loss_type + ")")
    if len(model_history.history['loss']) > patience:
        selected_epoch = len(model_history.history['loss']) - patience
    else:
        selected_epoch = len(model_history.history['loss'])
    axs[0].set_title("Loss")
    axs[0].plot(model_history.history['loss'])
    if validation:
        axs[0].plot(model_history.history['val_loss'])
    axs[0].set_ylabel("Loss")
    axs[0].set_axisbelow(True)
    axs[0].yaxis.grid(color='gray', linestyle='dashed')
    axs[0].axvline(x=selected_epoch, color='g', linewidth=2, linestyle="dashed",
                   zorder=20, label='axvline - full height')
    if second_metric is not None:
        axs[1].set_title(second_metric)
        axs[1].plot(model_history.history[second_metric])
        if validation:
            axs[1].plot(model_history.history["val_" + second_metric])
        axs[1].set_ylabel(second_metric)
        axs[1].set_axisbelow(True)
        axs[1].yaxis.grid(color='gray', linestyle='dashed')
        axs[1].axvline(x=selected_epoch, color='g', linewidth=2, linestyle="dashed",
                       zorder=20, label='axvline - full height')
        if validation:
            axs[1].legend(['Train', 'Validation'], loc='upper left')
        else:
            axs[1].legend(['Train'], loc='upper left')

    plt.xlabel('epoch')
    if validation:
        axs[0].legend(['Train', 'Validation'], loc='upper left')
    else:
        axs[0].legend(['Train'], loc='upper left')

    plt.subplots_adjust(left=0.2,
                        bottom=0.1,
                        right=0.9,
                        top=0.9,
                        wspace=0.4,
                        hspace=0.5)

    file_path = base_dir + "/outputs/training_graphs/"
    file_path_exists = os.path.exists(file_path)
    if not file_path_exists:
        os.makedirs(file_path)
    plt.savefig(file_path + fig_title + "_" + loss_type + '.pdf', bbox_inches='tight')
    plt.show()


def create_output_directory(path):
    file_path = base_dir + "/" + path
    file_path_exists = os.path.exists(file_path)
    if not file_path_exists:
        os.makedirs(file_path)


def normalize(image):
    minn, maxx = image.min(), image.max()
    return (image - minn) / (maxx - minn)
