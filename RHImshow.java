/*
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the
 copyright notice and the following two paragraphs appear in all
 copies of this software.

 IN NO EVENT SHALL LOBSTERMAN BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, 
 SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE 
 OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF LOBSTERMAN HAS BEEN 
 ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 LOBSTERMAN SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT 
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" 
 BASIS, AND LOBSTERMAN HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, 
 UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

 Online Documentation for this class can be found at
 http://caspar.bgsu.edu/~software/Java_Support/html/
 */

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * implements support functions for images used in the JavaGrinders library
 *
 * @author      robert <a href="mailto:lobsterman.bgsu@gmail.comu">lobsterman</a> huber
 * @version		5.6.0
 */
public class RHImshow extends JFrame {
	
	/**
	 * 
	 */
	private static final long 	serialVersionUID = 1L;
	private int					frameBorder = 11;
	
	/**
	 * standard constructor
	 *
	 * @param wTitle window title
	 */
	public RHImshow(String wTitle) {
		super(wTitle);
	}

	/** 
	 * converts an image from OpenCV to java space
	 *
	 * @param inMat the 2D array of image data
	 */
	public static BufferedImage makeBufferedImage(Mat inMat) {
		MatOfByte mb = new MatOfByte();
		Imgcodecs.imencode(".jpg",inMat,mb);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) { e.printStackTrace(); }
		return bufferedImage;
	}

	/** 
	 * converts an image from OpenCV to java space
	 *
	 * @param inMat the 2D array of image data
	 */
	public void showImage(Mat inMat) {
		BufferedImage anImgBuff = makeBufferedImage(inMat);
	    showImage(anImgBuff);
	}

	/** 
	 * converts an image from OpenCV to java space
	 *
	 * @param inBuffImg the buffered image to display
	 */
	public void showImage(BufferedImage inBuffImg) {
	    JPanel topPanel = new JPanel(new BorderLayout());
	    topPanel.add(new JLabel(new ImageIcon(inBuffImg)),BorderLayout.LINE_END);
	    getContentPane().add(topPanel);
	    setSize(inBuffImg.getWidth(),inBuffImg.getHeight()+frameBorder);
	    setVisible(true);
	}

	/** 
	 * main method
	 *
	 * @param args the arguments
	 */
	static public void main(String args[]) throws Exception {
		RHImshow frame = new RHImshow("Display image");
		BufferedImage anImg = new BufferedImage(800, 640, BufferedImage.TYPE_INT_ARGB);
		frame.showImage(anImg);
	}
}
