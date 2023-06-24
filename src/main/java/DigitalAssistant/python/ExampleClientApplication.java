package DigitalAssistant.python;
import py4j.ClientServer;

import java.util.Arrays;


public class ExampleClientApplication {

    public static void main(String[] args) {
        ClientServer clientServer = new ClientServer(null);
        // We get an entry point from the Python side
        IHello hello = (IHello) clientServer.getPythonServerEntryPoint(new Class[] { IHello.class });

        //IFaceDetection faceDetection = (IFaceDetection) clientServer.getPythonServerEntryPoint(new Class[] { IFaceDetection.class });
        // Java calls Python without ever having been called from Python
        System.out.println(hello.sayHello());
        //System.out.println(Arrays.toString(hello.sayHello(10, 12)));
        //System.out.println(faceDetection.FaceDetection());

        clientServer.shutdown();
    }
}
