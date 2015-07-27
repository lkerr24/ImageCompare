package imageCompare;

public class ByteImage {
	private int width;
	private int height;
	private byte[][] data;
	
	public ByteImage(int width, int height){
		this.width = width;
		this.height = height;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public int getPixel(int x, int y){
		return data[x][y];
	}
	public void setPixel(int x, int y, int value){
		this.data[x][y] = (byte) value;
	}

}
