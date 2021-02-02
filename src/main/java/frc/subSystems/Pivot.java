package frc.subSystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import frc.Math.pid;
import frc.states.Command;

public class Pivot{

    VictorSP pivotMotor;
    Encoder pivotEnc;
    pid PID;

    public boolean isPivotWantedAngle;


    static Pivot  mInstance = new Pivot();

    public static Pivot getInstance(){
        return mInstance;
    }

    public Pivot(){
        PID = pid.getInstance();
        pivotMotor = new VictorSP(2);
        pivotEnc = new Encoder(18,19,true);
        
    }

   public double getPivotAngle(){
       return pivotEnc.getDistance();
   }

   public void toIntakingPos(){

   }
   

   public void setPivotWithCommand(double theta,Command command){
    
    if(getPivotAngle() > theta -3 && getPivotAngle() < theta + 3){
        isPivotWantedAngle = true;
        pivotStop();
        command.command();
    }else{
        setPivotAngle(theta);
        isPivotWantedAngle = false;
    }

    }

    public void stallPivot(){
        pivotMotor.set(0.03);
    }

    public void pivotSlowUp(boolean isArmBacker){


    }

    public void pivotSlowDown(){



    }

    public void setPivotAngle(double angle){

    }

    public void pivotStop(){

        

    }

    public void setPivotPower(double power){

        pivotMotor.set(power);

        
    }

    public boolean isPivotDown(){

        return getPivotAngle()<=0.5;

    }

    public boolean isPivotTop(){

        return getPivotAngle()>=89.5;

    }

    
    public double getPivotEnc(){

        return pivotEnc.getDistance()-160;

    }

    public void holdInBallIntakePos(){
        
        if(getPivotEnc()<10 && getPivotEnc() > -3){
            stallPivot();
        }else{
            if(getPivotEnc() > 0){
                setPivotPower(1);
            }else{
                setPivotPower(-1);
            }
          
        }
    }

    public boolean isPivotBallIntaking(){
        return getPivotEnc() > -1;
    }

    public boolean isPivotFold(){
        return getPivotEnc() < -150;
    }
        

}