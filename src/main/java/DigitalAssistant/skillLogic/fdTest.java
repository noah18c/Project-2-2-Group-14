package DigitalAssistant.skillLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import nu.pattern.OpenCV;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;


public class fdTest {
    private Mat image = new Mat();
    public boolean faceDetected = false;

    public fdTest(Mat image){
        this.image = image;
        faceDetection(image);
    }
    /**
     * Method runs face detection based on Haar cascade classifier!
     * @return 
     */
    public boolean faceDetection(Mat image) {

        Mat frame = image;
        Mat gray = new Mat();
        String imagePath = "";

        try {
            // Load the OpenCV native library
            OpenCV.loadLocally();

            // Load the pre-trained Haar cascade classifier for face detection
            CascadeClassifier faceCascade = new CascadeClassifier();
            faceCascade.load("src/main/resources/DigitalAssistant/haarcascades/haarcascade_frontalface_alt.xml");

            // Loop to continuously capture frames
            while (true) {
                // DOWNSCALE the frame for faster processing!??
                Mat downscaledFrame = new Mat();
                Imgproc.resize(frame, downscaledFrame, new Size(frame.cols() / 1.2, frame.rows() / 1.2));
                // imagePath = "src/main/resources/DigitalAssistant/haarcascades/downscaledFrame.jpg";  // Specify the path to save the image
                // Imgcodecs.imwrite(imagePath, downscaledFrame);
                Mat downScaled_gray = new Mat();

                // Convert the frame to grayscale for faster processing
                //Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.cvtColor(downscaledFrame, downScaled_gray, Imgproc.COLOR_BGR2GRAY);
                
                // basic processing for face detection
                // 1. denoising with gaussian filter
                // 2. histogram equalisation to increase contrast!? applied to HSV image 
                //    grayscaling the result 
                // 3. binarization
                // histEqualization(frame);
                // edgeFilter(gray);
                // binarization(gray);
                //imageSegmentation(frame);
                // Create a blank image to store the filtered result
                Mat filteredImage = new Mat();

                // Apply Gaussian filter
                // Imgproc.GaussianBlur(gray, filteredImage, new Size(7, 7), 0);
                // Imgproc.GaussianBlur(downscaledFrame, filteredImage, new Size(5, 5), 0);
            
                // Detect faces in the frame using the Haar cascade classifier
                MatOfRect faces = new MatOfRect();
                // faceCascade.detectMultiScale(frame, faces, 1.3, 5);  // BGR
                faceCascade.detectMultiScale(downScaled_gray, faces, 1.3, 5);
                // faceCascade.detectMultiScale(gray, faces, 1.3, 5);     // set parameters for face detection here
                // faceCascade.detectMultiScale(filteredImage, faces, 1.3, 5);  // gaussian filter

                // Check if any faces were detected
                if (!faces.empty()) {
                    faceDetected = true;
                    System.out.println("+++ Detected face...");

                    for (Rect rect : faces.toArray()) {
                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 5);   // Draw a rectangle around each face
                    }

                    // Save the captured frame as an image file
                    imagePath = "src/main/resources/DigitalAssistant/haarcascades/detectedFace.jpg";
                    Imgcodecs.imwrite(imagePath, frame);
                }
                else {
                    System.out.println("--- No face detected");
                    break;
                }

                if (faceDetected) {
                    break;
                }
            }
            
            return faceDetected;
        } catch (Exception e) {
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
        int t = 150;                   // change threshold here
        int maxVal = 255;
        Mat binaryMask = new Mat();
        Imgproc.threshold(gray, binaryMask, t, maxVal, Imgproc.THRESH_BINARY);

        // Save the thresholded image
        Imgcodecs.imwrite("src/main/resources/DigitalAssistant/haarcascades/binaryMask.jpg", binaryMask);
    }

    public static void imageSegmentation(Mat imageRGB){
        // System.out.println("starting K-Means...");
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

    public static void writeDataToCSV(ArrayList<Long> measurements, String filePath, int successes, int failures, int totalScanned, int totalNegSamples) {
        try {
            // FileWriter csvWriter = new FileWriter(filePath);
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filePath, true));
            String header = "=== Time in milliseconds - Batch 3 - grayscale & gaussian === ";      // change batch number and ** equHist **

            // Write headers to the CSV file
            csvWriter.append(String.join(",", header));
            csvWriter.append("\n");
            // Convert ArrayList to array
            Long[] array = measurements.toArray(new Long[measurements.size()]);

            // Compute the average
            double average = 0.0;
            if (array.length > 0) {
                long sum = 0;
                for (long value : array) {
                    sum += value;
                }
                average = (double) sum / array.length;
            }
            double accuracy = 0.0;     // ( Tp + Tn ) / (Tp + Tn + Fp + Fn)
            double success = successes;
            double fails = failures;
            accuracy = success / (success + fails);
            // Append the average to the CSV file
            csvWriter.append("*** Number of positive samples scanned: " + totalScanned);
            csvWriter.append("\n");
            csvWriter.append("*** Number of negative samples scanned: " + totalNegSamples);
            csvWriter.append("\n");
            csvWriter.append("*** Average of this batch: " + average);
            csvWriter.append("\n");
            csvWriter.append("*** Correct detection: " + successes);
            csvWriter.append("\n");
            csvWriter.append("*** Incorrect detection: " + failures);
            csvWriter.append("\n");
            // Append the accuracy to the CSV file
            csvWriter.append("*** Accuracy of this batch: " + accuracy);
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void experiment(int batch){
        fdTest fd;
        int successes;
        int failures;
        int truePositives;
        int falsePositives;
        int trueNegatives;
        int falseNegatives;
        int totalScanned;
        int totalNegSamples;
        System.out.println("starting face detection TESTING ...");

        String folderPath_PosSamples = "src/main/resources/DigitalAssistant/haarcascades/Caltech_WebFaces";
        String folderPath_NegSamples = "src/main/resources/DigitalAssistant/haarcascades/NegSamples";
        String imgPathBase3 = "/pic00";       // 00100 <= x <= 00999
        String imgPathBase4 = "/pic0";        // 01000 <= x <= 09999
        String imgPathBase5 = "/pic";        // >= 10000
        String filePath = "src/main/resources/DigitalAssistant/haarcascades/experiment1.data"; // exp. 1 no pre processing
        
        // Read JPG images one at a time and save them as Mat
        if (batch == 3){
            truePositives = 0;
            falsePositives = 0;
            trueNegatives = 0;
            falseNegatives = 0;
            successes = 0;
            failures = 0;
            totalScanned = 0;
            totalNegSamples = 0;
            ArrayList<Long> measurements = new ArrayList<>();
            // test model on positive samples
            for (int i = 100; i <= 999; i++) {
                String digit = String.valueOf(i);
                String imagePath = folderPath_PosSamples + imgPathBase3 + digit + ".jpg";
                File file = new File(imagePath);
                if (file.exists()) {
                    totalScanned++;
                    Mat img = Imgcodecs.imread(imagePath);
                    long startTime = System.currentTimeMillis();
                    fd = new fdTest(img);
                    if (fd.faceDetected) truePositives++;
                    if (!fd.faceDetected) falseNegatives++;
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    measurements.add(elapsedTime);
                    System.out.println("Face detection time: " + elapsedTime + " ms");
                }        
            }
            // test model on negative samples
            for (int j = 1; j <= 12; j++) {
                String folder = folderPath_NegSamples + "/cat_" + String.valueOf(j);
                for (int i = 10; i <= 60; i++) {
                    //System.out.println("index: " + i);
                    String digit = String.valueOf(i);
                    String imgPath = folder + "/image_" + "00" + digit + ".jpg";
                    // src/main/resources/DigitalAssistant/haarcascades/NegSamples/cat_1/image_0010.jpg
                    File file = new File(imgPath);
                    if (file.exists()) {
                        totalNegSamples++;
                        Mat img = Imgcodecs.imread(imgPath);
                        long startTime = System.currentTimeMillis();
                        fd = new fdTest(img);
                        if (fd.faceDetected) falsePositives++;
                        if (!fd.faceDetected) trueNegatives++;
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        measurements.add(elapsedTime);
                        System.out.println("Face detection time: " + elapsedTime + " ms");
                    } 
                }
            }
            System.out.println("faces scanned: " + totalScanned);
            System.out.println("negative samples scanned: " + totalNegSamples);
            successes = truePositives+trueNegatives;
            failures = falsePositives+falseNegatives;
            writeDataToCSV(measurements, filePath, successes, failures, totalScanned, totalNegSamples);
        }
        if (batch == 4){
            truePositives = 0;
            falsePositives = 0;
            trueNegatives = 0;
            falseNegatives = 0;
            successes = 0;
            failures = 0;
            totalScanned = 0;
            totalNegSamples = 0;
            ArrayList<Long> measurements = new ArrayList<>();
            // test model on positive samples
            for (int i = 1000; i <= 1920; i++) {
                String digit = String.valueOf(i);
                String imagePath = folderPath_PosSamples + imgPathBase4 + digit + ".jpg";
                File file = new File(imagePath);
                if (file.exists()) {
                    totalScanned++;
                    Mat img = Imgcodecs.imread(imagePath);
                    long startTime = System.currentTimeMillis();
                    fd = new fdTest(img);
                    if (fd.faceDetected) truePositives++;
                    if (!fd.faceDetected) falseNegatives++;
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    measurements.add(elapsedTime);
                    System.out.println("Face detection time: " + elapsedTime + " ms");
                } 
            }
            // test model on negative samples
            for (int j = 1; j <= 12; j++) {
                String folder = folderPath_NegSamples + "/cat_" + String.valueOf(j);
                for (int i = 10; i <= 60; i++) {
                    //System.out.println("index: " + i);
                    String digit = String.valueOf(i);
                    String imgPath = folder + "/image_" + "00" + digit + ".jpg";
                    // src/main/resources/DigitalAssistant/haarcascades/NegSamples/cat_1/image_0010.jpg
                    File file = new File(imgPath);
                    if (file.exists()) {
                        totalNegSamples++;
                        Mat img = Imgcodecs.imread(imgPath);
                        long startTime = System.currentTimeMillis();
                        fd = new fdTest(img);
                        if (fd.faceDetected) falsePositives++;
                        if (!fd.faceDetected) trueNegatives++;
                        long endTime = System.currentTimeMillis();
                        long elapsedTime = endTime - startTime;
                        measurements.add(elapsedTime);
                        System.out.println("Face detection time: " + elapsedTime + " ms");
                    } 
                }
            }
            System.out.println("faces scanned: " + totalScanned);
            System.out.println("negative samples scanned: " + totalNegSamples);
            successes = truePositives+trueNegatives;
            failures = falsePositives+falseNegatives;
            writeDataToCSV(measurements, filePath, successes, failures, totalScanned, totalNegSamples);
        }
    }

    public static void main(String[] args) {
        // Load the OpenCV native library
        OpenCV.loadLocally();
        experiment(4);
        System.out.println("=== finished ===");
    
    }
}

