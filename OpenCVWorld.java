import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVWorld {
	public static void main (String args[]) {
		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Loaded: " + Core.NATIVE_LIBRARY_NAME.toString());
		VideoCapture camera = new VideoCapture(0);
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!camera.isOpened()) {
			System.out.println("Camera Error");
		} else {
			System.out.println("Camera OK!");
		}
		Mat frame = new Mat();
		camera.read(frame);
		String fileName = "capture1.jpg";
		Imgcodecs.imwrite(fileName, frame);
		System.out.println("Saved Image: " + fileName);
	}
}