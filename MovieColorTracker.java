import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

public class MovieColorTracker {

	public RHImshow 					imM = null;
	public HSVSlidersDialog 			colorHSVDialog = null;
	public VideoCapture 				camera;
	public Mat 							MatImg;
	public Mat 							MatImg_HSV = null;
	public Mat 							MatMask_Ranged = null;
	public JFrame 						jframe = null;
	public JLabel 						vidpanel = null;
	public int 							vidWidth;
	public int 							vidHeight;
	
	public MovieColorTracker() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Welcome to OpenCV " + Core.VERSION);
		MatImg = new Mat();
		MatImg_HSV = new Mat();
		MatMask_Ranged = new Mat();
		imM = new RHImshow("Mask");
		camera = new VideoCapture("Movie/colTest.mp4");

		if (camera.read(MatImg)) {
			vidWidth = MatImg.cols();
			vidHeight = MatImg.rows();
		}
		jframe = new JFrame("Title");
		jframe.setSize(vidWidth,vidHeight);
		jframe.setLocation(0,vidHeight);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setVisible(true);
		
// below are settings to track either the blue, red, or green card in the video. 
// uncomment the two lines of code that define that color and comment out the other four
		int sHueMin = 100;	//use to find blue card in video
		int sHueMax = 120;	//use to find blue card in video
//		int sHueMin = 65;	//use to find green card in video
//		int sHueMax = 85;	//use to find green card in video
//		int sHueMin = 150;	//use to find red card in video
//		int sHueMax = 179;	//use to find red card in video
		
		int sSatMin = 100;
		int sSatMax = 255;
		int sBriMin = 100;
		int sBriMax = 255;
		
		colorHSVDialog = new HSVSlidersDialog(sHueMin,sHueMax,sSatMin,sSatMax,sBriMin,sBriMax);
		colorHSVDialog.setLocation(vidWidth,0);
		colorHSVDialog.setVisible(true);
	}
	
	/** 
	 * creates a new BufferedImage from an OpenCV Mat
	 *
	 * @param inMat the 2D array of image data
	 */
	public static BufferedImage toBufferedImage(Mat inMat) {
		MatOfByte mb = new MatOfByte();
		Imgcodecs.imencode(".jpg", inMat, mb);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) { e.printStackTrace(); }
		return bufferedImage;
	}

	@SuppressWarnings("static-access")
	public Mat makeColorRangeMask() {
		if (colorHSVDialog.hueMinValue<colorHSVDialog.hueMaxValue) {
			Scalar sCol = new Scalar(colorHSVDialog.hueMinValue,colorHSVDialog.satMinValue,colorHSVDialog.briMinValue);
			Scalar eCol = new Scalar(colorHSVDialog.hueMaxValue,colorHSVDialog.satMaxValue,colorHSVDialog.briMaxValue);
			MatMask_Ranged = getColorRangeMask(MatImg_HSV,sCol,eCol);
		} else {
			System.out.println("wrap around hue range");
			Scalar sCol = new Scalar(colorHSVDialog.hueMinValue,colorHSVDialog.satMinValue,colorHSVDialog.briMinValue);
			Scalar eCol = new Scalar(179,colorHSVDialog.satMaxValue,colorHSVDialog.briMaxValue);
			Mat MatMask_Ranged_1 = getColorRangeMask(MatImg_HSV,sCol,eCol);
			sCol = new Scalar(0,colorHSVDialog.satMinValue,colorHSVDialog.briMinValue);
			eCol = new Scalar(colorHSVDialog.hueMaxValue,colorHSVDialog.satMaxValue,colorHSVDialog.briMaxValue);
			Mat MatMask_Ranged_2 = getColorRangeMask(MatImg_HSV,sCol,eCol);
			Core.bitwise_and(MatMask_Ranged_1, MatMask_Ranged_2, MatMask_Ranged);
		}
		return MatMask_Ranged;
	}

	public Mat getColorRangeMask(Mat HSVMat, Scalar sScalar, Scalar eScalar) {
		Mat mask = new Mat();
		Core.inRange(HSVMat, sScalar, eScalar, mask);
		return mask;
	}

	public void updateImages() {
		ImageIcon image = new ImageIcon(toBufferedImage(MatImg));
		vidpanel.setIcon(image);
		vidpanel.repaint();
		imM.showImage(MatMask_Ranged);
	}
	
	public void findContours() {
		 List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
		 Imgproc.findContours(MatMask_Ranged, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		 System.out.println(contours.size() + " contours");
	}
	
	public void operate(){
		int i = 0;
		if (camera.isOpened())
			while (true)
				if (camera.read(MatImg) && !MatImg.empty()) {
					Imgproc.GaussianBlur(MatImg, MatImg, new Size(15,15),50);
					Imgproc.cvtColor(MatImg, MatImg_HSV, Imgproc.COLOR_BGR2HSV);
					MatMask_Ranged = makeColorRangeMask();
					//findContours();
					updateImages();
					System.out.println("Frame#: " + i++);
				}
	}	

	public static void main(String[] args) {
		MovieColorTracker aMovTracker = new MovieColorTracker();
		aMovTracker.operate();
	}
}