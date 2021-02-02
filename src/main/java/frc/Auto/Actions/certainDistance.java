package frc.Auto.Actions;

import frc.subSystems.Drive;

public class certainDistance implements Action {

    Drive mDrive;
    double maxSpeed,targetX;
    
    public certainDistance(double maxSpeed, double targetX){
        this.maxSpeed = maxSpeed;
        this.targetX = targetX;
        mDrive = Drive.getInstance();
        
    }
    @Override
    public void start() {
        mDrive.ROTS(this.maxSpeed, this.targetX);
      
    }

    @Override
    public void update() {
        mDrive.ROTS(this.maxSpeed, this.targetX);
        
     
    }

    @Override
    public void done() {
        mDrive.robotDrive.arcadeDrive(0, 0);
        mDrive.bRots = false;
    }

    @Override
    public boolean isFinished() {
        return mDrive.bRots;
    }

}

