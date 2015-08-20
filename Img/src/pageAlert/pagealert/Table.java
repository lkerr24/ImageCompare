package pageAlert.pagealert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Table {
	private String tablePath = "resources/table.txt";
	private HashMap<String, HashMap<String, TimeEntry>> records;
	
	public Table(HashMap<String, HashMap<String, TimeEntry>> records){
		this.records = records;
	}
	
	public void save(){
		String table="";
		for (Map.Entry<String, HashMap<String, TimeEntry>> entry : records.entrySet()) {
		    String file = entry.getKey();
		    HashMap<String, TimeEntry> websites = entry.getValue();
		    for(Map.Entry<String, TimeEntry> site : websites.entrySet()){
		    	String web = site.getKey();
		    	TimeEntry time = site.getValue();
		    	String record = file+"|"+web+"|"+time.getAppearTime()+"|"+time.getWithdrawTime()+"\n";
		    	table = table+record;
		    }
		}
		try {
			File file = new File(tablePath);
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(table);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read(){
		BufferedReader br = null;
		try {
			File f = new File(tablePath);
			 
			if (f.exists()) {
				String line;
				br = new BufferedReader(new FileReader(f));
		 
				while ((line = br.readLine()) != null) {
				   System.out.println(line);
				   StringTokenizer stringTokenizer = new StringTokenizer(line, "|");
				   int i=0;
				   String file = null, web = null, appearTime = null, withdrawTime = null;
				   while (stringTokenizer.hasMoreElements()) {
					   switch(++i){
					   case 1:	file = stringTokenizer.nextElement().toString();
						   		break;
					   case 2:	web = stringTokenizer.nextElement().toString();
						   		break;
					   case 3:	appearTime = stringTokenizer.nextElement().toString();
						   		break;
					   case 4:	withdrawTime = stringTokenizer.nextElement().toString();
						   		break;
					   default: break;
					   }
				   }
				   TimeEntry te = new TimeEntry();
				   te.setAppearTime(appearTime);
				   te.setAppear();
				   if(!withdrawTime.equals("null")){
					   te.setWithdrawTime(withdrawTime);
					   te.setWithdraw();
				   }
				   
				   if(records.get(file)==null){
					   HashMap<String, TimeEntry> r = new HashMap<String, TimeEntry>();
					   r.put(web, te);
					   records.put(file, r);
				   }else{
					   HashMap<String, TimeEntry> r = records.get(file);
					   r.put(web, te);
					   records.put(file, r);
				   }
				   
				   System.err.println(file+"   "+web+"   "+te.getAppearTime()+"   "+te.getWithdrawTime());
				}
		 
				System.out.println("Done");
			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
