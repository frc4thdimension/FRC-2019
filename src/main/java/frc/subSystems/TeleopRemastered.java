package frc.subSystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.states.*;
import frc.states.Structure.robotStates;

public class TeleopRemastered{
    
    static TeleopRemastered mInstance = new TeleopRemastered();
    public static TeleopRemastered getInstance(){
        return mInstance;
    }

    //Power Constants
    private final double ballInPower = 1;
    private final double rollerInPower = 1;
    private final double rollerOutPower = -0.8;
    private final double kHatchLockDelay = 0.2;
    public DoubleSolenoid other;
    public Gamepad mGamepad;
    public Drive mDrive;
    public Arm mArm;
    public Elevator mElevator;
    public Pivot mPivot;
    public Intake mIntake;
    public DriverPanel mDriverPanel;
    public Structure mStructure;
    private double hatchCounter;
    private boolean hatchLock;
    private double hatchShoot;
    private boolean reverseCommand;
    private boolean manualException;
    public Solenoid climbPiston;
    VictorSP climbWh;
    double time;
    public robotStates lastRequestedState = robotStates.Starting;

    public TeleopRemastered(){
        climbWh = new VictorSP(7);
        other = new DoubleSolenoid(7, 1);
        mDrive = Drive.getInstance();
        mGamepad = Gamepad.getInstance();
        mArm = Arm.getInstance();
        mElevator = Elevator.getInstance();
        mPivot = Pivot.getInstance();
        mStructure = Structure.getInstance();
        mDriverPanel = DriverPanel.getInstance();
        mIntake = Intake.getInstance();
        climbPiston = new Solenoid(0);
        hatchLock = false;
        manualException = false;
        hatchShoot = 0.0;
        
    }



    public void IntakeLoop(){
        if(mGamepad.getLB()){
            //Ball Intaking
            mIntake.setIntakePower(ballInPower);
            mIntake.setRollers(rollerInPower);
        }else if(mGamepad.getButtonB()){
            //Ball Spit
            mIntake.setRollers(rollerOutPower);
            mIntake.setIntakePower(-ballInPower);
        
        }else if(mGamepad.getRTrigger() > 0){
            hatchLock = true;
           other.set(Value.kReverse);
            mIntake.hatchOut.set(false);
        }else if(mGamepad.getLTrigger() > 0){
            //hatch Out and Intaking Position
            if(Timer.getFPGATimestamp()-hatchCounter >= kHatchLockDelay){
                mIntake.hatchOut.set(false);
               
            }else{
                mIntake.hatchOut.set(true);
            }
            other.set(Value.kForward);
            
        }else{
            mIntake.setIntakePower(0);
            if(mIntake.ballDetected()){
                mIntake.setRollers(0.25);
            }else{
                mIntake.setRollers(0.0); 
            }
           

        hatchCounter = Timer.getFPGATimestamp();
            
                //hatch delayed lock
               
            
            
        }

        if(mGamepad.getRBpressed()){
            //Shift to unshifted gear
            mDrive.shift(!mDrive.shifter.get());
        }

        if(mGamepad.getYpressed()){
           
                climbPiston.set(!climbPiston.get());
          
            
            
            
        }
    }

    boolean reverseFlag;
    public void switchState(){

        if(mDriverPanel.getIfReverse()){
            reverseCommand = true;
        }else{
            reverseCommand = false;
        }
        if(mDriverPanel.getBallHigh()){
            manualException = false;
             if(reverseCommand){
                reverseFlag = true;
                lastRequestedState = robotStates.BallHighRear;
                mStructure.stateTransistor(robotStates.BallHighRear);
             }else{
                if(reverseFlag){
                    lastRequestedState = robotStates.BallHighRear;
                }else{
                    lastRequestedState = robotStates.BallHigh;
                }
                
                mStructure.stateTransistor(robotStates.BallHigh);
             }
            
        }else if(mGamepad.getButtonA()){
            manualException = false;
            lastRequestedState = robotStates.HatchPivot;
            mStructure.startToIntakeHandler(robotStates.HatchPivot);
        }else if(mDriverPanel.getBallLow()){
            manualException = false;
            
            lastRequestedState = robotStates.BallLow;
            mStructure.stateTransistor(robotStates.BallLow);
            

             
        }else if(mDriverPanel.getBallMid()){
            manualException = false;
            if(reverseCommand){
                lastRequestedState = robotStates.BallMidRear;
                mStructure.stateTransistor(robotStates.BallMidRear);
             }else{
                lastRequestedState = robotStates.BallMid;
                mStructure.stateTransistor(robotStates.BallMid);
             }
        }else if(mDriverPanel.getCargoLevel()){
            manualException = false;
            lastRequestedState = robotStates.BallCargo;
            mStructure.stateTransistor(robotStates.BallCargo);
        }else if(mDriverPanel.getHatchHigh()){
            manualException = false;
            if(reverseCommand){
                
                lastRequestedState = robotStates.HatchHighRear;
                mStructure.stateTransistor(robotStates.HatchHighRear);
             }else{
               
                lastRequestedState = robotStates.HatchHigh;
                mStructure.stateTransistor(robotStates.HatchHigh);
             }
        }else if(mDriverPanel.getHatchLow()){
            manualException = false;
            lastRequestedState = robotStates.HatchLow;
            mStructure.stateTransistor(robotStates.HatchLow);
        }else if(mDriverPanel.getHatchMid()){
            manualException = false;
            if(reverseCommand){
                
                lastRequestedState = robotStates.HatchMidRear;
                mStructure.stateTransistor(robotStates.HatchMidRear);
             }else{
                
                    lastRequestedState = robotStates.HatchMid;
                
               
                mStructure.stateTransistor(robotStates.HatchMid);
             }
        }else if(mDriverPanel.HatchIn()){
            
            manualException = false;
            lastRequestedState = robotStates.HatchPivot;
            mStructure.stateTransistor(robotStates.HatchPivot);

        }else if(mDriverPanel.cargoIn()){
            hatchLock = true;
           other.set(Value.kReverse);
            mIntake.hatchOut.set(false);
            manualException = false;

            lastRequestedState = robotStates.BallPivot;
            mStructure.stateTransistor(robotStates.BallPivot);
        }else if(mDriverPanel.getCargoLevel()){
            manualException = false;
            lastRequestedState = robotStates.BallCargo;
            mStructure.stateTransistor(robotStates.BallCargo);
        }else if(mGamepad.getButtonX()){
            manualException = true;
            if(mPivot.getPivotAngle()<=0){
                mPivot.setPivotPower(-0.35);
            }else{
                mPivot.setPivotPower(-0.8);
            }
           
            climbWh.set(1);

        }else if(mDriverPanel.slowArmDown()){
            manualException = true;
            mArm.setArmPower(-0.4);
        }else if(mDriverPanel.slowArmUp()){
            manualException = true;
            mArm.setArmPower(0.4);
        }else if(mDriverPanel.slowElvDown()){
            manualException = true;
            mElevator.releaseShifterAndGo(new Command(){
            
                @Override
                public void command() {
                    mElevator.setElevatorPower(-0.05);
                }
            });
        }else if(mDriverPanel.slowElvUp()){
            manualException = true;
            mElevator.releaseShifterAndGo(new Command(){
            
                @Override
                public void command() {
                    mElevator.setElevatorPower(0.3);
                }
            });
           
        }else if(mDriverPanel.slowPivotDown()){
            mPivot.setPivotPower(-1);
        }else if(mDriverPanel.slowPivotUp()){
            mPivot.setPivotPower(1);
        
        }else{
           /*
            * Main Else
            */
            climbWh.set(0);
            time=Timer.getFPGATimestamp();
            reverseCommand = false;
            if(manualException || !lastRequestedState.equals(mStructure.currentState)){
               
                mArm.sstallArm();
                mPivot.stallPivot();
                mElevator.lockElevator();
            }else if(!manualException && lastRequestedState.equals(mStructure.currentState)){
                if(mStructure.currentState.equals(robotStates.HatchHighRear)){
                    mStructure.stateTransistor(robotStates.HatchHighRear);
                }else{
                    mStructure.setArmAngleToState(lastRequestedState);
                }
                
            }else{

            }
        }
    }

    int climbModeFlag = 0;
    public void teleopStart(){


            switchState();
            IntakeLoop();
          
         
            
            mDrive.robotDrive.arcadeDrive(-1*mGamepad.getLAxisY(), -1*mGamepad.getRAxisX());

        SmartDashboard.putString("stateCurrent", mStructure.currentState.toString());
        SmartDashboard.putString("lastRequested", lastRequestedState.toString());
        //1st Driver Codes
        
    }

    
    }
