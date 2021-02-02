package frc.Auto.Actions;



import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.states.Command;
import frc.subSystems.Drive;

public class goToDistance implements Action{

    Drive mDrive;

    private int flagLastSector = 0;
    private double wantedDistance;
    private double afterAngle;
    private double outputPower;
    private double lastSectorOutput;
    private double cruiseV;
    private boolean isForward;
    boolean finished = false;
    double error;
    double prevError;
    double integral;
    final double kP = 1.2;
    final double kD = 1;
    double timeStarted;

    

    public double pidForward(double wantedD){
        error =  wantedD - mDrive.getAvgDistance();
        return kP*error;


    }
    public void goForward(){
        System.out.println("ım in t he method");
        

    }

    public void goBackwards(){

    }

    public void goDistance(double distance){
        isForward = distance < 0;
        if(isForward){
            goForward();
        }else{
            goBackwards();
        }
    }

    Command commander;

    public goToDistance(double distance,double afterAngle,double cruiseV,Command com){
        commander = com;
        outputPower = 0;
        mDrive = Drive.getInstance();
        this.wantedDistance = distance;
        this.afterAngle = afterAngle;
        this.cruiseV = cruiseV;
    }

    @Override
    public void start() {
        flagLastSector = 0;
        timeStarted = Timer.getFPGATimestamp();
        lastSectorOutput = 0;
    }

    @Override
    public void update() {
        SmartDashboard.putNumber("avgjdsd", mDrive.getAvgDistance());


        commander.command();
        if(mDrive.getAvgDistance() <  wantedDistance*0.25){
            outputPower+=  0.005;
            if(outputPower >= cruiseV){
                outputPower = cruiseV;
            }

        }else if(mDrive.getAvgDistance() > wantedDistance * 0.25 && mDrive.getAvgDistance() < wantedDistance*0.5){
            
        }else if(mDrive.getAvgDistance() > wantedDistance * 0.5 && mDrive.getAvgDistance() < wantedDistance*0.8){
            outputPower-=0.01;
            if(outputPower < 0.4){
                outputPower = 0.4;
            }
            
        }else{
           outputPower = pidForward(wantedDistance);
            if(outputPower > 0.4){
                outputPower = 0.4;
            }

        }

        mDrive.robotDrive.arcadeDrive(outputPower,(mDrive.getAngle()-afterAngle )*0.2);
        }

    
        
        
    

    @Override
    public void done() {
        
        outputPower = wantedDistance *-0.05 / 300;
        mDrive.robotDrive.curvatureDrive(outputPower, 0, false);
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp()-timeStarted > 4 || mDrive.getAvgDistance()>wantedDistance-5 && mDrive.getAvgDistance() < wantedDistance+5;
    }
}