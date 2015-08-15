package qub.ecit.sivs.pagealert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.commons.io.FileUtils;

import static java.nio.file.StandardCopyOption.*;


public class DownloadSites {
	
	String prefix = System.getProperty("user.dir");
	
	public DownloadSites(){
		
		
	}
	
	public void start(StyledDocument docResults){
		try{
	          BufferedReader adUrlReader = new BufferedReader(new FileReader("resources/urls.csv"));
	          ArrayList<String> adUrlList = new ArrayList<String>();
	          String url;

	          while ((url = adUrlReader.readLine()) != null){
	              adUrlList.add(url.trim());
	          }
	          adUrlReader.close();

	          int fileCounter = 1;
	          for (String adUrl : adUrlList) {
	              try {
	            	  FileUtils.deleteDirectory(new File(prefix+"/resources/sites/"+fileCounter));
	                  String cmdString = prefix+"/lib/wget-1.11.4-1-bin/bin/wget --timestamping -v --directory-prefix=" + prefix + "/resources/sites/" 
	                		  				+ Integer.toString(fileCounter) + " --tries=2 --recursive --no-parent " + adUrl;
	                  System.out.println(cmdString);
	                  Runtime rt = Runtime.getRuntime();
	                  Process  p = rt.exec(cmdString);
	                 // p.waitFor();
	                  BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	                  String s;
	                  while ((s = r.readLine())!=null) {
	                      System.err.println( s );
	                  }
	                  r.close();
	              } catch (IOException ioe){
	            	  ioe.printStackTrace(); 
	            	  try {
							docResults.insertString(docResults.getLength(), ioe.toString(), null);
	                      } catch (BadLocationException e) {
							e.printStackTrace();
	                      }
	              }	//catch (InterruptedException ie){
	             // 	  ie.printStackTrace(); 
	             // }
	              
	              copyImages(fileCounter);
	              
	              fileCounter++;
	          }

	      } catch (IOException e){
	    	  e.printStackTrace(); 
	      }
	}
	
	private void copyImages(int fileCounter){ //copy all image files to images folder
		FileSearch fileSearch = new FileSearch();
        fileSearch.searchDirectory(new File(prefix + "/resources/sites/" + Integer.toString(fileCounter)));
        int count = fileSearch.getResult().size();
		  if(count ==0){
			System.out.println("\nNo result found!");
			System.out.println();
		  }else{
			  
			  File f = new File(prefix + "/resources/sites/" + Integer.toString(fileCounter)+"/images");
			  try{
			      if(f.mkdir()) { 
			          System.out.println("Directory Created");
			          System.out.println();
			      } else {
			          System.out.println("Directory is not created");
			          System.out.println();
			      }
			  } catch(Exception e){
			      e.printStackTrace();
			  } 
			  System.out.println("\nFound " + count + " result!\n");
			  for (String matched : fileSearch.getResult()){
				System.out.println("Found : " + matched);
				try {
					String filePath = prefix + "/resources/sites/" + Integer.toString(fileCounter)+"/images/"+ getFileName(matched);
					Files.copy(FileSystems.getDefault().getPath(matched), 
							   FileSystems.getDefault().getPath(filePath),
							   REPLACE_EXISTING);
					//getFileAttributes(filePath);
				} catch (IOException e) {
					e.printStackTrace();
				}

			  }
		  }
	}
	
	public static String getFileName(String s){
		String fileName = null;
		int length = s.length();
		
		for(int i = length-1; i>=0; i--){
			if(s.charAt(i)=='\\') {
				fileName = s.substring(i+1, length);
				break;
			}
		}
		return fileName;
	}
	
	public void getFileAttributes(String filePath){
		
		BasicFileAttributes attr;
		try {
			attr = Files.readAttributes(FileSystems.getDefault().getPath(filePath), BasicFileAttributes.class);
			System.out.println(filePath+" Created: " + attr.creationTime());
			System.out.println(filePath+" Accessed: " + attr.lastAccessTime());
			System.out.println(filePath+" Modified: " + attr.lastModifiedTime());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}