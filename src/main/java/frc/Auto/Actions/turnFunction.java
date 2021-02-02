package frc.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.subSystems.Drive;

public class turnFunction implements Action {

    Drive mDrive;
    double anglee;
    double time;
    boolean finished=false;
    int a=0;
    double firstTime;

    public  turnFunction(double angle){
        this.anglee = angle;
        mDrive = Drive.getInstance();
    }
   

    
    

    
    @Override
    public boolean isFinished() {
        
        System.out.println("oh "+mDrive.getAngle());
        return mDrive.isTurnFinished;
    }


    @Override
    public void update() {
        System.out.println("Update!!!!!!!!");
        
            System.out.println("Turning!!!");
            mDrive.turnFunc2(this.anglee);
      
    
    }


    @Override
    public void done() {
        System.out.println("bitti"+mDrive.getAngle());
        mDrive.Stop();
        
                
        
    }

    
    @Override
    public void start() {
    
        //mDrive.turnFunc2(this.anglee);

    }

  
   
}