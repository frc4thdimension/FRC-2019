package frc.Auto.SwitchAutos;


public class mainSwitchAuto {}
/*


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Auto.Actions.resetGyro;
import frc.subSystems.Arduino;
import frc.subSystems.Drive;
import frc.subSystems.Gamepad;
import frc.subSystems.Teleop;

public class mainSwitchAuto {

    Arduino mArduino;
    Drive mDrive;
    Gamepad mGamepad;
    Teleop mTeleop;
    int easyCounter = 0;
    int mainCounter = 0;
    public boolean inTeleop2=false;
    double prevTime;
    double prevAngle;
    boolean finish;

    public mainSwitchAuto(){
      
        mArduino = Arduino.getInstance();
        mDrive = Drive.getInstance();
        mGamepad = Gamepad.getInstance();
        mTeleop = Teleop.getInstance();

    }


    //Switch Main

    public void switchMainAutoExecute(){
        SmartDashboard.putBoolean("inTeleop2", inTeleop2);
    if(mGamepad.getButtonA()){
        inTeleop2 = true;
    }


    if(inTeleop2==false){
//mDrive.robotDrive.arcadeDrive(0.8,0);
        System.out.println("SwitchMainnnnnnnn");
/*
     
        switch(mainCounter){
            case 0:
            mDrive.ROTS(2.5, 200);

                if(mDrive.bRots){
                    mDrive.robotDrive.arcadeDrive(0, 0);
                    mainCounter++;
                    mDrive.bRots = false;
                    mDrive.resetGyro();
                    
                }
            break;


            case 1:
            mDrive.turnFunc2(-90);
            if(mDrive.isTurnFinished){
                prevTime = Timer.getFPGATimestamp();
                mDrive.resetGyro();
                mDrive.resetXY();
                mDrive.robotDrive.arcadeDrive(0,0);
                mDrive.isTurnFinished = false;
                mainCounter++;
            }

            break;


            case 2:
            mDrive.ROTS(2.5, 340);
            
                if(mDrive.bRots){
                    mDrive.robotDrive.arcadeDrive(0,0);
                    mainCounter++;
                    mDrive.bRots = false;
                }

            break;
            

            case 4:
            mDrive.turnFunc2(20);

                if(mDrive.isTurnFinished){
                    mDrive.resetGyro();
                    mDrive.robotDrive.arcadeDrive(0,0);
                    mDrive.isTurnFinished = false;
                    mainCounter++;
                }

                
            break;

            case 5:
            if(mArduino.getPixy()==0){


            }else if(mArduino.getPixy()==1){


            }else if(mArduino.getPixy()==2){
                mainCounter++;
            }

            break;

            case 6:
         
            //hatch out
                if(finish){
                    prevAngle = mDrive.getAngle();
                    mDrive.resetGyro();
                    mDrive.resetXY();
                    mainCounter++;
                }


            break;


            case 7:
            
            mDrive.turnFunc2(-prevAngle);
            if(mDrive.isTurnFinished){
                mDrive.resetGyro();
                mDrive.resetXY();
                mDrive.robotDrive.arcadeDrive(0,0);
                mDrive.isTurnFinished = false;
                mainCounter++;
            }


            break;

            case 8:
            mDrive.ROTS(-2.5, -550);
            
            if(mDrive.bRots){
                mDrive.resetGyro();
                mDrive.resetXY();
                mDrive.bRots = false;
                mDrive.robotDrive.arcadeDrive(0,0);
                mainCounter++;
                
            }

            break;

            case 9:

             //hatch in
            
                if(finish){

                    mainCounter++;
                }
                


            break;

            case 10:
            mDrive.ROTS(2.5, 550);

                if(mDrive.bRots){
                    mDrive.resetGyro();
                    mDrive.resetXY();
                    mDrive.bRots = false;
                    mainCounter++;
                }

            break;
            
            case 11:
            mDrive.turnFunc2(20);

                if(mDrive.isTurnFinished){
                    mDrive.resetGyro();
                    mDrive.robotDrive.arcadeDrive(0,0);
                    mDrive.isTurnFinished = false;
                    mainCounter++;
                }

                
            break;

            case 12:
            if(mArduino.getPixy()==0){


            }else if(mArduino.getPixy()==1){


            }else if(mArduino.getPixy()==2){
                prevAngle = mDrive.getAngle();
                mainCounter++;
            }

            break;

            case 13:
         
            //hatch out
                if(finish){
                    mainCounter++;
                }


            break;


            case 14:
            
            mDrive.turnFunc2(-prevAngle);
                if(mDrive.isTurnFinished){
                    mDrive.resetGyro();
                    mDrive.resetXY();
                    mDrive.robotDrive.arcadeDrive(0,0);
                    mDrive.isTurnFinished = false;
                    mainCounter++;
                }

            break;

            case 15:
            mDrive.ROTS(-2.5, -200);
                if(mDrive.bRots){
                    mainCounter++;
                }
            break;
        }
    
    }else{
    mTeleop.start();
    }

    */


