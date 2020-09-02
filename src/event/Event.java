package event;

public class Event {
	private boolean isCancelled;
	
	public void setCancelled(boolean b) {
		isCancelled = b;
	}
	
	public boolean isCancelled() {
		return isCancelled;
	}
}
