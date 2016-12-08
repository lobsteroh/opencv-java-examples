import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class CameraColorTracker {

	VideoCapture				camera = null;
	Mat 						MatImg = null;
	Mat 						MatImg_HSV = null;
	Mat 						MatMask_Ranged = null;
	RHImshow 					im = null;
//	HSVSlidersDialog 			colorHSBDialog = null;

//	int 						sHueMin = 100;	//for blue
//	int 						sHueMax = 120;	//for blue
//	int 						sHueMin = 65;	//for green
//	int 						sHueMax = 85;	//for green
	int 						sHueMin = 150;	//for red
	int 						sHueMax = 179;	//for red
	int 						sSatMin = 150;
	int 						sSatMax = 255;
	int 						sBriMin = 150;
	int 						sBriMax = 255;
	
	public CameraColorTracker() {
		powerUp();
		MatImg = new Mat();
		MatImg_HSV = new Mat();
		MatMask_Ranged = new Mat();
		im = new RHImshow("showImg");
//		colorHSBDialog = new HSVSlidersDialog(sHueMin,sHueMax,sSatMin,sSatMax,sBriMin,sBriMax);
//		colorHSBDialog.setVisible(true);
	}

	public void powerUp() {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("Welcome to OpenCV " + Core.VERSION);
			camera = new VideoCapture(0);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void operate() {
		if (camera.isOpened())
			while (true)
				if (camera.read(MatImg) && !MatImg.empty()) {
					Imgproc.GaussianBlur(MatImg, MatImg, new Size(15,15),50);
					Imgproc.cvtColor(MatImg, MatImg_HSV, Imgproc.COLOR_BGR2HSV);
					Scalar sCol = new Scalar(sHueMin,sSatMin,sBriMin);
					Scalar eCol = new Scalar(sHueMax,sSatMax,sBriMax);
					Core.inRange(MatImg_HSV, sCol, eCol, MatMask_Ranged);
					im.showImage(MatMask_Ranged);
				}
	}	

	public static void main(String[] args) {
		System.out.println("This sample program masks the live video to only showpixels within the requested color range");
		CameraColorTracker anImager = new CameraColorTracker();
		anImager.operate();
		System.out.println(anImager.toString());
	}
}
