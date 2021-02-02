package frc.Auto.Actions;

import java.util.ArrayList;
import java.util.List;

public class seriesAction implements Action {

    private Action CurrentAct;
	private final ArrayList<Action> RemainingActs;
	
	 public seriesAction(List<Action> actions) {
	        RemainingActs = new ArrayList<>(actions);
	        CurrentAct = null;
	}
	
	@Override
	public boolean isFinished() {
		return RemainingActs.isEmpty() && CurrentAct == null;
	}

	@Override
	public void update() {
		
		  if (CurrentAct == null) {
	            if (RemainingActs.isEmpty()) {
	                return;
	            }

	            CurrentAct = RemainingActs.remove(0);
	            CurrentAct.start();
	        }

		  CurrentAct.update();

	        if (CurrentAct.isFinished()) {
	        	CurrentAct.done();
	        	CurrentAct = null;
	        }
	    }

		
	

	@Override
	public void done() {
		
		
	}

	@Override
	public void start() {
		
		
	}

}