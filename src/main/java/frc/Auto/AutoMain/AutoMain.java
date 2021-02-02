package frc.Auto.AutoMain;

import frc.Auto.Actions.Action;

public abstract class AutoMain {

    protected double updateRate = 1/50;
	protected boolean  active = false;
	protected abstract void routine() throws AutoModeEndedException;
	
	public void run() {
		active = true;
		
		try {
			//tries routine 
			routine();
		}
		catch(AutoModeEndedException e) {
			e.getMessage();
			return;
		}
		done();
	}
	/**
     * Willingly Stop
     */
	public void done() {
		System.out.println("done");
		
	}
	/**
     * Force stop
     */
	public void stop() {
		System.out.println("stopped");
		active = false;
	}
	/**
     * checks if enable or not(auto not robot)
     * @return
     */
	public boolean isActive() {
		return active;
	}
	/**
     * if active loop
     * @return
     * @throws AutoModeEndedException
     */
	public boolean isActiveWithThrow() throws AutoModeEndedException {
		
		if(!isActive()) {
			throw new AutoModeEndedException();
		}
		return isActive();
		
	}
    
    /**
     * Main auto execution runs over this
     * @param action
     * @throws AutoModeEndedException
     */
	public void executeOrder66 (Action action)throws AutoModeEndedException {
		
		
		isActiveWithThrow();
		action.start();
		
		while(isActiveWithThrow()&&!action.isFinished()) {
		
			long waitTime = (long) (updateRate*1000);
			try {
			Thread.sleep(waitTime);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			action.update();
		}
		action.done();
	}
}
