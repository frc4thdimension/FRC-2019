package frc.Auto.Actions;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.subSystems.Arm;
import frc.subSystems.Elevator;
import frc.subSystems.Intake;
import frc.subSystems.Teleop;
import frc.subSystems.TeleopRemastered;

public class hatch implements Action {

    Intake mIntake;
    Arm mArm;
    private double shootTime;
    private double shootDuration = 1;
    Elevator mElv;
    TeleopRemastered mTeleop;


    public hatch(){
        shootDuration = 1.2;
        mArm = Arm.getInstance();
        mElv = Elevator.getInstance();
        mIntake = Intake.getInstance();
        mTeleop = TeleopRemastered.getInstance();
    }
    @Override
    public void start() {
        shootTime = Timer.getFPGATimestamp();
        mIntake.hatchOut.set(true);
        
        
    }

    @Override
    public void update() {
        mIntake.hatchOut.set(true);
        if(Timer.getFPGATimestamp()-shootTime > 0.5){
            mTeleop.other.set(Value.kForward);
        }
        
        mElv.lockElevator();
        mArm.sstallArm();
    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - shootTime > shootDuration ;
    }

}