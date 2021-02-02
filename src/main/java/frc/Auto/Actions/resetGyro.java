package frc.Auto.Actions;

import frc.subSystems.Drive;

public class resetGyro implements Action {

    Drive mDrive;

    public resetGyro(){

        mDrive = Drive.getInstance();

    }

    @Override
    public void start() {
       mDrive.resetGyro();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return mDrive.isGyroReset();
    }

}