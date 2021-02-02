package frc.Auto.Actions;

import frc.subSystems.Drive;

public class xyTurn implements Action {

    Drive mDrive;
    double destX;
    double angle;
    double destY;

    public xyTurn(double destinationX,double destinationY, double angle){

        this.destX = destinationX;
        this.destY = destinationY;
        this.angle = angle;
        mDrive = Drive.getInstance();

    }

    @Override
    public void start() {
        mDrive.xyTurnPID(destX, destY, angle);
    }

    @Override
    public void update() {
        mDrive.xyTurnPID(destX, destY, angle);
    }

    @Override
    public void done() {
        mDrive.robotDrive.arcadeDrive(0,0);
        mDrive.bTurnFunc = false;
    }

    @Override
    public boolean isFinished() {
        return mDrive.bTurnFunc;
    }


}