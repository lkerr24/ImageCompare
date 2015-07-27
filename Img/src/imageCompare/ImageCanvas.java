package imageCompare;

import java.awt.*;

public class ImageCanvas extends Canvas{
	Image image;
	public ImageCanvas(Image image){
		this.image = image;
	}
	public void paint(Graphics g){
		g.drawImage(image, 0, 0, this);
	}
	

}
