from py4j.clientserver import ClientServer, JavaParameters, PythonParameters

import numpy
import cv2


class FaceDetection(object):
    # This class handles object detection -> threshold based or Haar Cascade classifier (more sophisticated)

    def faceDetection(self):
        print("==== Hello there, the file is being executed. Hang on...")
        print("==== Starting face detection...")
        # Open the default camera (index 0)
        cap = cv2.VideoCapture(0)
        # Load the Haar cascade classifier for face detection
        face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
        eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')
        face_detected = False
        # Loop to continuously capture frames
        while True:
            # Read a new frame from the camera
            ret, frame = cap.read()

            # Convert the frame to grayscale for faster processing
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # Detect faces in the frame using the Haar cascade classifier
            faces = face_cascade.detectMultiScale(gray, 1.3, 5)

            # Check if any faces were detected
            if len(faces) > 0:
                face_detected = True
                # Draw a rectangle around each face
                for (x, y, w, h) in faces:
                    cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 5)
                    face_gray = gray[y:y + h, x:x + w]
                    face_color = frame[y:y + h, x:x + w]
                    # eye detection
                    eyes = eye_cascade.detectMultiScale(face_gray, 1.3, 5)
                    for (ex, ey, ew, eh) in eyes:
                        cv2.rectangle(face_color, (ex, ey), (ex + ew, ey + eh), (255, 0, 0), 5)

                # Display the frame with the detected faces in a window
                cv2.imshow('Object Detection', frame)

            # Check if the user pressed the 'q' key to quit
            if cv2.waitKey(1) == ord('q'):
                break

            # Check if the user pressed the 'esc' key to quit
            if cv2.waitKey(1) == 27:
                break
        print("Hello there...")
        # Release the camera and destroy the window
        cap.release()
        cv2.destroyAllWindows()

        print(face_detected)

        return True

    class Java:
        implements = ["DigitalAssistant.python.IFaceDetection"]


# Make sure that the python code is started first.
# Then execute: java -cp py4j.jar py4j.examples.SingleThreadClientApplication
facedetection = FaceDetection()
gateway = ClientServer(
    java_parameters=JavaParameters(),
    python_parameters=PythonParameters(),
    python_server_entry_point=facedetection)
