#! /bin/bash
export OLD_JAVA_HOME=$JAVA_HOME
export OLD_PATH=$PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/ibm-semeru-open-17.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
gpg --version
echo "test" | gpg --clearsign

mvn clean install deploy -Prelease
export JAVA_HOME=$OLD_JAVA_HOME
export PATH=$OLD_PATH

