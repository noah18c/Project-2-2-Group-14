package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import nu.pattern.OpenCV;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.MatOfRect;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;


public class FaceDetection {

    public FaceDetection(){
        //faceDetection();
    }
    /**
     * Method runs face detection based on pre-trained Haar cascade classifier!
     * @return 
     */
    public boolean faceDetection() {
        try {
            ArrayList<Mat> our_images = new ArrayList<>();
            long startTime = System.currentTimeMillis();
    
            // Load the OpenCV native library
            OpenCV.loadLocally();

            // Open the default camera (index 0) 
            VideoCapture cap = new VideoCapture(0);

            // Check if camera opened successfully
            if (!cap.isOpened()) {
                System.out.println("Failed to open camera!");
                return false; // return false;
            }

            // Load the pre-trained Haar cascade classifier for face detection
            CascadeClassifier faceCascade = new CascadeClassifier();
            faceCascade.load("src/main/resources/DigitalAssistant/haarcascades/haarcascade_frontalface_alt.xml");

            boolean faceDetected = false;
            Mat frame = new Mat();
            Mat gray = new Mat();
            Mat faceROI = new Mat();
            String imagePath = "";

            // Loop to continuously capture frames
            while (true) {
                
                cap.read(frame);
                // DOWNSCALE the frame for faster processing
                // Mat downscaledFrame = new Mat();
                //Imgproc.resize(frame, downscaledFrame, new Size(frame.cols() / 1.2, frame.rows() / 1.2));

                // Convert the frame to grayscale for faster processing
                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                // basic processing for face detection
                // 1. denoising with gaussian filter

                Mat filteredImage = new Mat();
                // Apply Gaussian filter
                Imgproc.GaussianBlur(gray, filteredImage, new Size(5, 5), 0);
            
                // Detect faces in the frame using the Haar cascade classifier
                MatOfRect faces = new MatOfRect();
                //faceCascade.detectMultiScale(gray, faces, 1.3, 5);     // set parameters for face detection here
                faceCascade.detectMultiScale(filteredImage, faces, 1.5, 5);     

                // Check if any faces were detected
                if (!faces.empty()) {
                    faceDetected = true;
                    System.out.println("+++ Detected face +++");
                
                    for (Rect rect : faces.toArray()) {
                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 5);   // Draw a rectangle around each face
                        faceROI = gray.submat(rect);
                    }

                    // Save the captured frame as an image file
                    imagePath = "src/main/resources/DigitalAssistant/haarcascades/detectedFace.jpg";
                    Imgcodecs.imwrite(imagePath, frame);
                    our_images.add(faceROI);
                }
                else if ((faces.empty())) {
                    System.out.println("--- No face detected ---");
                    break;
                }

                if (faceDetected) {
                    cap.release();
                    break;
                }
            } 

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Face detection time: " + elapsedTime + " ms");
            
            // Display the saved image
            HighGui.namedWindow("Detected face", HighGui.WINDOW_NORMAL);
            // Mat savedImage = Imgcodecs.imread(imagePath);
            //HighGui.imshow("Detected face", savedImage);
            
            // Release the camera and destroy the window
            cap.release();
            HighGui.destroyAllWindows();
            
            return faceDetected;
        } 
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return false;
        }
    }


    public static void binarization(Mat gray){
        // Perform simple thresholding
        int t = 120;                   // change threshold here
        int maxVal = 255;
        Mat binaryMask = new Mat();
        Imgproc.threshold(gray, binaryMask, t, maxVal, Imgproc.THRESH_BINARY);

        // Save the thresholded image
        Imgcodecs.imwrite("src/main/resources/DigitalAssistant/haarcascades/binaryMask.jpg", binaryMask);
    }


    public static void main(String[] args) {
        FaceDetection fd = new FaceDetection();
        System.out.println("starting face detection...");
        if (fd.faceDetection()) System.out.println("### Haar Cascades successfully applied ###");
        System.out.println("=== finished ===");
    }
}

