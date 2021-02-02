package frc.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.subSystems.Intake;

public class hatchIntake implements Action {

    Intake mIntake;
    int pistonNumber;
    boolean pistonState;
    double time;
    double firstTime;
    boolean transitionFinished = false;

    /**
     * pistonNum: 0 - out
     *            1- in
     * @param pistonNum
     * @param state
     */
    public hatchIntake(int pistonNum, boolean state,double time){
        this.time = time;
        pistonNumber = pistonNum;
        pistonState = state;
        mIntake = Intake.getInstance();
    }

    @Override
    public void start() {
        firstTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
    if(Timer.getFPGATimestamp()-firstTime>time){
        if(pistonNumber==0){
            if(pistonState){
                mIntake.hatchOut.set(true);
                transitionFinished = true;
            }else{
                mIntake.hatchOut.set(false);
                transitionFinished = true;
            }

        }else if(pistonNumber == 1){
            if(pistonState){
               
                transitionFinished = true;
            }else{
              
                transitionFinished = true;
            }
        }
    }

    }

    @Override
    public void done() {
        transitionFinished = false;
        firstTime = 0; 
    }

    @Override
    public boolean isFinished() {
        return transitionFinished;
    }

}