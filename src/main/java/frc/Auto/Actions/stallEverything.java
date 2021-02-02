package frc.Auto.Actions;

import frc.subSystems.Arm;
import frc.subSystems.Elevator;

public class stallEverything implements Action {

    Arm mArm;
    Elevator mElevator;
    

    public stallEverything(){
        mArm = Arm.getInstance();
        mElevator = Elevator.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        mArm.sstallArm();
        if(mElevator.getPosition() < 10){
            mElevator.setShifterState(false);
        }else{
            mElevator.setShifterState(true);
        }
    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    
}