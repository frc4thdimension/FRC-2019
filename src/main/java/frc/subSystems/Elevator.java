package frc.subSystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import frc.Math.pid;
import frc.states.Command;
import frc.states.Structure;



public class 
Elevator extends Subsystem {

    static Elevator mInstance = new Elevator();

    public static Elevator getInstance() {
        return mInstance;
    }

    private static final double kStallVoltage = 0.05;
    Structure mStructure;
    double time;
    int m = 0;
    boolean lockElevator = true;
    double positionTolerance = 20;
    Boolean manualHatchPos;
    VictorSP elevator1;
    VictorSP elevator2;
    Encoder elevatorEncoder;
    Solenoid elevatorLock;
    int a = 0;
    pid mPid;
    Drive mDrive;

    
    public boolean isStalled,isHanged,isElevatorDown,isArrivedWantedMM;


   

    double realSetPower;

    public Elevator() {
        elevatorLock = new Solenoid(6);
        elevatorLock.set(false);
        elevator1 = new VictorSP(4);
        elevator1.setInverted(true);
        elevator2 = new VictorSP(3);
        elevatorEncoder = new Encoder(16, 17, true);
        elevatorEncoder.setDistancePerPulse(6.35 * 18 / 360);
        mPid = pid.getInstance();
        mDrive = Drive.getInstance();
        
      
    }

    @Override
    public void zeroSensors() {
        elevatorEncoder.reset();
    }

    @Override
    public void checkSensors() {

    }

    public double getPosition() {
        return elevatorEncoder.getDistance();
    }

    public boolean isElevatorDown(){
        return getPosition() < 10;
    }

    public void lockElevator(){
        setShifterState(true);
        setElevatorPower(0);
    }

    public void setBallIntakingPos(){
        releaseShifterAndGo(new Command(){
        
            @Override
            public void command() {
                if(getPosition()>60){
                    setElevatorPower(-0.3);
                }else if(getPosition() >25 && getPosition() < 60){
                    setElevatorPower(-0.01);
                }else if (getPosition() < 25 && getPosition() > 5){
                    setElevatorPower(0.02);
                }else {
                    setElevatorPower(0);
                }
            }
        });
       }
    public boolean flagElvHeight;
    public void setElevatorPID(double mmOffGround) {

        
        if (getPosition() > mmOffGround - 5 && getPosition() < mmOffGround + 5) {
            setElevatorPower(0);
           setShifterState(true);
           flagElvHeight = true;

        } else {
            releaseShifterAndGo(new Command(){
            
                @Override
                public void command() {
                   closedLoopElevator(mmOffGround,1);
                    
                }
            });
            isElevatorDown = false;
            flagElvHeight = false;
            }
    }

    
    
    public void setElevatorPIDafterCommand(double mmOffGround,Command command) {
        if (getPosition() > mmOffGround - 5 && getPosition() < mmOffGround + 5) {
            setElevatorPower(0);
           setShifterState(true);
           command.command();

        } else {
            releaseShifterAndGo(new Command(){
            
                @Override
                public void command() {
                   closedLoopElevator(mmOffGround,1);
                    
                }
            });
            isElevatorDown = false;
            }
    }

    
 
    public void switchElevatorHeight(double height) {
        if(getPosition() < height-5){
        switch (m) {
        case 0:
            setShifterState(false);
            m++;
            time = Timer.getFPGATimestamp();
            break;
        case 1:
            if(Timer.getFPGATimestamp()>time+0.5){
            
                setElevatorPower(-0.4);
            }
            

            break; 
        }
        }else{
            
            setShifterState(true);
            setElevatorPower(0.1);
            m=0;
        }
    }
 
    

    @Override
    public void outputToDashBoard() {

    }

    double mark = 1;
    public void closedLoopElevator(double mmOffGround,double maxSpeed){
         if( (getPosition() - mmOffGround) > 0) {
            mark = -1;
        }else{
            mark = 1;
        }
        
        setElevatorPower(mPid.elevatorPID(mmOffGround, getPosition(), maxSpeed));
        /*
       if(getPosition() > mmOffGround -20 && getPosition() < mmOffGround +20){
        setElevatorPower(-mPid.elevatorPID(mmOffGround,getPosition(),-0.5));
       }
       else{
        setElevatorPower(-mPid.elevatorPID(mmOffGround,getPosition(),0.8));
       } */

       
    }


    public void setElevatorPower(double voltage){
        elevator1.set(-voltage);
        elevator2.set(voltage);
    }
    

    public void hangElevator(){

        if(!elevatorLock.get()){
            setElevatorPower(0.1);;
            elevatorLock.set(true);
        }else {
            setElevatorPower(0);
        }
    }

    public void stallElevator(){
        setElevatorPower(kStallVoltage);
        
    }

    public void setShifterState(boolean shifterState){
        elevatorLock.set(shifterState);
    }
    private double shifted = 0;
    public void releaseShifterAndGo(Command command){
        if(elevatorLock.get()){
            shifted = Timer.getFPGATimestamp();
            elevatorLock.set(false);
           

        }else{
            if(Timer.getFPGATimestamp() - shifted > 0.05){
                command.command();
            }
            
        }
        
    }

    public boolean isMoving(){
       return elevatorEncoder.getRate() > 0.2 || elevatorEncoder.getRate() < -0.2;
        
    }

    @Override
    public void logData() {
        System.out.println(getPosition());
    }
     public void makeArmSafeToUp(double inputHeight){
       
    }
    
  
    
} 