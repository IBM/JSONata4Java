#! /bin/bash
export OLD_JAVA_HOME=$JAVA_HOME
export OLD_PATH=$PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.17+8/Contents/Home
export PATH=$JAVA_HOME:$PATH
mvn clean install deploy -Prelease
export JAVA_HOME=$OLD_JAVA_HOME
export PATH=$OLD_PATH

