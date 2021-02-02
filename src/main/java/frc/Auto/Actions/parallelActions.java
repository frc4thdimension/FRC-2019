package frc.Auto.Actions;

import java.util.ArrayList;
import java.util.List;

public class parallelActions implements Action {
    

	private final ArrayList<Action> mActions;
	
	public parallelActions(List<Action> actions) {
		mActions = new ArrayList<>(actions);
	}
	
	@Override
	public boolean isFinished() {
		  for (Action action : mActions) {
	            if (!action.isFinished()) {
	                return false;
	            }
	        }
	        return true;
	}

	@Override
	public void update() {
		  for (Action action : mActions) {
	            action.update();
	        }
		
	}

	@Override
	public void done() {
		for (Action action : mActions) {
            action.done();
		
		}
		
	}

	@Override
	public void start() {
		for (Action action : mActions) {
            action.start();
        }
	}
	
	

}