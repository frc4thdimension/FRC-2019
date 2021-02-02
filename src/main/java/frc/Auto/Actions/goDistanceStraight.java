package frc.Auto.Actions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.subSystems.Drive;

public class goDistanceStraight implements Action{
    robotSide rs;
    robotSide travelWay;
    double arrivedDist;

    boolean isForward;
    double initialPosition;
    double finalPosition;
    double accDuration;
    double accDistance;
    double accRatePerCycle;
    double accRatePerSecond;
    double powerOutput;
    double at;
    double displacement;
    double startTime;
    double vInitial;
    double vFinal;
    int index;
    double t,tSq;
    double totalTime;
    double outPower;
    double a;//mm/s^2
    double cruiseVoltage;
    ArrayList<Double> voltageList;
    double voltageToAddToList;
    Drive mDrive = Drive.getInstance();
    public enum robotSide{
        OurDS(-1),
        TheirDS(-1);

        double mark;
        private robotSide(double m){
            mark = m;
        }
    }

    public double PowerToVelocity(double power){
        return power*483;
    } 
    public double VelocityToPower(double velocity){
        return velocity/483;
    }
    
    public void createTrajectory(){
        Thread creationThread = new Thread(new Runnable(){
        
            @Override
            public void run() {

                for(int counter = 0; counter <= totalTime*50 ; counter +=20){
                    if(counter <= t*50){
                        if(voltageToAddToList <= cruiseVoltage){
                            voltageToAddToList+=accRatePerCycle;
                        }else{
                            voltageToAddToList=cruiseVoltage;
                        }
                        voltageList.add(voltageToAddToList);
                    }else if(counter>t*50 && counter <= totalTime){
                        voltageList.add(cruiseVoltage);
                    }else{
                        if(voltageToAddToList > -accRatePerCycle*50 ){
                            voltageToAddToList-=accRatePerCycle;
                        }else{
                            voltageToAddToList = -accRatePerCycle*50;
                        }
                        voltageList.add(voltageToAddToList);
                    }
                }
                
            }
        });
        creationThread.start();
    } 
    
    public goDistanceStraight(double distanceToGo,double distanceStarted ,
    double cruiseVoltage,double vInitial,boolean isForward){
        arrivedDist = distanceToGo;
        displacement = distanceToGo - distanceStarted;
        accDistance = displacement/3;
        this.vInitial = PowerToVelocity(vInitial);
        vFinal = PowerToVelocity(cruiseVoltage); 
        at = vFinal - this.vInitial;
        t = (2*accDistance)/at;
        tSq = t * t;
        a = (2*accDistance)/tSq;
        accRatePerCycle = VelocityToPower(a)/50;
        totalTime = (accDistance/PowerToVelocity(cruiseVoltage)) * 2 * t;
        accDistance = distanceToGo / 3;
        powerOutput = 0;
        this.cruiseVoltage = cruiseVoltage;
        this.isForward = isForward;
        
    }

   
    @Override
    public void start() {
        SmartDashboard.putNumber("t", t);
        index = 0;
        powerOutput = 0;
        outPower = 0;
        
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        System.out.println("power: "+outPower);
        System.out.println("powerup: "+ t);
        System.out.println("accrate: "+ accRatePerCycle);
        SmartDashboard.putNumber("lookkkk", outPower);
        SmartDashboard.putNumber("encoefgyd", mDrive.getDistance());

        SmartDashboard.putNumber("total", totalTime);
       
       if(mDrive.getDistance() <= accDistance){
        outPower += 0.01;
        
        if(outPower > cruiseVoltage){
            outPower = cruiseVoltage;
        } 
       
        mDrive.setSeperately(outPower,outPower);
       }else if(mDrive.getDistance()>accDistance && mDrive.getDistance() < accDistance*2){
           System.out.println("wtf");
        mDrive.setSeperately(outPower,outPower);
       }else{
            System.out.println("l");
            outPower -= 0.001;
            if(outPower < -accRatePerCycle*10){
                outPower = -accRatePerCycle*10;
            } 
        mDrive.setSeperately(outPower,outPower);
       }
       

    }

    @Override
    public void done() {
        mDrive.robotDrive.curvatureDrive(0, 0, false);
    }

    @Override
    public boolean isFinished() {
        return mDrive.getDistance() > arrivedDist-5 && mDrive.getDistance() < arrivedDist+5;
    }

}