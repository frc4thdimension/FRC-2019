package frc.Auto.Actions;

import frc.subSystems.Drive;

public class resetXY implements Action {

    Drive mDrive;

    public resetXY(){

        mDrive = Drive.getInstance();

    }

    @Override
    public void start() {

        mDrive.resetXY();

    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return mDrive.isXYreset();
    }


}