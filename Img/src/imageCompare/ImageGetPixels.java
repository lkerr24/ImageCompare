package imageCompare;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;

public class ImageGetPixels extends JFrame{
	public ImageGetPixels(String filename) throws IOException, ImageDecoderException{
		super(filename);
		ImageDecoder input = ImageFile.createImageDecoder(filename);
		BufferedImage image = input.decodeAsBufferedImage();
		JLabel view = new JLabel(new ImageIcon(image));
		getContentPane().add(view);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
	}
	public static Image processImage(String infile, String outfile){
		Image image = Toolkit.getDefaultToolkit().getImage(infile);
		Image imageGrey = null;
		try{
			PixelGrabber grabber = new PixelGrabber(image, 0,0,-1,-1,false);
			if(grabber.grabPixels()){
				int width = grabber.getWidth();
				int height = grabber.getHeight();
				ColorModel greyModel = null;
				if (bytesAvailable(grabber)){
					byte[] data  = (byte[]) grabber.getPixels();
					System.out.println("processing grey image");
					byte[] grey = new byte[256];
					for (int i=0;i<256;i++){
						grey[i] = (byte) i;
						greyModel = new IndexColorModel(8, 256, grey, grey, grey);
					}
					ImageProducer prod = new MemoryImageSource(256, 256, greyModel, data, 0, 256);
					imageGrey = Toolkit.getDefaultToolkit().createImage(prod);
				}
				else {
					int[] data = (int[]) grabber.getPixels();
					System.out.println("colour processing");
					byte[] grey = new byte[256];
					for (int i=0;i<256;i++){
						grey[i] = (byte) i;
						greyModel = new IndexColorModel(8, 256, grey, grey, grey);
					}
					ImageProducer prod = new MemoryImageSource(256, 256, greyModel, data, 0, 256);
					imageGrey = Toolkit.getDefaultToolkit().createImage(prod);;
				}
			}
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}

		return imageGrey;
	}
	public static final boolean bytesAvailable(PixelGrabber pg){
		return pg.getPixels() instanceof byte[];
	}
	public static void main(String[] argv){
		String filename = "IMG_0062.jpg";
			try{
				JFrame frame = new Display(filename);
				frame.pack();
				frame.setVisible(true);
				Image grey = processImage("IMG_0062.jpg", "IMG_0062.jpg");
				
			} catch (Exception e){
				System.err.println(e);
				System.exit(1);
			}
		}
}
