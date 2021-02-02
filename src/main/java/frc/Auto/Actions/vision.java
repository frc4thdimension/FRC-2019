package frc.Auto.Actions;

import frc.states.Command;
import frc.subSystems.Arduino;
import frc.subSystems.Drive;

public class vision implements Action {

    private static final double tol = 5;
    Arduino mArduino;
    Drive mDrive;
    double forPow;
    double resPower;
    double pixyValue;
    double output;
    final double midV = 99;
    boolean targetingFinished = false;
    Command cm;

    public vision(double forwardPow,Command command){
        cm = command;
        forPow = forwardPow;
        mArduino = Arduino.getInstance();
        mDrive = Drive.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        cm.command();
       output = (mArduino.getPixy()-midV) *0.03;
       if(output > 0.5){
        output = 0.5;
       }
       if(output < -0.5){
        output = -0.5;
       }
        mDrive.robotDrive.arcadeDrive(0,-output);
       
    }

    @Override
    public void done() {
        mDrive.robotDrive.arcadeDrive(0, 0);
        targetingFinished = false;
    }

    @Override
    public boolean isFinished() {
        return mArduino.getPixy()<midV+tol && mArduino.getPixy()<midV-tol;
    }

}