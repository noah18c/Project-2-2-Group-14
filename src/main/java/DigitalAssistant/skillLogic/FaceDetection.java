package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.List;
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
        faceDetection();
    }
    /**
     * Method runs face detection based on Haar cascade classifier!
     * @return 
     */
    public boolean faceDetection() {
        try {
            ArrayList<Mat> our_images = new ArrayList<>();
            int successes = 0;
            int failures = 0;
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

            // image segmentation for simple threshold based detection!!!?

            boolean faceDetected = false;
            Mat frame = new Mat();
            Mat gray = new Mat();
            Mat faceROI = new Mat();
            String imagePath = "";

            // Loop to continuously capture frames
            while (true) {
                
                cap.read(frame);
                // DOWNSCALE the frame for faster processing
                Mat downscaledFrame = new Mat();
                Imgproc.resize(frame, downscaledFrame, new Size(frame.cols() / 2, frame.rows() / 2));
                imagePath = "src/main/resources/DigitalAssistant/haarcascades/downscaledFrame.jpg";  // Specify the path to save the image
                Imgcodecs.imwrite(imagePath, downscaledFrame);

                // Convert the frame to grayscale for faster processing
                Imgproc.cvtColor(downscaledFrame, gray, Imgproc.COLOR_BGR2GRAY);
                // Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                // basic processing for face detection
                // 1. denoising with gaussian filter
                // 2. histogram equalisation to increase contrast!? applied to HSV image 
                //    grayscaling the result 
                // 3. binarization
                // histEqualization(frame);
                // edgeFilter(gray);
                // binarization(gray);
                // imageSegmentation(frame);
            
                // Detect faces in the frame using the Haar cascade classifier
                MatOfRect faces = new MatOfRect();
                faceCascade.detectMultiScale(gray, faces, 1.3, 5);     // set parameters for face detection here

                // Check if any faces were detected
                if (!faces.empty()) {
                    faceDetected = true;
                    successes++;
                    System.out.println("+++ Detected face...");
                    System.out.println("Successes: " + successes);

                    for (Rect rect : faces.toArray()) {
                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 5);   // Draw a rectangle around each face
                        faceROI = gray.submat(rect);
                    }

                    // Save the captured frame as an image file
                    imagePath = "src/main/resources/DigitalAssistant/haarcascades/detectedFace.jpg";
                    Imgcodecs.imwrite(imagePath, frame);
                    our_images.add(faceROI);
                }
                else {
                    System.out.println("--- No face detected");
                    failures++;
                    System.out.println("Failures: " + failures);
                    break;
                }

                if (faceDetected) {
                    cap.release();
                    break;
                }
            }
            // if (faceDetected) {
            //     //System.out.println("+++ Face saved to array : " + our_images.get(0));
            //     System.out.println("*** The detected face has the following image path: " + imagePath);
            // }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Face detection time: " + elapsedTime + " ms");
            
            // Display the saved image
            HighGui.namedWindow("Detected face", HighGui.WINDOW_NORMAL);
            // Mat savedImage = Imgcodecs.imread(imagePath);
            //HighGui.imshow("Detected face", savedImage);

            // Check if the user pressed the 'q' key to quit
            // if (HighGui.waitKey(1) == '\u0071' || HighGui.waitKey(1) == 27) {
            //     // User pressed 'q' or 'esc' key, terminate the program
            //     HighGui.waitKey(0);
            //     HighGui.destroyAllWindows();
            //     return faceDetected;
            // }
            
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

    public static void edgeFilter(Mat gray){
        // Sobel kernels for 45 degrees and 135 degrees
        Mat k45 = new Mat(3, 3, CvType.CV_32F);
        k45.put(0, 0, 2, 1, 0, 1, 0, -1, 0, -1, -2);

        Mat k135 = new Mat(3, 3, CvType.CV_32F);
        k135.put(0, 0, 0, 1, 2, -1, 0, 1, -2, -1, 0);

        // Apply filters to images
        Mat filter45 = new Mat();
        Imgproc.filter2D(gray, filter45, -1, k45);

        Mat filter45n = new Mat();
        Mat filterCombo = new Mat();
        Core.subtract(Mat.zeros(3, 3, CvType.CV_32F), k45, filterCombo);
        Imgproc.filter2D(gray, filter45n, -1, filterCombo);

        // Combine the negative and positive filter images
        Mat combinedFilters = new Mat();
        Core.add(filter45, filter45n, combinedFilters);
        // Save the thresholded image
        Imgcodecs.imwrite("src/main/resources/DigitalAssistant/haarcascades/edges.jpg", combinedFilters);

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

    public static void imageSegmentation(Mat imageRGB){
        System.out.println("starting K-Means...");
        // Reshape the image to a 2D matrix where each row represents a pixel
        Mat reshapedImage = imageRGB.reshape(1, imageRGB.cols() * imageRGB.rows());
        reshapedImage.convertTo(reshapedImage, CvType.CV_32F);

        // Define the parameters for the k-means algorithm
        int K = 8; // Number of clusters
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 0.1);
        int attempts = 10;
        int flags = Core.KMEANS_PP_CENTERS;

        // Perform k-means clustering
        Mat labels = new Mat();
        Mat centers = new Mat();
        Core.kmeans(reshapedImage, K, labels, criteria, attempts, flags, centers);

        // Reshape the labels matrix back to the original image shape
        Mat segmentedLabels = labels.reshape(1, imageRGB.rows());
        segmentedLabels.convertTo(segmentedLabels, CvType.CV_8U);

        // Display the segmented image or save it to a file
        Imgcodecs.imwrite("src/main/resources/DigitalAssistant/haarcascades/segmentedIMG.jpg", segmentedLabels);
    }

    public static void histEqualization(Mat img){
        // Convert BGR image to HSV color space
        Mat hsvImg = new Mat();
        Imgproc.cvtColor(img, hsvImg, Imgproc.COLOR_BGR2HSV);

        // Split BGR channels
        Mat b = new Mat();
        Mat g = new Mat();
        Mat r = new Mat();
        Core.extractChannel(img, b, 0);
        Core.extractChannel(img, g, 1);
        Core.extractChannel(img, r, 2);

        // Create zero-filled matrices of the same size as the original image
        Mat b1 = Mat.zeros(img.size(), img.type());
        Mat g1 = Mat.zeros(img.size(), img.type());
        Mat r1 = Mat.zeros(img.size(), img.type());

        // // Assign channels to the corresponding matrices
        // Core.merge(new Mat[]{b, b1.submat(0, b.rows(), 0, b.cols()), b1.submat(0, b.rows(), 2, b.cols() + 2)}, b1);
        // Core.merge(new Mat[]{g1.submat(0, g.rows(), 1, g.cols() + 1, g1), g, g1.submat(0, g.rows(), 2, g.cols() + 2, g1)}, g1);
        // Core.merge(new Mat[]{r1.submat(0, r.rows(), 0, r.cols(), r1), r1.submat(0, r.rows(), 1, r.cols() + 1, r1), r}, r1);

        // Normalize the Hue channel
        Mat h = new Mat();
        Core.extractChannel(hsvImg, h, 0);
        Mat hNorm = new Mat();
        Core.normalize(h, hNorm, 0, 179, Core.NORM_MINMAX);

        // Split HSV channels
        Mat s = new Mat();
        Mat v = new Mat();
        Core.extractChannel(hsvImg, s, 1);
        Core.extractChannel(hsvImg, v, 2);

        // Equalize the channels
        Mat dst1 = new Mat();
        Mat dst2 = new Mat();
        Mat dst3 = new Mat();
        Imgproc.equalizeHist(hNorm, dst1);
        Imgproc.equalizeHist(s, dst2);
        Imgproc.equalizeHist(v, dst3);

        // Merge equalized channels
        Mat mergeImg1 = new Mat();
        List<Mat> mergeChannels = new ArrayList<>();
        mergeChannels.add(dst1);
        mergeChannels.add(dst2);
        mergeChannels.add(dst3);
        Core.merge(mergeChannels, mergeImg1);

        // Convert merged image to BGR color space
        Mat finalImg1 = new Mat();
        Imgproc.cvtColor(mergeImg1, finalImg1, Imgproc.COLOR_HSV2BGR);

        // Display the equalized BGR image
        Imgcodecs.imwrite("src/main/resources/DigitalAssistant/haarcascades/equImg.jpg", finalImg1);
    }

    public static void main(String[] args) {
        FaceDetection fd = new FaceDetection();
        System.out.println("starting face detection...");
        if (fd.faceDetection()) System.out.println("### Haar Cascades successfully applied ###");
        System.out.println("=== finished ===");
    }
}

