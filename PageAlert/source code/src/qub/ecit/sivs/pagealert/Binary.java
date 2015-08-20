
public class Binary extends Comparison{

	public Binary() {
		super();
	}

	public boolean compare2(File image1Path, File image2Path){
		image1 = image1Path;
		image2 = image2Path;
		boolean compareResult = false;
		try {
			compareResult = FileUtils.contentEquals(image1, image2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compareResult;
	}
}
