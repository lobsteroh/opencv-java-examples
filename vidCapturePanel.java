import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class vidCapturePanel extends JPanel {
	/**
	 * 
	 */
	private static final long 			serialVersionUID = 5176681693976942429L;
	public static Mat 					MatImg;
    public static Mat 					MatImg_mod;
	public static VideoCapture			camera;
	public BufferedImage 				buffImg;
	public int							counter = 0;
	public long 						startTime;
	public long 						frameTime;
	public boolean 						grayScale = false;
	
	// !!!: Constructors
	/** 
	 * Standard constructor for video capture panel
	 */
	public vidCapturePanel() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Welcome to OpenCV " + Core.VERSION);
		powerUp();
	}

	/** 
	 * power up system for video capture
	 */
	public void powerUp() throws Exception {
		MatImg = new Mat();
		MatImg_mod = new Mat();
		camera = new VideoCapture(0);
		if (camera.isOpened()) {
			System.out.println("Camera opened");
			if (camera.read(MatImg) && !MatImg.empty()) {
				System.out.println("Frame read");
				System.out.println("Width " + MatImg.width() + " Height " + MatImg.height());
				if (grayScale)
					Imgproc.cvtColor(MatImg, MatImg_mod, Imgproc.COLOR_RGB2GRAY);
				else
					MatImg_mod = MatImg;
			    buffImg = makeBufferedImage(MatImg_mod);
			}
		} else
			System.err.println("No Camera");
	}

	/** 
	 * power up system for video capture
	 */
	public void startCapture() {
		if (camera.isOpened()) {
			Runnable r = new Runnable() {
				public void run() {
					while (true) execute();
				}
			};
			startTime = System.currentTimeMillis();
			(new Thread(r)).start();
		}
	}
	
	/** 
	 * Standard constructor for video capture panel
	 */
	public void execute() {
		frameTime = System.currentTimeMillis();
		if (camera.read(MatImg))
			if (!MatImg.empty()) {
				counter++;
				if (grayScale)
				    Imgproc.cvtColor(MatImg, MatImg_mod, Imgproc.COLOR_BGR2GRAY);
				else
					MatImg_mod = MatImg;
				fillBufferedImage(MatImg_mod,buffImg);
				repaint();
			}
	}

	/** 
	 * creates a new BufferedImage from an OpenCV Mat
	 *
	 * @param inMat the 2D array of image data
	 */
	public static BufferedImage makeBufferedImage(Mat inMat) {
		MatOfByte mb = new MatOfByte();
		Imgcodecs.imencode(".jpg", inMat, mb);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) { e.printStackTrace(); }
		return bufferedImage;
	}

	/** 
	 * fills an existing BufferedImage from the pixel info in an OpenCV Mat
	 *
	 * @param inMat the 2D array of image data
	 */
	public static void fillBufferedImage(Mat srcMat, BufferedImage dstBuff) {
		int bufferSize = srcMat.channels()*srcMat.cols()*srcMat.rows();
		byte[] srcBytes = new byte[bufferSize];
		srcMat.get(0,0,srcBytes);
		final byte[] dstBytes = ((DataBufferByte) dstBuff.getRaster().getDataBuffer()).getData();
		System.arraycopy(srcBytes, 0, dstBytes, 0, bufferSize);
	}

 /** 
 * repaint the panel
 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (buffImg != null)
			g.drawImage(buffImg,0,0,buffImg.getWidth(),buffImg.getHeight(), null);
	}
	    
	 /** 
	 * Standard constructor for video capture panel
	 */
	public void finishUp() {
		double timeDiffSecs = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.println(counter + " frames; " + timeDiffSecs + " secs; " + (double)counter/timeDiffSecs + " fps");
	}

	public static void main(String[] args) {
		try {
			final vidCapturePanel vidPanel = new vidCapturePanel();
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
		        public void windowClosing(WindowEvent e) {
		        	vidPanel.finishUp();
		        }
		      });			
			frame.add(vidPanel);
			frame.setSize(MatImg.width(),MatImg.height());  
			frame.setVisible(true);				
			vidPanel.startCapture();			
		} catch (Exception e) { e.printStackTrace(); }
	}
}
