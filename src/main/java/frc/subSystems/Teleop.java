package frc.subSystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Math.pid;
import frc.states.Command;
import frc.states.Structure;
import frc.states.Structure.robotStates;
import frc.subSystems.Intake.IntakeStates;

public class Teleop{

    static Teleop mInstance = new Teleop();
    public boolean isShooting = false;
    public static Teleop getInstance(){
        return mInstance;
    }

    Drive mDrive;
    Gamepad mGamepad;
    DriverPanel mDriverPanel;
    Intake mIntake;
    Pivot mPivot;
    VictorSP forwardMotor;
    Elevator mElevator;
    Arm mArm;
    pid mPid;
    PowerDistributionPanel pdp;
    Structure mStructure;
    boolean isShifted = false;
    double timeToReverse = 0;
    public boolean tookAutohatch;
    public boolean isAutoActive;
    public enum switchState{
        RequestDwell,
        OnState;

    }
    public switchState mySwitchState = switchState.OnState;

    public ArrayList<DataCollector> allDatasCollected;
    DataCollector mDataCollector;
    private boolean superStructureOverWhelmed = false;
    public boolean reverseCommand = false;
    Ultrasonic ultraSon;
    //Solenoid shifter;
    double time;
    public Teleop(){
        
        //shifter = new Solenoid(0);
       forwardMotor = new VictorSP(8);
        allDatasCollected = new ArrayList<>();
        tookAutohatch = false;
        isAutoActive = true;
        //ultraSon = new Ultrasonic(pingChannel, echoChannel);
        pdp = new PowerDistributionPanel();
        mPid = pid.getInstance();
        mDrive = Drive.getInstance();
        mGamepad = Gamepad.getInstance();
        mDriverPanel = DriverPanel.getInstance();
        mIntake = Intake.getInstance();
        mPivot = Pivot.getInstance();
        mElevator = Elevator.getInstance();
        mArm = Arm.getInstance();
        mStructure = Structure.getInstance();
    
    }

    
    public void takeHatch(){
      
        if(mArm.getArmAngle() < 11.5){
            mArm.setArmPower(0.23);
            tookAutohatch = false;
            timeToReverse = Timer.getFPGATimestamp();
        }else{
            mIntake.setRollers(-0.4);
            mArm.stallArm();
            if(Timer.getFPGATimestamp() - timeToReverse > 0.3){
                tookAutohatch = true;
            }
          
            
                
               
            }
            
        }
    


    public void start(){
        mDataCollector = new DataCollector();
        mDataCollector.setGyroData(mDrive.getAngle());
        mDataCollector.setLeftEncoderData(mDrive.getDistance());
        mDataCollector.setRightEncoderData(mDrive.getDistanceLeft());
        mDataCollector.setAxisLeft(mGamepad.getLAxisY()*-1);
        mDataCollector.setAxisRight(mGamepad.getRAxisX()*-0.8);
        allDatasCollected.add(mDataCollector);
       
        
         if(mGamepad.getButtonA()){
             if(Timer.getFPGATimestamp() -time < 1.4){
                mPivot.setPivotPower(-0.8);
             }else{
                 mPivot.setPivotPower(-0.35);
             }

             if(Timer.getFPGATimestamp() -time < 1.5){
                mDrive.robotDrive.arcadeDrive(1, 0);
             }else{
                mDrive.robotDrive.arcadeDrive(0.3, 0);
             }

             forwardMotor.set(1);
           
           
        }
        else if (mGamepad.getButtonB()){
            forwardMotor.set(1);
            mPivot.setPivotPower(-1);
        } else{
            time = Timer.getFPGATimestamp();
           mPivot.setPivotPower(0);
           forwardMotor.set(0);
            mDrive.robotDrive.arcadeDrive(mDataCollector.getAxisLeft(), mDataCollector.getAxisRight());
        } 
        
        if(mGamepad.getButtonX()){
            //shifter.set(true);
        }else if(mGamepad.getButtonY()){
            
            //shifter.set(false);

        }

        if(mGamepad.getRBpressed()){
            mDrive.shift(!mDrive.shifter.get());
        }

        if(mGamepad.getLB()){
            forwardMotor.set(1);
        }
      
        

    }


}