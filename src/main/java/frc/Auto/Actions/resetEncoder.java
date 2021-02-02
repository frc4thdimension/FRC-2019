package frc.Auto.Actions;

import frc.states.Command;
import frc.subSystems.Drive;
import frc.subSystems.Intake;

public class resetEncoder implements Action {

    Intake mIntake;
    Drive mDrive;
    int a = 0;
    

    public resetEncoder(){
       
        mDrive = Drive.getInstance();
        mIntake = Intake.getInstance();
    }

    @Override
    public void start() {

        mDrive.resetEncoder();
        a++;

    }

    @Override
    public void update() {
        
    }

    @Override
    public void done() {
        a=0;
    }

    @Override
    public boolean isFinished() {
        return a>0;
    }


}