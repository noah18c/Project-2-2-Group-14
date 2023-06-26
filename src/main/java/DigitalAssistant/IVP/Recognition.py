import cv2
import numpy as np
import glob
from sklearn.decomposition import PCA
import os
import tkinter as tk
from PIL import Image, ImageTk
import time



class FaceRecognitionApp:
    def __init__(self, dataset_dir, num_eigen_faces=4, threshold=0.3):
        self.dataset_dir = dataset_dir
        self.num_eigen_faces = num_eigen_faces
        self.threshold = threshold

    def createDataMatrix(self, images):
        num_images = len(images)
        data = np.empty((num_images, images[0].size), dtype=np.uint8)
        for i in range(num_images):
            image = images[i].flatten()
            data[i, :] = image
        return data

    def calculateWeights(self, images, mean, eigenVectors):
        num_images = len(images)
        weights = []
        for i in range(num_images):
            image = images[i].flatten()
            weight = np.dot(eigenVectors, (image - mean))
            weights.append(weight)
        return weights

    def recognize_face(self, test_image):
        MaximalMinDistance = 100

        # Step 1: Iterate over each folder (person) in the dataset
        for person_dir in glob.glob(os.path.join(self.dataset_dir, '*')):
            if not os.path.isdir(person_dir):
                continue

            # Step 2: Load and preprocess the images for the current person
            image_paths = glob.glob(os.path.join(person_dir, '*.jpg'))
            images = []
            for path in image_paths:
                img = cv2.imread(path, 0)  # Load images in grayscale
                images.append(img)

            # Size of images
            sz = images[0].shape

            # Create data matrix for PCA
            data = self.createDataMatrix(images)

            # Compute the eigenvectors from the stack of images created
            mean, eigenVectors = cv2.PCACompute(data, mean=None, maxComponents=self.num_eigen_faces)

            # Calculate the weights for the training set
            training_weights = self.calculateWeights(images, mean[0], eigenVectors)

            # Normalize the weights
            training_weights = np.asarray(training_weights)
            training_weights /= np.linalg.norm(training_weights, axis=1, keepdims=True)

            # Calculate the weight for the test image
            test_weight = np.dot(eigenVectors, (test_image.flatten() - mean[0]))
            test_weight /= np.linalg.norm(test_weight)

            # Calculate the distance between the test weight and each training weight
            distances = np.linalg.norm(training_weights - test_weight, axis=1)

            # Find the closest matching label
            min_distance_idx = np.argmin(distances)
            min_distance = distances[min_distance_idx]
            
            if min_distance < MaximalMinDistance:
                MaximalMinDistance = min_distance
                person_name = os.path.basename(person_dir)
            

        return MaximalMinDistance , person_name , self.threshold

    def capture_images(self, person_name, output_dir, num_images, image_width, image_height):
        # Create a folder for the person if it doesn't exist
        person_dir = os.path.join(output_dir, person_name)
        os.makedirs(person_dir, exist_ok=True)

        video_capture = cv2.VideoCapture(0)  # Use the default camera (index 0)

        # Load the face cascade for face detection
        face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

        image_count = 0  # Counter for the captured images

        while image_count < num_images:
            ret, frame = video_capture.read()

            # Convert the frame to grayscale for face detection
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # Detect faces in the frame
            faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

            # Draw bounding boxes around detected faces and save the cropped face
            for (x, y, w, h) in faces:
                cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

                # Crop the face region
                face_crop = frame[y:y + h, x:x + w]

                # Resize the cropped face image
                resized_face = cv2.resize(face_crop, (image_width, image_height))

                # Save the resized face image
                image_path = os.path.join(person_dir, "image" + str(image_count + 1) + ".jpg")
                #image_path = os.path.join(person_dir, f"image{image_count + 1}.jpg")
                cv2.imwrite(image_path, resized_face)

                image_count += 1
                #print("Saved {}/{} images. ({:.2f}% completed)".format(image_count, num_images, (image_count / num_images) * 100))
                #print(f"Saved {image_count}/{num_images} images. ({(image_count / num_images) * 100:.2f}% completed)")

                if image_count >= num_images:
                    break

                cv2.imshow('Camera', frame)
                cv2.waitKey(1000)  # 1-second delay

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        video_capture.release()
        cv2.destroyAllWindows()

  
    def capture_single_face(self,image_width , image_height):
        # Load the face cascade for face detection
        face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

        # Open the default camera
        video_capture = cv2.VideoCapture(0)

        while True:
            # Capture frame-by-frame
            ret, frame = video_capture.read()

            # Convert the frame to grayscale for face detection
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # Detect faces in the frame
            faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

            # Display the frame with bounding boxes around detected faces
            for (x, y, w, h) in faces:
                cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            # Check if a face is detected
            if len(faces) == 1:
                # Crop the face region
                (x, y, w, h) = faces[0]
                face_crop = frame[y:y + h, x:x + w]

                # Release the video capture and close the window
                video_capture.release()
                cv2.destroyAllWindows()

                # Resize the face crop to the target size
                face_resized = cv2.resize(face_crop, (image_width,image_height))
                face_resized = cv2.cvtColor(face_resized, cv2.COLOR_BGR2GRAY)
                
                cv2.imwrite("capturedImage.jpg",face_resized)
                
                return face_resized
    
            # Check for 'q' key press to exit
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        # Release the video capture and close the window
        video_capture.release()
        cv2.destroyAllWindows()
        return None
    
class FaceRecognitionGUI:
    def __init__(self, app):
        self.app = app
        self.face_recognition = FaceRecognitionApp(dataset_dir='dataset')

        # Create the main window
        self.main_window = tk.Tk()
        self.main_window.title("Face Recognition App")

        # Create the buttons
        self.recognition_button = tk.Button(self.main_window, text="Person Recognition", command=self.recognize_person)
        self.recognition_button.pack()

        self.introduction_button = tk.Button(self.main_window, text="Person Introduction", command=self.introduce_person)
        self.introduction_button.pack()
        
        # Create the label for displaying the result
        self.result_label = tk.Label(self.main_window, text="")
        self.result_label.pack()

    def recognize_person(self):
        
        # Capture a single face image
        a = 0
        recognized_person = "Unknown"
        distances = []
        while True:
            a+=1

            face_image = self.face_recognition.capture_single_face(image_width=256,image_height=256)

            # Perform face recognition on the captured image
            if face_image is not None:
                distance ,person_name,threshold = self.face_recognition.recognize_face(face_image)
                distances.append((distance,person_name,threshold))

            if a == 5:
                break
        
        # Sort the results based on distance
        sorted_results = sorted(distances, key=lambda x: x[0])

        # Retrieve the entry with the smallest distance
        smallest_distance_entry = sorted_results[0]
        smallest_distance, smallest_person_name, smallest_threshold = smallest_distance_entry
                
        if smallest_distance < smallest_threshold:
            self.result_label.config(text=person_name)
            recognized_person = smallest_person_name
        #     #print(recognized_person)
        #     time.sleep(2)  # Wait for 2 seconds before closing
        #     self.main_window.destroy()
        
        else:
            self.result_label.config(text="Unknown")
            recognized_person = "Unknown"
        #     #print("Unknown person")


        # if recognized_person is not None:
        #     self.result_label.config(text=recognized_person)
        #     #print(recognized_person)
        #     time.sleep(2)  # Wait for 2 seconds before closing
        #     self.main_window.destroy()
            
        #     break
        # else:
        #     self.result_label.config(text="Unknown")
        #     recognized_person = "Unknown"
        #     #print("Unknown person")
    
        print(recognized_person , smallest_distance)    
        self.main_window.quit()

    def introduce_person(self):
        # Create a new window for person introduction
        introduction_window = tk.Toplevel(self.main_window)
        introduction_window.title("Person Introduction")

        # Create a text entry for entering the person's name
        name_label = tk.Label(introduction_window, text="Person's Name:")
        name_label.pack()
        name_entry = tk.Entry(introduction_window)
        name_entry.pack()

        # Create a button to capture images for the person
        capture_button = tk.Button(introduction_window, text="Capture Images",
                                   command=lambda: self.capture_images(name_entry.get()))
        capture_button.pack()

    def capture_images(self, person_name):
        # Capture multiple images for the person
        self.face_recognition.capture_images(person_name, output_dir='dataset',
                                             num_images=10, image_width=256, image_height=256)

    def run(self):
        # Start the GUI event loop
        self.main_window.mainloop()


 # Create the FaceRecognitionApp instance and the GUI
app = FaceRecognitionApp(dataset_dir='dataset')
gui = FaceRecognitionGUI(app)
# Run the GUI
gui.run()