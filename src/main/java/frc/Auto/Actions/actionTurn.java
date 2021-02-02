package frc.Auto.Actions;

import frc.states.Command;
import frc.subSystems.Drive;

public class actionTurn implements Action {

    Drive mDrive;
    private double zRampDownRate;
    private double rotatePower;
    private double angleToGo;
    private double rampDownDistance;
    private double forwardProp;
    private double prevErr;
    private double Der;
    private double lastOutput;
    Command command;

    double error;
    final double kP = 0.05;
    final double kD = 5;
    private final double cruiseVelocity = 0.7;
    public actionTurn(double forwardProp,double angleToGo,Command command){
        this.command = command;
        this.forwardProp = forwardProp;
        mDrive = Drive.getInstance();
        rampDownDistance = angleToGo*0.3;
        zRampDownRate = cruiseVelocity/rampDownDistance;
        this.angleToGo = angleToGo;


    }

    public double turnPID(double angle){
        error = mDrive.getAngle()-angle;
        Der = error-prevErr;
        prevErr = error;
        return error*kP + kD*Der;
    }
    @Override
    public void start() {

    }

    @Override
    public void update() {

        
        command.command();
        if(forwardProp != 0){
            if(mDrive.getAngle() < angleToGo+rampDownDistance && mDrive.getAngle() > angleToGo -rampDownDistance){
                if(mDrive.getAngle() < angleToGo+3 && mDrive.getAngle() > angleToGo - 3 ){
                    if(forwardProp <0){
                        mDrive.robotDrive.arcadeDrive(-0.05, 0);
                    }else if(forwardProp>0){
                        mDrive.robotDrive.arcadeDrive(-0.05, 0);
                    }else{
                        mDrive.robotDrive.arcadeDrive(0, 0);
                    }
                }else{
                    lastOutput = turnPID(angleToGo);
                    if(lastOutput > 0.7){
                        lastOutput = 0.7;
                    }
                    if(lastOutput < -0.7){
                        lastOutput = -0.7;
                    }
                    mDrive.robotDrive.curvatureDrive(forwardProp, lastOutput,false);
                }
            }else if(mDrive.getAngle() > angleToGo+rampDownDistance){
                mDrive.robotDrive.curvatureDrive(forwardProp, cruiseVelocity,false);
            }else{
                mDrive.robotDrive.curvatureDrive(forwardProp, -cruiseVelocity,false);
    
            }
        }else{
            if(mDrive.getAngle() < angleToGo+rampDownDistance && mDrive.getAngle() > angleToGo -rampDownDistance){
                if(mDrive.getAngle() < angleToGo+3 && mDrive.getAngle() > angleToGo - 3 ){
                    if(forwardProp <0){
                        mDrive.robotDrive.arcadeDrive(0.05, 0);
                    }else if(forwardProp>0){
                        mDrive.robotDrive.arcadeDrive(-0.05, 0);
                    }else{
                        mDrive.robotDrive.arcadeDrive(0, 0);
                    }
                }else{
                    lastOutput = turnPID(angleToGo);
                    if(lastOutput > 0.7){
                        lastOutput = 0.7;
                    }
                    if(lastOutput < -0.7){
                        lastOutput = -0.7;
                    }
                    mDrive.robotDrive.arcadeDrive(forwardProp, lastOutput);
                }
            }else if(mDrive.getAngle() > angleToGo+rampDownDistance){
                mDrive.robotDrive.arcadeDrive(forwardProp, cruiseVelocity);
            }else{
                mDrive.robotDrive.arcadeDrive(forwardProp, -cruiseVelocity);
    
            }
        }
        
    }

    @Override
    public void done() {
        if(forwardProp <0){
            mDrive.robotDrive.arcadeDrive(-0.05, 0);
        }else if(forwardProp>0){
            mDrive.robotDrive.arcadeDrive(-0.05, 0);
        }else{
            mDrive.robotDrive.arcadeDrive(0, 0);
        }
      
        
    }

    @Override
    public boolean isFinished() {
        return mDrive.getAngle() > angleToGo-5 && mDrive.getAngle() < angleToGo+5;
    }

}