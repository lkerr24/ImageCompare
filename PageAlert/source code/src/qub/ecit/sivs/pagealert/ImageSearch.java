package qub.ecit.sivs.pagealert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class ImageSearch {
	
	  private static final String EXT = "jpg";
	 
	  private String fileExtentionToSearch;
	  private List<String> result = new ArrayList<String>();
	 
	  public String getFileExtensionToSearch() {
		return fileExtentionToSearch;
	  }
	 
	  public void setFileExtensionToSearch(String fileExtensionToSearch) {
		this.fileExtentionToSearch = fileExtensionToSearch;
	  }
	 
	  public List<String> getResult() {
		return result;
	  }
	 
	  public static void main(String[] args) {
		  
		  
		ImageSearch fileSearch = new ImageSearch();
	 
		fileSearch.searchDirectory(new File("sites"), EXT);
	
		int count = fileSearch.getResult().size();
		if(count ==0){
		    System.out.println("\nNo result found!");
		}else{
		    System.out.println("\nFound " + count + " result!\n");
		    for (String matched : fileSearch.getResult()){
			System.out.println("Found : " + matched);
		    }
		}
	  }
	 
	  public void searchDirectory(File directory, String fileExtension) {
	 
		setFileExtensionToSearch(fileExtension);
	 
		if (directory.isDirectory()) {
		    search(directory);
		} else {
		    System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}
	 
	  }
	 
	  private void search(File file) {
	 
		if (file.isDirectory()) {
		  System.out.println("Searching directory ... " + file.getAbsoluteFile());
	 
		    if (file.canRead()) {
		    	for (File temp : file.listFiles()) {
		    		if (temp.isDirectory()) {
		    			search(temp);	
		    		} else {
		    			
		    			if (getFileExtensionToSearch().equals(FilenameUtils.getExtension(temp.getName()))) {		
		    				result.add(temp.getPath().toString());
		    			}
	 
		    		}
		    	}
	 
		    } else {
		    	System.out.println(file.getAbsoluteFile() + "Permission Denied");
		    }
	    }
	  }
}