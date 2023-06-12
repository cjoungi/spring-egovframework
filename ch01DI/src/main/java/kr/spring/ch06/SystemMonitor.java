package kr.spring.ch06;

public class SystemMonitor {
	private long periodTime;
	private SmsSender sender;
	
	public SystemMonitor(long periodTime, SmsSender sender) {
		this.periodTime = periodTime;
		this.sender = sender;
	}

	//저장되어 있는 값을 확인하는 용도
	@Override
	public String toString() {
		return "SystemMonitor [periodTime=" + periodTime + ", sender=" + sender + "]";
	}
	
	
}
