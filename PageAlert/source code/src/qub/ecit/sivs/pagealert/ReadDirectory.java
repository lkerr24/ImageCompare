package qub.ecit.sivs.pagealert;

import java.io.File;
import java.util.ArrayList;

public class ReadDirectory{
	
	private File[] filesList;
	
	public ReadDirectory(String directoryPath){
		File directory = new File(directoryPath);
		filesList = directory.listFiles();
	}
	
    public ArrayList<File> getFilesList(){
    	ArrayList<File> webList = new ArrayList<File>();
    	for(File file: filesList){
    		webList.add(file);
    	}
    	return webList;
    }
}