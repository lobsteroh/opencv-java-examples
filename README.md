# opencv-java-examples
a set of java programs that provide short examples for OpenCV functions and how to call them from Eclipse IDE with java. You need a computer with a functional installation for a source of video frames (e.g., usb webcam, Raspberry Pi camera module). This repository assumes that you have already (1) installed opencv with java bindings, (2) installed Eclipse, (3) opened Eclipse and set its default workspace, and (4) created a UserLibrary for your opencv jar file within Eclipse. I am using this on MacOSX 10.11.6 (with Eclipse Oxygen Release Milestone 3 (4.7.0 M3) Build id: 20161103-0824, Java SE Runtime Environment (build 1.8.0_112-b16), OpenCV 3.1.0) and Raspberry Pi 3 Model B (Raspbian Jessie, Camera Module V2 with UV4L Driver, oracle-java8-jdk, opencv-3.1.0)

   - create folder 'opencv-java-examples' in your default Eclipse workspace
   - create folder 'src' inside this folder
   - copy the *.java files from this repository into the src folder
   - open Eclipse and choose menu File -> New -> Java Project
   - enter project name 'opencv-java-examples' and click the 'Finish' button. The Eclipse project wizzard will set things up and create a Project in your Project Explorer
   - in the Package Explorer select the Project 'opencv-java-examples' and choose menu Project -> Properties
   - click on button "Add Library...', choose 'User Library', and click 'Next>'
   - select your userlibrary for opencv and click Buttons for 'Apply' and 'OK'
   - in your Package Explorer's Project src folder choose any of the java source files and click the Run Icon (i.e., green circle with white tirangle inside)

Enjoy,
