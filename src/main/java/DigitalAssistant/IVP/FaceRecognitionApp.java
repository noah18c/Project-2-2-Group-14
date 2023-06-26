package DigitalAssistant.IVP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FaceRecognitionApp {

    public static String eigenFaceRecognition(){
    
        try {
            // Command to execute the Python script
            String pythonScript = "src/main/java/DigitalAssistant/IVP/Recognition.py"; 
            
            String osName = System.getProperty("os.name").toLowerCase();

            ProcessBuilder processBuilder;


            if (osName.contains("mac") || osName.contains("darwin")) {
                processBuilder = new ProcessBuilder("python.app", pythonScript);

            } else if (osName.contains("win")) {
                processBuilder = new ProcessBuilder("python.exe", pythonScript);

            } else {
                processBuilder = new ProcessBuilder("python", pythonScript);
            }

            // Create the ProcessBuilder
            // ProcessBuilder processBuilder = new ProcessBuilder("python.app", pythonScript);

            //ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);
            
            //ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output from the Python script
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Process the output line from the Python script
                //System.out.println(line);
                if(!line.equals("Unknown")){
                    return line;
                }
                else{
                }
            }
            
            // Wait for the Python script to complete
            int exitCode = process.waitFor();
            //System.out.println("Python script exited with code: " + exitCode);
            return "Unknown";

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static void main(String[] args) {
        System.out.println(FaceRecognitionApp.eigenFaceRecognition());
    }
}