package qub.ecit.sivs.pagealert;

import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class SearchIdentical {
	private String prefix = System.getProperty("user.dir")+ "/resources/sites/";
	private ArrayList<File> files; // files to search
	private JTextPane textPane;
	private ArrayList<File> webList; 
	private StyledDocument doc;
	private HashMap<String, HashMap<String, TimeEntry>> records; //the first string is image name.
	private boolean matched=false;
	private static int WIDTH=100, HEIGHT=100;
	private boolean cosineMatch = false;

	
	public SearchIdentical(HashMap<String, HashMap<String, TimeEntry>> records,
						   ArrayList<String> filePaths, JTextPane textPane, StyledDocument doc){
		files = new ArrayList<File>();
		for(String filePath : filePaths){
			files.add(new File(filePath));
		}
		this.textPane = textPane;
		this.doc=doc;
		webList = new ReadDirectory(prefix).getFilesList();
		this.records = records;
	}
	
	public void start(){
		CompareImage cImage = new CompareImage();
		for(File file : files){
			for(int i=1; i<=webList.size(); i++){
				matched=false;
				String web = getWebName(webList.get(i-1).toString());
				ArrayList<File> imageList = new ReadDirectory(prefix+i+"/images/").getFilesList();
				for(int j=0; j<imageList.size(); j++){
					boolean result = false;
					result = cImage.compare2(file, imageList.get(j));
					if(result){
						matched = true;
						if(records.get(file.getName())==null){
							TimeEntry te = new TimeEntry();
							te.setAppearTime(getCurrentTimeStamp());
							te.setAppear();
							HashMap<String, TimeEntry> r = new HashMap<String, TimeEntry>();
							r.put(web, te);
							records.put(file.getName(), r);
							try {
								doc.insertString(doc.getLength(), web+"\n", null);
								Image img = new ImageIcon(imageList.get(j).getPath()).getImage();  
								Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
								textPane.insertIcon (new ImageIcon(newimg));
								textPane.insertIcon(new ImageIcon("resources/upload.png"));
								doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getAppearTime(), null);
								doc.insertString(doc.getLength(), "\n\n\n", null);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}else {
							if(records.get(file.getName()).get(web)!=null){
								try {
									doc.insertString(doc.getLength(), web+"\n", null);
									Image img = new ImageIcon(imageList.get(j).getPath()).getImage();  
									Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
									textPane.insertIcon (new ImageIcon(newimg));
									textPane.insertIcon(new ImageIcon("resources/upload.png"));
									doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getAppearTime(), null);
									doc.insertString(doc.getLength(), "\n\n\n", null);
								} catch (BadLocationException e) {
									e.printStackTrace();
								}
							}else{
								TimeEntry te = new TimeEntry();
								te.setAppearTime(getCurrentTimeStamp());
								te.setAppear();
								HashMap<String, TimeEntry> r = records.get(file.getName());
								r.put(web, te);
								records.put(file.getName(), r);
								
								try {
									doc.insertString(doc.getLength(), web+"\n", null);
									Image img = new ImageIcon(imageList.get(j).getPath()).getImage();  
									Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
									textPane.insertIcon (new ImageIcon(newimg));
									textPane.insertIcon(new ImageIcon("resources/upload.png"));
									doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getAppearTime(), null);
									doc.insertString(doc.getLength(), "\n\n\n", null);
								} catch (BadLocationException e) {
									e.printStackTrace();
								}
							}
						}
							
					}
					
				}
				if(!matched && records.get(file.getName())!=null && records.get(file.getName()).get(web)!=null){	
					if(!records.get(file.getName()).get(web).isWithdraw()){
						records.get(file.getName()).get(web).setWithdrawTime(getCurrentTimeStamp());
						records.get(file.getName()).get(web).setWithdraw();
						try {
							doc.insertString(doc.getLength(), web+"\n", null);
							Image img = new ImageIcon(file.getPath()).getImage();  
							Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
							textPane.insertIcon (new ImageIcon(newimg));
							textPane.insertIcon(new ImageIcon("resources/upload.png"));
							doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getAppearTime(), null);
							textPane.insertIcon(new ImageIcon("resources/remove.png"));
							doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getWithdrawTime(), null);
							doc.insertString(doc.getLength(), "\n\n\n", null);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}else{
						try {
							doc.insertString(doc.getLength(), web+"\n", null);
							Image img = new ImageIcon(file.getPath()).getImage();  
							Image newimg = img.getScaledInstance(WIDTH, HEIGHT,  java.awt.Image.SCALE_SMOOTH); 
							textPane.insertIcon (new ImageIcon(newimg));
							textPane.insertIcon(new ImageIcon("resources/upload.png"));
							doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getAppearTime(), null);
							textPane.insertIcon(new ImageIcon("resources/remove.png"));
							doc.insertString(doc.getLength(), records.get(file.getName()).get(web).getWithdrawTime(), null);
							doc.insertString(doc.getLength(), "\n\n\n", null);
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}
	}
	
	private String getWebName(String path){
		String name = null;
		
		File directory = new File(path);
		File[] filesList = directory.listFiles();
		if(filesList[0].toString().substring(path.length()+1).equals("images")){
			name = filesList[1].toString().substring(path.length()+1);
		}else{
			name = filesList[0].toString().substring(path.length()+1);
		}
		return name;
	}
	
	public static String getCurrentTimeStamp() {
		  SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		  Date now = new Date();
		  String strDate = sdfDate.format(now);
		  return strDate;
	}
}
