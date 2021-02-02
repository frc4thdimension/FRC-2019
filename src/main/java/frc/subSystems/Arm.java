package frc.subSystems;

import java.util.ArrayList;
import java.util.List;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Math.pid;
import frc.states.Command;

public class Arm extends Subsystem {

    static Arm mInstance = new Arm();

    public static Arm getInstance() {
        return mInstance;
    }

    // Constants
    public final double kArmFullMotionInDegree = 283;
    public final double kArmMaxVelocity = 220;
    private final double kArmStallTorque = 239.27;
    private final double armInitialAngle = (360 - 283) / 2;

    VictorSPX armMotor;
    Encoder armEncoder;
    PowerDistributionPanel pdp;
    Thread armThread,armThread1;
    pid PID;

    enum armMotorState {
        Coast, Brake;
    }

    private boolean isVoltageRegulating = false;
    private double initialVoltage;
    private double realAppliedVoltage;
    private double rampDownDurationInAngles;
    private double outputPower;
    public ArrayList<Double> feedForwardVoltage200;
    public ArrayList<Double> feedForwardVoltage90;
    public ArrayList<Double> feedForwardVoltage50;
    public ArrayList<Double> feedForwardVoltageBase;
    public ArrayList<Double> feedForwardVoltage;
    
    // setConstantPower()
    private double mandotaryOutput;

    public Arm() {
        feedForwardVoltage200 = new ArrayList<>();
        feedForwardVoltage90 = new ArrayList<>();
        feedForwardVoltage50 = new ArrayList<>();
        feedForwardVoltageBase = new ArrayList<>();

      
        pdp = new PowerDistributionPanel();
        initialVoltage = 12.7;
        armMotor = new VictorSPX(1);
        /* armMotor.configVoltageCompSaturation(12.7);
        armMotor.enableVoltageCompensation(true);
        armMotor.setNeutralMode(NeutralMode.Brake); */
        armEncoder = new Encoder(14, 15, false);
        armEncoder.setDistancePerPulse(0.133);
        PID = pid.getInstance();
    }

    /**
     * gets arm angle via encoder rate
     * 
     * @return
     */
    public double getArmAngle() {
        return -armEncoder.getDistance() + 9.55;
    }

    /**
     * Applies +- deadband to the value given with the max Value Given
     * 
     * @param value
     * @param maxValue
     * @return
     */
    public double applyDeadband(double value, double maxValue) {
        if (value >= maxValue) {
            return maxValue;
        } else if (value <= -maxValue) {
            return -maxValue;
        } else {
            return value;
        }
    }

    /**
     * @return Arm's angle to the elevator
     */
    public double getRealArmAngle() {
        return armInitialAngle + getArmAngle() - 9.55;
    }

    public enum LimitDirection {
        Higher, Lower;
    }

    public double limit(double number, double limitNumber, LimitDirection limitD) {
        switch (limitD) {
        case Higher:
            if (number >= limitNumber) {
                return limitNumber;
            } else {
                return number;
            }

        case Lower:
            if (number <= limitNumber) {
                return limitNumber;
            } else {
                return number;
            }
        default:
            return 0;
        }

    }

    public double getArmAngleToGround() {
        return getArmAngle() + armInitialAngle;
    }

    public double getArmAngleInRadians() {
        return Math.toRadians(getArmAngle());
    }

    public void setInitialVoltage(double iVoltage) {
        initialVoltage = iVoltage;
    }

     public void isBrakeMode(boolean isBraked) {
        if (isBraked) {
            armMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            armMotor.setNeutralMode(NeutralMode.Coast);
        }
    } 

    /**
     * Actuates the voltage compensating mode When this mode is activated, motor
     * output is arranged depending on the current voltage of the battery Dont
     * forget to set the initial battery voltage
     * 
     * @see setInitialVoltage()
     * @param isVoltageRegulating
     */
    public void setisVoltageRegulating(boolean isVoltageRegulating) {
        this.isVoltageRegulating = isVoltageRegulating;
    }

    public boolean isArmBack() {
        return getArmAngle() > 140;
    }

    public boolean isArmBottom() {
        return getArmAngle() < 20;
    }

    public void setArmPower(double power) {
        applyDeadband(power, 1);
        if (this.isVoltageRegulating) {
            setArmPowerVoltageRegulating(power);
        } else {
            setConstantPower(power);
        }

    }

    public void setArmPowerSimp(double power) {
        armMotor.set(ControlMode.PercentOutput,power);
    }

    /**
     * Voltage Compensation control to be sure that we apply same torque all the
     * time.
     * 
     * @param power
     */
    public void setArmPowerVoltageRegulating(double power) {
        realAppliedVoltage = initialVoltage * power;
        outputPower = realAppliedVoltage / pdp.getVoltage();
        setPower(outputPower);

    }

    public void sstallArm(){
        setPower(torqueToVoltage(getArmInitalTorque()));
    }
    /**
     * Stalling arm in every angle effecciently
     */
    public void stallArm() {
        if (isArmBack()) {
            setArmPower(-0.07);
        } else {
            if (isArmBottom()) {
                System.out.println("anan");
                setArmPower(0);
            } else {
                setArmPower(0.07);
            }
        }
    }

    public void setArmAngle(double wantedAngle) {
        PID.armPID(wantedAngle, getArmAngle());
        if (getArmAngle() > wantedAngle - 5 && getArmAngle() < wantedAngle + 5) {
            sstallArm();
        } else {
            setArmPower(PID.armRes);

        }

    }

    public void setArmAngleFollowUp(double wantedAngle, Command command) {
        PID.armPID(wantedAngle, getArmAngle());
        if (getArmAngle() > wantedAngle - 5 && getArmAngle() < wantedAngle + 5) {
            stallArm();
            command.command();
        } else {
            setArmPower(PID.armRes);

        }

    }

    public void setArmAngleFollowUpHighTolerance(double wantedAngle, Command command) {
        PID.armPID(wantedAngle, getArmAngle());
        if (getArmAngle() > wantedAngle - 6.5 && getArmAngle() < wantedAngle + 6.5) {
            stallArm();
            command.command();
        } else {
            setArmPower(PID.armRes);

        }

    }

    public void setArmToZero() {
        if (getArmAngle() >= 50) {
            setArmPowerSimp(-0.5);
        } else if (getArmAngle() < 50 && getArmAngle() >= 15) {
            setArmPowerSimp(-0.2);
        } else {
            setArmPowerSimp(0);
        }
    }
    public void returnArmToBase(){
        armThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("ım in");
           
                outputValue = feedForwardVoltageBase.get(roundUpAngle(getArmAngle())) + feedBackController(roundUpAngle(getArmAngle()-1));
               
               
                if(outputValue >= 0.75){
                    outputValue = 0.75;
                }
                if(outputValue <= -0.75){
                    outputValue = -0.75;
                }

                if(getArmAngle() < 12 && getArmAngle() > 8 ){
                    sstallArm();
                }else{
                    setConstantPower(feedForwardVoltageBase.get(roundUpAngle(getArmAngle())));
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
        });
        armThread.start();
    }
    public void setArmToZeroFollowUp(Command command) {
        if (getArmAngle() >= 50) {
            setArmPowerSimp(-0.5);
        } else if (getArmAngle() < 50 && getArmAngle() >= 12) {
            setArmPowerSimp(-0.2);
        } else {
            setArmPowerSimp(0);
            command.command();
        }
    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void checkSensors() {

    }

    @Override
    public void outputToDashBoard() {
        SmartDashboard.putNumber("ArmAngle", getArmAngle());
        SmartDashboard.putBoolean("ArmOnBack", isArmBack());
    }

    @Override
    public void logData() {

    }

    /**
     * 
     * @param torqueValue wanted torque value
     * @return voltage output of the given torque value
     */
    public double torqueToVoltage(double torqueValue) {
        return torqueValue / kArmStallTorque;
    }

    /**
     * 
     * @param voltageValue
     * @return torque return of the given voltage value
     */
    public double voltageToTorque(double voltageValue) {
        return voltageValue * kArmStallTorque;
    }

    /**
     * Rounds up the angle value to smaller integer
     * 
     * @param angle
     * @return
     */
    public int roundUpAngle(double angle) {
        return (int) angle;
    }

    /**
     * 
     * @param voltage
     * @return Velocity value corresponding to the input voltage value
     */
    public double voltageToVelocity(double voltage) {
        return voltage * kArmMaxVelocity;
    }

    /**
     * 
     * @param voltage
     * @return Voltage value corresponding to the input velocity value
     */
    public double velocityToVoltage(double velocity) {
        return velocity / kArmMaxVelocity;
    }

    /**
     * Get arm's cosinus to the state where arm is perpendicular to the ground
     * 
     * @return
     */
    public double getArmCosinus() {
        return Math.sin(Math.toRadians(getRealArmAngle()));
    }

    /**
     * Get the torque arm creates with its own weight and arm Lenght
     * 
     * @return Arm Torque in N*m
     */
    public double getArmInitalTorque() {
        return 50 * 0.5 * getArmCosinus();
    }

    /**
     * Arm's created torque not considering its own weight depending on the given
     * voltage
     * 
     * @param inputVoltage
     * @return
     */
    public double getArmMotorTorque(double inputVoltage) {
        return inputVoltage * kArmStallTorque;
    }

    /**
     * Arm's total produced torque
     * 
     * @param s
     * @return
     */
    public double getRealArmTorque(double s) {
        return getArmMotorTorque(s) - getArmInitalTorque();
    }

    /**
     * 
     * @return Arm's angular velocity min-max(0-220)
     */
    public double getArmSpeed() {
        return armEncoder.getRate();
    }

    public void setAccDurationInAngles(double angle) {

    }

    /**
     * Calculates the power needed to keep the arm at same velocity
     * 
     * @param voltage
     * @return voltage value depending on the arm angle
     */
    public double getConstantPower(double voltage) {
        mandotaryOutput = voltageToTorque(voltage) + getArmInitalTorque();
        return torqueToVoltage(mandotaryOutput);
    }

    /**
     * Straight Power Setting To Motors
     * 
     * @param power
     */
    public void setPower(double power) {
        armMotor.set(ControlMode.PercentOutput,power);
    }

    /**
     * Gives different voltage values at different angles to keep the arm at same
     * speed
     * 
     * @see getConstantPower()
     * @param wantedVoltage
     */
    public void setConstantPower(double wantedVoltage) {
        // If condition to eliminate errors may be caused by asymetrical shape of the
        // arm
        if (!isArmBack() && wantedVoltage < 0) {
            // Lower the output minus 1/3 since arm is felt heavier by the pivot
            setArmPowerVoltageRegulating(Math.abs(wantedVoltage) / 3 + getConstantPower(wantedVoltage));
        } else {
            setArmPowerVoltageRegulating(getConstantPower(wantedVoltage));
        }

    }

    // move on later...
    public double cruiseTimeSeconds;
    double cruiseDurationInAngles;
    public double cruiseLimit;
    public double accRatePerLoop;
    double cruiseEndPoint;
    double rampDownDistance;
    public double accRatePerAngle;
    public double decreasedSpeedTotal = 0;
    public double startAngle;
    double setFlag = 0;
    int a = 0;

    /**
     * Creates smooth trajectory to follow with ramp down at the end
     * 
     * @param wantedAngle
     * @param cruiseVoltage
     */
    public void createArmTrajectory(double wantedAngle, double cruiseVoltage,List<Double> voltageList) {

        startAngle = 9.55;
        if(wantedAngle < 140){
            rampDownDurationInAngles = wantedAngle * 6/9;
        }else{
            rampDownDurationInAngles = wantedAngle * 5/9;
        }
       
        cruiseDurationInAngles = (wantedAngle - rampDownDurationInAngles) - startAngle;
        cruiseLimit = cruiseDurationInAngles + startAngle;
        accRatePerAngle = cruiseVoltage / rampDownDurationInAngles;

        for (a = 0; a < wantedAngle; a++) {
            voltageList.add(a, cruiseVoltage);
        }
        for (int b = (int) cruiseLimit; b < 270; b++) {
          
                voltageList.add(b, cruiseVoltage - ((b - cruiseLimit) * accRatePerAngle));
           
        }
    }

     private double travelledDistance = 0;
     public int backCounter = 0;
    public void createArmTrajectoryBackWards(double wantedAngle,double cruiseVoltage,List<Double> voltageList){
        startAngle = 250;
        travelledDistance = 250-wantedAngle;
        rampDownDurationInAngles = (startAngle - wantedAngle)*5/9;
        cruiseDurationInAngles = (travelledDistance - rampDownDurationInAngles);
        cruiseLimit = wantedAngle + rampDownDurationInAngles;
        accRatePerAngle = cruiseVoltage / rampDownDurationInAngles;
        for (int bo = 0 ; bo <= wantedAngle; bo++) {
        voltageList.add(bo, 0.0);
        }

        for (int b = (int) wantedAngle; b <= 250; b++) {
            if(b<=cruiseLimit){
                voltageList.add(b, -cruiseVoltage+(cruiseLimit-b)*accRatePerAngle);
            }else{
                voltageList.add(b, -cruiseVoltage);
            }
            
        }
        

    } 


    public double outputTrial;
    public double outputValue;
    public void executeTrajectory(List<Double> executableTrajectory,double wantedAngle) {
        armThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("ım in");
               if(getArmAngle() <= wantedAngle + 1 && getArmAngle() >= wantedAngle - 1){
                outputValue = executableTrajectory.get(roundUpAngle(getArmAngle())) + feedBackController(wantedAngle);
               }else if(getArmAngle() < wantedAngle-1){
                outputValue = executableTrajectory.get(roundUpAngle(getArmAngle())) + feedBackController(roundUpAngle(getArmAngle()+1));
               }else{
                outputValue = executableTrajectory.get(roundUpAngle(getArmAngle())) + feedBackController(roundUpAngle(getArmAngle()-1));
               }
               
                if(outputValue >= 0.75){
                    outputValue = 0.75;
                }
                if(outputValue <= -0.75){
                    outputValue = -0.75;
                }

               
                    if(getArmAngle()>wantedAngle-2 && getArmAngle() < wantedAngle+2){
                        sstallArm();  
                    }else{
                        setConstantPower(outputValue);
                    }
                
                
                
                SmartDashboard.putNumber("outputValueOf the motor",executableTrajectory.get(roundUpAngle(getArmAngle())));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
        });
        armThread.start();
    }

     public double feedForwardError;
     public double feedForwardDerivative;
     public double feedForwardPreviousError;
     public final double kP = 0.05;
     public final double kD = 0;
     /**
      * Checks whether feedForward values jerked or not and fixes with additional PID values
      * @param forwardValues
      * @return
      * FeedBack of the system errors
      */
     public double feedBackController(double listInput){
        feedForwardError = getArmAngle() - listInput;
        feedForwardDerivative = feedForwardError-feedForwardPreviousError;
        feedForwardPreviousError = feedForwardError;
        return feedForwardError*-kP + feedForwardDerivative*kD;
        
    }

    public void executeTrajectoryWithFollowUp(List<Double> executableTrajectory,double wantedAngle,Command newCommand){
        armThread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("ım in");
               if(wantedAngle < 0){
                outputValue = executableTrajectory.get(roundUpAngle(getArmAngle())) + feedBackController(roundUpAngle(getArmAngle()-1));
               }else{
                outputValue = executableTrajectory.get(roundUpAngle(getArmAngle())) + feedBackController(roundUpAngle(getArmAngle()+1));
               }
               
                if(outputValue >= 0.75){
                    outputValue = 0.75;
                }
                if(outputValue <= -0.75){
                    outputValue = -0.75;
                }

                if(wantedAngle < 0){
                    if(getArmAngle() > -wantedAngle){
                        setConstantPower(outputValue);
                    }else{
                        sstallArm();
                        newCommand.command();
                    }
                }else{
                    if( getArmAngle()>wantedAngle-2 && getArmAngle() < wantedAngle+2){
                        newCommand.command();
                        sstallArm();  
                    }else{
                        setConstantPower(outputValue);
                    }
                }
                
                
                SmartDashboard.putNumber("outputValueOf the motor",executableTrajectory.get(roundUpAngle(getArmAngle())));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
        });
        armThread1.start();
    }

}



    
    




