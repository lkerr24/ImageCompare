package qub.ecit.sivs.pagealert;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
 
public class FileSearch {
 
	private PathMatcher matcher;
	private List<String> result = new ArrayList<String>();
  
	public FileSearch(){
		matcher = FileSystems.getDefault().getPathMatcher("glob:*.{png,jpg,jpeg}");
	}
 
	
 
	public List<String> getResult() {
		return result;
	}
 
	public void searchDirectory(File directory) {
 
		if (directory.isDirectory()) {
			search(directory);
		} else {
			System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}
	}
 
	private void search(File file) {
 
		if (file.isDirectory()) {
			System.out.println("Searching directory ... " + file.getAbsoluteFile());
 
            //do you have permission to read this directory?	
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						search(temp);
					} else {	
						if (matcher.matches(FileSystems.getDefault().getPath(temp.getName()))) {	
							result.add(temp.getAbsoluteFile().toString());
						}
 
					}
				}
			} else {
				System.out.println(file.getAbsoluteFile() + "Permission Denied");
			}
		}
	}
} 