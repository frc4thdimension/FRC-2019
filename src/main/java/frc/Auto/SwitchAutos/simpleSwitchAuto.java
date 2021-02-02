package frc.Auto.SwitchAutos;
/*
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.subSystems.Drive;
import frc.subSystems.Gamepad;
import frc.subSystems.Teleop;
*/
public class simpleSwitchAuto {}

    /*
    Drive mDrive;
    Gamepad mGamepad;
    Teleop mTeleop;
    int easyCounter = 0;
    int mainCounter = 0;
    double prevTime;
    public boolean inTeleop = false;

    public simpleSwitchAuto(){
        
        mDrive = Drive.getInstance();
        mTeleop = Teleop.getInstance();
        mGamepad = Gamepad.getInstance();


    }

    //Switch Easy

    public void switchEasyAutoExecute(){
        //SmartDashboard.putBoolean("inTeleop", inTeleop);
        if(mGamepad.getButtonA()){
            inTeleop = true;
        }

        if(inTeleop==false){
            
           // mDrive.robotDrive.arcadeDrive(0.5, 0);
            System.out.println("Simple switchhhhhhhhhh");

        switch(easyCounter){

            case 0:
            mDrive.ROTS(2,200);

                if(mDrive.bRots){
                    mDrive.bRots = false;
                    easyCounter++;
                }
            break;

            case 1:
            mDrive.turnFunc2(-90);

                if(mDrive.bTurnFunc){
                    mDrive.resetGyro();
                    mDrive.resetXY();
                    mDrive.bTurnFunc = false;
                    easyCounter++;
                    
                }

            break;


            case 2:
            mDrive.ROTS(2.5, 300);

                if(mDrive.bRots){
                    mDrive.resetGyro();
                    mDrive.resetXY();
                    mDrive.bRots = false;
                    easyCounter++;
                   

                }

            break;
            

            case 3:
            mDrive.turnFunc2(35);

                if(mDrive.isTurnFinished){
                    mDrive.robotDrive.arcadeDrive(0, 0);
                    easyCounter++;
                }

            break;

            case 4:

            //hatch up


            case 5:
            //hatch place


            break;


            case 6:
            //hatch down

            break;

            case 7:
            mDrive.ROTS(-2.5, -200);

                if(mDrive.bRots){

                    easyCounter++;
                    mDrive.bRots = false;
                }

            break;
        }

    
            }else{
                mTeleop.start();
            }
        }

    }
*/