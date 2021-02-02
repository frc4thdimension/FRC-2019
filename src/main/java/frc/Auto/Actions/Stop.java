package frc.Auto.Actions;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.subSystems.Drive;

public class Stop implements Action {

    Drive driveObject;
	//Intake mIntake;
	public Stop() {
		//mIntake = Intake.getInstance();
		driveObject = Drive.getInstance();
		

	}
	@Override
	public boolean isFinished() { 
			
		return false;
					
	}

	@Override
	public void update() {
		SmartDashboard.putNumber("gyroBoyy", driveObject.getAngle());
		driveObject.Stop();
	}

	@Override
	public void done() {
		
			
	}

	@Override
	public void start() {

		driveObject.Stop();
		
		
	}
	
	



}