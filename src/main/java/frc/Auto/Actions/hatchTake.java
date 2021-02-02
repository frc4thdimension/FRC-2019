package frc.Auto.Actions;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.subSystems.Arm;
import frc.subSystems.Drive;
import frc.subSystems.Intake;
import frc.subSystems.TeleopRemastered;

public class hatchTake implements Action {

    Drive mDrive;
    Arm mArm;
    Intake mIntake;
    boolean done = false;
    TeleopRemastered mTeleop;
    public hatchTake(){
        mTeleop = TeleopRemastered.getInstance();
        mIntake = Intake.getInstance();
        mArm = Arm.getInstance();
        mDrive = Drive.getInstance();
    }

    @Override
    public void start() {
        mTeleop.other.set(Value.kForward);
    }

    @Override
    public void update() {
        if(mArm.getArmAngle() < 12){
            mArm.setArmPower(0.25);
        }else{
            mArm.sstallArm();
            mTeleop.other.set(Value.kReverse);
            done = true;
            
        }
    }

    @Override
    public void done() {
        
    }

    @Override
    public boolean isFinished() {
        return done;
    }


}