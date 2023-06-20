from py4j.clientserver import  ClientServer, JavaParameters, PythonParameters
import math
import random
import numpy as np
import pandas as pd
import nltk
import keras
import tensorflow as tf


class SimpleHello(object):

    def sayHello(self, int_value=None, string_value=None):
        print(int_value, string_value)

        banana = np.array([1, 2, 3])

        java_array = gateway.new_array(gateway.jvm.int, len(banana))
        for i in range(len(banana)):
            java_array[i] = int(banana[i])

        return java_array

    class Java:
        implements = ["DigitalAssistant.python.IHello"]

# Make sure that the python code is started first.
# Then execute: java -cp py4j.jar py4j.examples.SingleThreadClientApplication
simple_hello = SimpleHello()
gateway = ClientServer(
    java_parameters=JavaParameters(),
    python_parameters=PythonParameters(),
    python_server_entry_point=simple_hello)
