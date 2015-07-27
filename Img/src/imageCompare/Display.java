package imageCompare;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.IOException;

import javax.swing.*;

import com.pearsoneduc.ip.io.ImageDecoder;
import com.pearsoneduc.ip.io.ImageDecoderException;
import com.pearsoneduc.ip.io.ImageFile;
import com.pearsoneduc.ip.io.PPMDecoderException;
import com.pearsoneduc.ip.op.*;

public class Display extends JFrame {
	public Display(String filename) throws IOException, ImageDecoderException{
		super(filename);
		ImageDecoder input = ImageFile.createImageDecoder(filename);
		BufferedImage image = input.decodeAsBufferedImage();
		Raster rastimage = image.getData();
		int width = rastimage.getWidth();
		int height = rastimage.getHeight();
		System.out.println(width+" width pixels");
		System.out.println(height+" height pixels");
		/*
		 * int imgType;
		if (rastimage.getWidth()> 255 || rastimage.getHeight() > 255){
			imgType = BufferedImage.TYPE_USHORT_GRAY;
		} else {
			imgType = BufferedImage.TYPE_BYTE_GRAY;
		}
		/**
		 * 
		 */
		//BufferedImage greyImage = ;
		for (int i = 0; i<width;i++){
			for (int j=0; j<height; j++){
				//this turns the image black
				image.setRGB(i, j, BufferedImage.TYPE_BYTE_GRAY);
			}
		}
		//greyImage = new BufferedImage(rastimage.getWidth(), rastimage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		//JLabel view = new JLabel(new ImageIcon(greyImage));
		JLabel view = new JLabel(new ImageIcon(image));
		getContentPane().add(view);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
	}
	public static void main(String[] argv){
		String filename = "IMG_0062.jpg";
			try{
				JFrame frame = new Display(filename);
				frame.pack();
				frame.setVisible(true);
				//ImageGetPixels.processImage("IMG_0062.jpg", "IMG_0062.jpg");
			} catch (Exception e){
				System.err.println(e);
				System.exit(1);
			}
		}
}
