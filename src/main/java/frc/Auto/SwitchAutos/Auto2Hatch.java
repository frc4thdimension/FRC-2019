package frc.Auto.SwitchAutos;

import edu.wpi.first.wpilibj.Timer;
import frc.states.Command;
import frc.states.Structure;
import frc.states.Structure.robotStates;
import frc.subSystems.*;


public class Auto2Hatch {

    static Auto2Hatch mInstance = new Auto2Hatch();

    public int autoCounter;
    Teleop mTeleop;
    Arm mArm;
    Structure mStructure;
    Intake mIntake;
    Elevator mElevator;
    Pivot mPivot;
    Timer mTimer;
    Timer mTimer1;

    Timer mTimer2;

    Timer mTimer3;

    Timer mTimer4;
    Drive mDrive;

    public double delay;
    public static Auto2Hatch getInstance(){
        return mInstance;
        
    }

    public Auto2Hatch(){
        mTimer3 = new Timer();
        mTimer1 = new Timer();
        autoCounter = 0;
        mTimer = new Timer();
        mDrive = Drive.getInstance();
        mTeleop = Teleop.getInstance();
        mElevator = Elevator.getInstance();
        mArm = Arm.getInstance();
        mIntake = Intake.getInstance();
        mStructure = Structure.getInstance();
        mPivot = Pivot.getInstance();
    }

    public void executeAuto(){

        switch(autoCounter){
            case 0:
                if(!mTeleop.tookAutohatch){
                    mTeleop.takeHatch();
                }else{
                    mIntake.setRollers(0);
                
                    autoCounter++;
                }
             break;

            case 1: 
            mStructure.startToIntakeHandler(robotStates.HatchPivot);
            mDrive.goToDistance(145, 0,new Command(){
            
                @Override
                public void command(){
                    if(mStructure.currentState.equals(robotStates.HatchPivot)){
                        autoCounter++;
                    }else{

                    }
                }
            });
            break;

            case 2:
            mDrive.turnFunc2followUp(48,new Command(){
            
                @Override
                public void command() {
                    autoCounter++;
                    mDrive.resetEncoder();
                    mDrive.resetGyro();
                }
            });
             break;

            case 3:
            
            mStructure.stateTransistor(robotStates.HatchMidAuto);
            
            mDrive.goToDistanceToRocket(272, 0,new Command(){
            
                @Override
                public void command() {
                   
                    autoCounter++;
                    mTimer1.start();
                    mDrive.resetEncoder();
                    
                }
            });
            break;

            case 4: 

            if(mTimer1.get() < 1){
                mIntake.setRollers(1);
            }else{
                mIntake.setRollers(0);
                mTimer3.start();
                mDrive.resetGyro();
                autoCounter++;
            }
           
            break;

            case 5:

            mArm.setArmPower(0.05);

            if(mTimer3.get() > 0.3){
                mDrive.goToDistanceBack(-15,0,new Command(){
                
                    @Override
                    public void command() {
                        
                        autoCounter++;
                    }
                });
            }
            
             break;

            case 6:
            mArm.setArmAngle(40);
            mDrive.turnFunc2followUp(130,new Command(){
            
                @Override
                public void command() {
                    mArm.stallArm();
                    autoCounter++;
                    mDrive.resetEncoder();
                    mDrive.resetGyro();
                }
            });
            
            break;

            case 7: 
            mStructure.stateTransistor(robotStates.HatchPivot);
            mDrive.goToDistanceToHatch(300,0,new Command(){
            
                @Override
                public void command() {
                    
                }
            });
            
            
            break;

            case 100: 
            System.out.println("automode interrupted");
            break;

        }
    }



  
}