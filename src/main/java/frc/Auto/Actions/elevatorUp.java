
package frc.Auto.Actions;

import frc.subSystems.Elevator;

public class elevatorUp implements Action {

    Elevator mElevator;
    double wantedHeight;

    public elevatorUp(double mmOffGround){
        wantedHeight = mmOffGround;
        mElevator = Elevator.getInstance();
    }

    @Override
    public void start() {
        mElevator.setElevatorPID(wantedHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return mElevator.isArrivedWantedMM;
    }

}