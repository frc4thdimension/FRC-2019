package frc.Math;
//Import Drive

import frc.subSystems.Drive;

public class pid{


    /*
    Initialization
    */

    
     static pid mInstance = new pid();

    public static pid getInstance(){
        return mInstance;
    }

    public double angle;
    public double gyroError = 0;
    public double prevGyroError=0;
    public double gyroIntegral=0;
    public double gyroDerivative;
    public double kGP = 0.135;//0.21
    public double kGI = 0.0001;//0.01
    
    public double kGD = 2.1;//2.5
    public double gyroPIDres;
    public double reSetter=1;
    public double encoderError;
    public double prevEncoderError;
    public double encoderIntegral;
    public double encoderDerivative;
    public double Current,rampError,rampIntegral=0;
    public double deRampError, deRampIntegral=0;
    private double derampP = 0.2;
    private double derampI = 0.0005;
    private double derampD = 0.3;
    private double preverrorD = 0;
    private double derivative;
    public double rampDownRes;
    private double rampP = 0.1;
    private double rampI = 0.3;
    private double turnError;
    public double turnPIDres;
    private double turnP=0.14;
    private double turnI=0.004;
    private double turnD=1.05;
    private double turnIntegral;
    private double turnDerivative;
    private double turnAngle;
    private double prevTurnError=0;
    private double xyPrevError = 0;
    private double xyError;
    private double xyIntegral;
    private double xyDerivative;
    private double xyIK = 0.1;
    private double xyPK = 0;
    private double xyDK = 0.085;
    public double xyRes;
    private double xDiff;
    private double xaError;

    private double xaP = 0;
    public double xaRes;

    private double elevError;
    private double prevElevError = 0;
    private double elevIntegral;
    private double elevDerivative;
    private double elevI = 0;
    private double elevP = 0.01;
    private double elevD = 0;
    public double elevRes;

    private double elevDownError;
    private double prevElevDownError = 0;
    private double elevDownIntegral;
    private double elevDownDerivative;
    private double elevDownI = 0;
    private double elevDownP = 0;
    private double elevDownD = 0;
    public double elevDownRes;

    //arm errors
    private double armError;
    private double armDerivative;
    private double armIntegral;
    private double prevArmError = 0;
    private double armD = 0.0;
    private double armP = 0.02;
    private double armI = 0.0;
    public double armRes = 0;
    private double armBrakePower = 0;

    

    /*
    Constructor 
    */
    public pid(){
        
    }


    /*
    GyroPid(wantedAngle)
    1-get error from angle
    2-multiply error with .02 and sum it to integral 
    3-find difference between previous error and current error
    4-output well-tuned PID value
    */
    public void gyroPID(double angle,double currentValue){

        this.angle = angle;
        gyroError = currentValue-this.angle;
        gyroIntegral += gyroError*0.02;
        gyroDerivative  = gyroError-prevGyroError;
        gyroPIDres = gyroError*kGP + gyroDerivative*kGD + gyroIntegral*kGI;

        prevGyroError = gyroError;

    }

    
    public void turnPID(double angle, double currentAngle){
        this.turnAngle = angle;
        turnError = turnAngle-currentAngle;
        turnIntegral += turnError*0.02;
        turnDerivative = turnError-prevTurnError;
        turnPIDres = turnError*turnP+turnDerivative*turnD+turnIntegral*turnI;
        if(turnPIDres>0.7){
            turnPIDres =0.7;
        }else if(turnPIDres<-0.7){
            turnPIDres=-0.7;
        }
        prevTurnError = turnError;


    }
    public void slowturnPID(double angle, double currentAngle){
        this.turnAngle = angle;
        turnError = turnAngle-currentAngle;
        turnIntegral += turnError*0.02;
        turnDerivative = turnError-prevTurnError;
        turnPIDres = turnError*turnP+turnDerivative*turnD+turnIntegral*turnI;
        
        if(turnPIDres > 0.6){
            turnPIDres = 0.6;
        }else{

        }
        prevTurnError = turnError;


    }

    public double rampingUpPI(double accDistance,double current){

        this.Current = current/100;
        rampError = accDistance - Current;
        rampIntegral += rampError*.02;
        return rampI*rampIntegral + rampError*rampP;

     }

     public void rampingDownPID(double deaccDistance, double current){

        this.Current = current;
        deRampError = deaccDistance - Current;
        deRampIntegral += deRampError * .02;
        derivative = deRampError-preverrorD;
        rampDownRes = (derampI* deRampIntegral + derampP * deRampError+ derampD * derivative)/5;
        preverrorD = deRampError;


     }

 

 public void setRampZero(){
    rampError = 0;
    rampIntegral = 0;
 }

    /*
    EncoderPid(wantedDistance)
    WARNING: Just activate within the couple of cm to target
    1-get Error to Distance
    2-multiply it by .02 and add up to integral
    3-find difference between current and previous error
    4-output well-tuned PID value
     */

     

 public void xyGyroPID(double angle, double currentAngle, double currentX, double X,double maxS){
/*
    xDiff = (X - currentX)/200;
    xyError = angle - currentAngle;
    xyIntegral += xyError*0.02;
    xyDerivative = xyError - xyPrevError;
    xyRes = xyError* xyPK + xyDerivative* xyDK+ xyIntegral* xyIK*xDiff;

    xyPrevError = xyError;
*/
    
    xaError = angle - currentAngle;
   
    xaRes = xaError* xaP;

   
    
    xaP = 0.45/xyError;
    

    xyError = X - currentX;    
    
   
    xyRes = xyError* xyPK;

    xyPK = 1/xaError;

    if(xyRes > maxS){
        xyRes = maxS;
        
 
 }else{

 }

  
 }

 public double elevatorPID(double wantedLevel, double currentLevel,double maxSpeed){
    elevError = wantedLevel- currentLevel;
    elevIntegral += elevError*0.02;
    elevDerivative = elevError- prevElevError;

    elevRes = elevError*elevP+ elevDerivative*elevD+ elevIntegral*elevI;

    prevElevError = elevError;
    if(currentLevel - wantedLevel > 0){
        if(elevRes < -maxSpeed){
            elevRes = -maxSpeed;
            
        }
    }else{
    if(elevRes > maxSpeed ){
        elevRes = maxSpeed;
        
    }
}
    return elevRes;
 }

 public void elevatorDownPID(double wantedLevel, double currentLevel){
    elevDownError = wantedLevel- currentLevel;
    elevDownIntegral += elevDownError*0.02;
    elevDownDerivative = elevDownError- prevElevDownError;

    elevDownRes = elevDownError*elevDownP+ elevDownDerivative*elevDownD+ elevDownIntegral*elevDownI;

    prevElevDownError = elevDownError;
 }

 public double armPID(double wantedLevel, double currentLevel){
    
        armError = wantedLevel- currentLevel;
        armIntegral += armError*0.02;
        armDerivative = armError - prevArmError;

        armRes = armError*armP+armIntegral*armI+ armDerivative*armD;
        prevArmError = armError;

        if(wantedLevel - currentLevel > 0 && armRes > 0.55){
            if(wantedLevel > 140 && currentLevel > 130){
                armRes = 0.25;
            }else{
                armRes = 0.55;
            }
           
        }else if( wantedLevel - currentLevel < 0 && armRes < -0.35 ){
            armRes = -0.35;
        }
        return armRes;
        
   }

   
/*
   public double backArmPID(double wantedLevel,double currentLevel,double maxSpeed){
    armError = wantedLevel- currentLevel;
    armIntegral += armError*0.02;
    armDerivative = armError - prevArmError;

    armRes = armError*armP+armIntegral*armI+ armDerivative*armD;
    prevArmError = armError;
    if(armRes>=maxSpeed){
        armRes= maxSpeed;
    }
    return armRes;
    
   }
   */
}