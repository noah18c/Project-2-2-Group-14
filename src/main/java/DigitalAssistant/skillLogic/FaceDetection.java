package DigitalAssistant.skillLogic;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import nu.pattern.OpenCV;
import org.opencv.core.Core;

import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.MatOfRect;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;


public class FaceDetection {

    public FaceDetection(){
        
    }
    
    public static boolean faceDetection() {
        try {
            // Load the OpenCV native library
            OpenCV.loadLocally();

            // Open the default camera (index 0)
            VideoCapture cap = new VideoCapture(0);

            // Check if camera opened successfully
            if (!cap.isOpened()) {
                System.out.println("Failed to open camera!");
                return false;
            }

            // Load the pre-trained Haar cascade classifier for face detection
            CascadeClassifier faceCascade = new CascadeClassifier();
            faceCascade.load("src/main/resources/DigitalAssistant/haarcascades/haarcascade_frontalface_alt.xml");

            // CascadeClassifier eyeCascade = new CascadeClassifier();
            // eyeCascade.load("haarcascade_eye.xml");

            // Create a window to display the video stream
            HighGui.namedWindow("Detected face", HighGui.WINDOW_NORMAL);

            boolean faceDetected = false;
            Mat frame = new Mat();
            Mat gray = new Mat();
            String imagePath = "";

            // Loop to continuously capture frames
            while (!faceDetected) {
                
                cap.read(frame);

                // Convert the frame to grayscale for faster processing
                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

                // Detect faces in the frame using the Haar cascade classifier
                MatOfRect faces = new MatOfRect();
                faceCascade.detectMultiScale(gray, faces, 1.3, 5);

                // Check if any faces were detected
                if (!faces.empty()) {
                    faceDetected = true;
                    System.out.println("++++ Detected face...");
                    // Draw a rectangle around each face
                    for (Rect rect : faces.toArray()) {
                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 5);

                        // Mat faceROI = gray.submat(rect);
                        // Mat faceColorROI = frame.submat(rect);

                        // Eye detection
                        // MatOfRect eyes = new MatOfRect();
                        // eyeCascade.detectMultiScale(faceROI, eyes, 1.3, 5);

                        // for (Rect eyeRect : eyes.toArray()) {
                        //     Imgproc.rectangle(faceColorROI, eyeRect.tl(), eyeRect.br(), new Scalar(255, 0, 0), 5);
                        // }
                    }
                    // Display the frame with the detected faces in a window
                    Imgproc.putText(frame, "Face Detected", new Point(10, 30), Core.BORDER_CONSTANT, 1.0, new Scalar(0, 0, 255), 2);
                    // Save the captured frame as an image file
                    imagePath = "src/main/resources/DigitalAssistant/haarcascades/img1.jpg";  // Specify the path to save the image
                    Imgcodecs.imwrite(imagePath, frame);

                    //HighGui.imshow("Video Stream", frame);
                   
                    // Wait for a key press to exit the loop
                    // if (HighGui.waitKey(1) == '\u0071' || HighGui.waitKey(1) == 27) {
                    //     // User pressed 'q' or 'esc' key, terminate the program
                    //     //return faceDetected;
                    //     break;
                    // }
                    //HighGui.waitKey(0);
                }
                if (faceDetected) {
                    cap.release();
                    //break;
                    return faceDetected;
                }
                
                if (!faceDetected) {
                    System.out.println("+++ No face detected");
                    return faceDetected;
                }

            }
            
            // Display the saved image
            System.out.println("*** The image path: " + imagePath);
            Mat savedImage = Imgcodecs.imread(imagePath);
            HighGui.imshow("Detected face", savedImage);
            //HighGui.imshow("Video Stream", frame);

            // Check if the user pressed the 'q' key to quit
            if (HighGui.waitKey(1) == '\u0071' || HighGui.waitKey(1) == 27) {
                // User pressed 'q' or 'esc' key, terminate the program
                HighGui.waitKey(0);
                HighGui.destroyAllWindows();
                return faceDetected;
                //break;
            }
            Thread.sleep(4000);
            // Release the camera and destroy the window
            //cap.release();
            //HighGui.waitKey(0);
            
            return faceDetected;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("starting face detection...");
        if (faceDetection()) System.out.println("face detection successful");

    }
}

