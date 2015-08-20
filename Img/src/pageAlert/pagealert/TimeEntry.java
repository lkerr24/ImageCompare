package pageAlert.pagealert;

public class TimeEntry{
	
	private boolean appear;
	private boolean withdraw;
	private String appearTime, withdrawTime;
	
	public void setAppear(){
		appear = true;
	}
	
	public boolean isAppear(){
		return appear;
	}
	
	public void setWithdraw(){
		withdraw = true;
	}
	
	public boolean isWithdraw(){
		return withdraw;
	}
	
	public void setAppearTime(String time){
		appearTime = time;
	}
	
	public String getAppearTime(){
		return appearTime;
	}

	public void setWithdrawTime(String time){
		withdrawTime = time;
	}
	
	public String getWithdrawTime(){
		return withdrawTime;
	}
}
