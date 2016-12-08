import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class MinimalApp {
	
	public static void main(String[] args) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("Welcome to OpenCV " + Core.VERSION);
			VideoCapture camera = new VideoCapture(0);
			if (camera.isOpened()) {
				System.out.println("Camera opened");
				RHImshow im = new RHImshow("showVideo");
				Mat frame = new Mat();
				while (true) {
					if (camera.read(frame))
						im.showImage(frame);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
