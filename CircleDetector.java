import java.awt.image.*;
import java.io.*;
import org.opencv.core.*;
import javax.imageio.ImageIO;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CircleDetector {
	
	private File image;
	private BufferedImage bufferedImage;
	
	CircleDetector() {
	}
	
	// loads the image as a bufferedImage
	private void loadImage() throws Exception {
		this.image = new File("image.jpg");
		this.bufferedImage = ImageIO.read(this.image);
	}
	
	// converts myBuffImg to a Mat object
	private Mat buffImgToMat(BufferedImage myBuffImg) {
		byte[] pixels = ((DataBufferByte) myBuffImg.getRaster().getDataBuffer()).getData();
		Mat matrix = new Mat(myBuffImg.getHeight(), myBuffImg.getWidth(), CvType.CV_8UC3);
		matrix.put(0, 0, pixels);
		return matrix;
	}
	
	// converts matrix to a BufferedImage object
	private BufferedImage matToBuffImg(Mat matrix) throws Exception { 
	    MatOfByte matOfByte = new MatOfByte(); 
	    Imgcodecs.imencode(".jpg", matrix, matOfByte);
	    return ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
	}
	
	// draws circles on the original image
	private void drawCircles(Mat image, Mat circles) {
		//Draws circles on top of the original image
	    for (int i = 0; i < circles.cols(); i++) {
	        double[] circleData = circles.get(0, i);
	        Point centre = new Point(Math.round(circleData[0]), Math.round(circleData[1]));
	        int radius = (int) Math.round(circleData[2]);
	        Imgproc.rectangle(image, new Point(centre.x - 5, centre.y - 5), new Point(centre.x + 5, centre.y + 5), new Scalar(0, 0, 255)); 
	        Imgproc.circle(image, centre, radius, new Scalar(0, 255, 0), 3); 
	    }
	}
	
	//Handles the circle detection, then outputs file with circles
	private void detectCircles() throws Exception {
		Mat image = buffImgToMat(this.bufferedImage);
		Mat grayscale = new Mat();
	    Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY); 
	    Imgproc.blur(grayscale, grayscale, new Size(3, 3)); 
		Mat circles = new Mat();
	    
	    // Canny edge detection -> Hough transform -> detects circles
	    Imgproc.HoughCircles(grayscale, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 10, 150, 50, 5, 0);

	    this.drawCircles(image, circles);
	    this.bufferedImage = matToBuffImg(image);
	    
	    //Outputs the image with circles and centres drawn on top
	    File output = new File("image_w_circles.jpg");
        ImageIO.write(matToBuffImg(image), "jpg", output);
	}
	
	public static void main(String[] args) throws Exception{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CircleDetector detector = new CircleDetector();
		detector.loadImage();
		detector.detectCircles();
	}
	
	// javac -cp opencv-347.jar CircleDetector.java
	// java -cp opencv-347.jar;. CircleDetector
}