package frc.Auto.Actions;

import frc.subSystems.Drive;

public class getXY implements Action {

    Drive mDrive;

    public getXY(){
        mDrive = Drive.getInstance();
    }

    @Override
    public void start() {
        mDrive.getDeltaX();
        mDrive.getDeltaY();

    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return mDrive.bRots;
    }


}