package de.bale.eyetracking;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.opencv.videoio.Videoio.CAP_DSHOW;

public class Eyetracker extends Application {

    Mat matrix = null;
    ImageView imageView;
    @Override
    public void start(Stage stage) throws IOException {
        // Capturing the snapshot from the camera
        Eyetracker obj = new Eyetracker();
        Image image = obj.captureSnapshot();

        // Setting the image view
        imageView = new ImageView(image);

        // setting the fit height and width of the image view
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        // Setting title to the Stage
        stage.setTitle("Capturing an image");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<60;i++) {
                    imageView.setImage(captureSnapshot());
//                    try {
////                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            }
        }).start();;
    }

    public Image captureSnapshot() {
        Image image = null;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture c = new VideoCapture(0,CAP_DSHOW);
        Mat matrix = new Mat();
        c.read(matrix);
        if (c.isOpened()) {
            if (c.read(matrix)) {
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".bmp", matrix, byteMat);
                image = new Image(new ByteArrayInputStream(byteMat.toArray()));
            }
        }
        return image;
    }

    public static void main(String args[]) {
        launch(args);
    }
}
//    public Eyetracker() {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        VideoCapture videoCapture = new VideoCapture(0);
//        Mat matrix = new Mat();
//        videoCapture.read(matrix);
//        Stage secondStage = new Stage();
//
//    }

