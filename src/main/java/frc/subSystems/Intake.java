package frc.subSystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import frc.states.Command;


public class Intake {

    static Intake mInstance = new Intake();

    public static Intake getInstance(){
        return mInstance;
    }

    public enum IntakeStates{
        hatchIntake,
        HatchClosed,
        HatchIn(),
        HatchOut;
    }

    IntakeStates currentHatchState = IntakeStates.HatchClosed;

    //public VictorSP ballIntake;
    //public VictorSP ballIntakeArm;
    
    public Solenoid hatchOut;
    public VictorSP intakeMotor;
    VictorSP rollerMotor;
    public double longMidClosed;
    public double startTimeMid;
    DigitalInput switchBall;
    double hatchMidTimer;

    //public Ultrasonic roboUltrasonic;

    public Intake(){
        //ballIntake = new VictorSP(10);
        //ballIntakeArm = new VictorSP(11);
        switchBall = new DigitalInput(0);
        rollerMotor = new VictorSP(6);
        
        intakeMotor= new VictorSP(5);
        
        hatchOut = new Solenoid(4);
        
        hatchOut.set(false);
       

        //roboUltrasonic = new Ultrasonic(16, 17);
        //roboUltrasonic.setAutomaticMode(true);
    }

    public void hatchIntake(Command command){
        
    }

    int a = 0;
    double startTime;
    public void hatchShoot(double delayTime){
        switch(a){
            case 0:
            startTime = Timer.getFPGATimestamp();
            a++;
            break;
            case 1:
            if(Timer.getFPGATimestamp() - startTime < delayTime){
                
            }else{
                hatchOut.set(true);
                a = 0;
                break;
            }
            
        }
    }

    

    public void setIntakePower(double power){
        intakeMotor.set(power);
    }

    public void setRollers(double power){
        rollerMotor.set(-power);
    }



    public boolean ballDetected(){
        return switchBall.get();
    }

    /**
     * Switch and overriding method for hatch intake states
     */
   
}