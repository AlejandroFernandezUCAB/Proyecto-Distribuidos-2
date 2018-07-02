#!/bin/bash

# estos pasos hay que hacerlos siempre por la libreria de google
# sino dara este error:
# Exception in thread "Thread-0" java.lang.NoClassDefFoundError: com/google/gson/Gson

# javac *.java
export CLASSPATH=./gson-2.6.2.jar:.
java Anillo