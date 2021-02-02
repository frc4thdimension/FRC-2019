package frc.Auto.Actions;

import frc.subSystems.Arm;
import frc.subSystems.Intake;

public class PickHatch implements Action {

    Arm mArm;
    Intake mIntake;
    private final double kArmHatchAngle = 15;
    public PickHatch(){
        mArm = Arm.getInstance();
        mIntake = Intake.getInstance();
    }
    @Override
    public void start() {

    }

    @Override
    public void update() {
        if(mArm.getArmAngle() < kArmHatchAngle){
            mArm.setArmPower(0.3);
        }else{
            mArm.sstallArm();
        }
    }

    @Override
    public void done() {
        mArm.sstallArm();
    }

    @Override
    public boolean isFinished() {
        return mArm.getArmAngle() > kArmHatchAngle;
    }

}