package frc.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.states.Command;
import frc.subSystems.Drive;

public class slowDistance implements Action{

    double time,voltage;
    double timeStarted;
    Drive mDrive;
    Command command;

    public slowDistance(double time,double power, Command command){
        this.command = command;
        mDrive = Drive.getInstance();
        voltage = power;
        this.time = time;
    }

    public slowDistance(double time,Command command){
        this.command = command;
        mDrive = Drive.getInstance();
        this.time = time;
        voltage = 0.5;
    }
    @Override
    public void start() {
        timeStarted = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        command.command();
        if(Timer.getFPGATimestamp()-timeStarted < time){
            mDrive.robotDrive.arcadeDrive(voltage, 0);
        }else{
            mDrive.robotDrive.arcadeDrive(Math.signum(voltage)*-0.05, 0);
        }
    }

    @Override
    public void done() {
        mDrive.robotDrive.arcadeDrive(Math.signum(voltage)*-0.05, 0);
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp()-timeStarted > time;
    }

}