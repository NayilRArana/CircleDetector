import java.awt.image.*;
import java.io.*;
import org.opencv.core.*;
import javax.imageio.ImageIO;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CircleDetector {
	
	private File image;
	private BufferedImage bufferedImage;
	
	public void loadImage() throws Exception {
		this.image = new File("image.png");
		this.bufferedImage = ImageIO.read(this.image);
	}
	
	public Mat buffImgToMat(BufferedImage myBuffImg) {
		byte[] pixels = ((DataBufferByte) myBuffImg.getRaster().getDataBuffer()).getData();
		Mat matrix = new Mat(myBuffImg.getHeight(), myBuffImg.getWidth(), CvType.CV_8UC3);
		matrix.put(0, 0, pixels);
		return matrix;
	}
	
	public BufferedImage matToBuffImg(Mat matrix) throws Exception { 
	    MatOfByte matOfByte = new MatOfByte(); 
	    Imgcodecs.imencode(".png", matrix, matOfByte);
	    return ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
	}
	
}