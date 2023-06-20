package DigitalAssistant.python;
import py4j.ClientServer;

import java.util.Arrays;


public class ExampleClientApplication {

    public static void main(String[] args) {
        ClientServer clientServer = new ClientServer(null);
        // We get an entry point from the Python side
        IHello hello = (IHello) clientServer.getPythonServerEntryPoint(new Class[] { IHello.class });
        // Java calls Python without ever having been called from Python
        System.out.println(Arrays.toString(hello.sayHello()));
        System.out.println(Arrays.toString(hello.sayHello(2, "Hello World")));
        clientServer.shutdown();
    }
}
