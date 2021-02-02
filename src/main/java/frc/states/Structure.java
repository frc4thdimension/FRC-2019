
package frc.states;

import java.util.ArrayList;

import frc.subSystems.*;


public class Structure {
    static Structure mInstance = new Structure();
    public static Structure getInstance(){
        return mInstance;
    }
  
    public double elevatorHeight;
    public double armAngle;
    public double PivotAngle;
    Teleop mTeleop;
    Elevator mElevator;
    Arm mArm;
    Pivot mPivot;
    Intake mIntake;
    boolean isTherePivotMovement;
    boolean towardsCollision;
    public final double elevatorDefaulHeight = 100;

    /**
     * Arm Pre-FeedForward lists
     */
    public ArrayList<Double> hatchPivot;
    public ArrayList<Double> hatchMid;
    public ArrayList<Double> hatchHigh;
    public ArrayList<Double> ballPivot;
    public ArrayList<Double> ballCargo;
    public ArrayList<Double> ballLow;
    public ArrayList<Double> ballMid;
    public ArrayList<Double> ballHigh;
    public ArrayList<Double> hatchHighRear;
    public ArrayList<Double> hatchMidRear;
    public ArrayList<Double> armDown;
    public ArrayList<Double> ballHighRear;
    public ArrayList<Double> ballMidRear;
    public ArrayList<Double> hatchLow;
    public ArrayList<Double> escape;
    public ArrayList<Double> hatchPivotReturn;
    
    
    

    public enum robotStates{
        armDown(0,10,0,false,2),
        Starting(0,9.5,-160,false,2),
        BallPivot(1,0,0,false,0),
        HatchPivot(1,43,-160,false,1),
        HatchMid(610,55,-160,true,1),
        HatchMidAuto(540,50,-160,true,1),
        HatchLow(105,37,-160,true,1),
        HatchMidRear(0,230,0,true,1),
        HatchHighRear(553,200,0,true,1),
        BallHighRear(640,190,0,true,0),
        BallMidRear(511,235,0,true,0),
        HatchHigh(620,90,-160,true,1),
        BallLow(150,35,0,true,0),
        BallMid(620,50,0,true,0),
        BallCargo(530,42,0,true,0),
        BallHigh(640,100,0,true,0),
        Escape(0,70,0,true,0);
        
     

        double elevatorHeight,armAngle,PivotAngle,usage;
        boolean isScoring;
        robotStates(double height,double armAngle,double PivotAngle,boolean isScore,double usage){
            this.elevatorHeight = height;
            this.armAngle = armAngle;
            this.PivotAngle = PivotAngle;
            this.isScoring = isScore;
            this.usage = usage;
        }

    }
    
    public Structure(){
        mTeleop = Teleop.getInstance();
        
        mIntake = Intake.getInstance();
        
        mElevator = Elevator.getInstance();
        mArm = Arm.getInstance();
        mPivot = Pivot.getInstance();

        hatchHigh = new ArrayList<>();
        hatchPivot = new ArrayList<>();
        hatchMid = new ArrayList<>();
        hatchHigh = new ArrayList<>();
        ballLow = new ArrayList<>();
        ballPivot = new ArrayList<>();
        ballMid = new ArrayList<>();
        ballHigh = new ArrayList<>();
        ballHighRear = new ArrayList<>();
        ballMidRear = new ArrayList<>();
        hatchHighRear = new ArrayList<>();
        hatchMidRear = new ArrayList<>();
        armDown = new ArrayList<>();
        hatchLow = new ArrayList<>();
        escape = new ArrayList<>();
        hatchPivotReturn = new ArrayList<>();



    }



    public robotStates currentState = robotStates.Starting;

  

    public void startToIntakeHandler(robotStates wantedState){
        if(wantedState.equals(robotStates.BallPivot) && currentState.equals(robotStates.Starting)){

            if(!a){
                mElevator.setElevatorPID(105);
                
                setArmAngleToStateFollowUp(robotStates.Escape,new Command(){
                
                    @Override
                    public void command() {
                        if(!mPivot.isPivotBallIntaking()){
                            mPivot.holdInBallIntakePos();
                        }else {
                            a = true;
                            mPivot.stallPivot();
                        }
                        
                    }
                });
               
            }else {
                mPivot.setPivotPower(0);
               if(mArm.getArmAngle() > 25){
                mArm.setArmPower(-0.5);
               }else if(mArm.getArmAngle() < 25 && mArm.getArmAngle() > 15){
                mArm.setArmPower(-0.1);
               }else{
                mArm.setArmPower(0);
                
                if(!mElevator.isElevatorDown()){
                    mElevator.setBallIntakingPos();
                }else{
                    mElevator.setElevatorPower(0);
                    currentState = robotStates.BallPivot;
                    a = false;
                }
               }
             }
        }else if(wantedState.equals(robotStates.HatchPivot) && currentState.equals(robotStates.Starting)){
           setArmAngleToStateFollowUp(robotStates.HatchPivot,new Command(){
           
               @Override
               public void command() {
                   currentState = robotStates.HatchPivot;
               }
           });

        }
}

    public void scoreToscoreHatch(robotStates rs){
        if(!isStateEqual(rs)){
            mElevator.setElevatorPID(rs.elevatorHeight);
            mArm.setArmAngle(rs.armAngle);
        }else{
            mArm.sstallArm();
            mElevator.setShifterState(true);
            mElevator.setElevatorPower(0);
            currentState = rs;
        }
    }

    public void scoreToscoreBall(robotStates rs){
        mIntake.setRollers(-0.1);
        if(!isStateEqual(rs)){
            mElevator.setElevatorPID(rs.elevatorHeight);
            setArmAngleToState(rs);
        }else{
            setArmAngleToState(rs);
            mElevator.setShifterState(true);
            mElevator.setElevatorPower(0);
            currentState = rs;
        }
    }
    public void scoreToscoreHandler(robotStates wantedState){
        if((currentState.usage == 0 && wantedState.usage == 0) && !currentState.equals(wantedState)){
            scoreToscoreBall(wantedState);
        }else if((currentState.usage == 1 && wantedState.usage == 1) && !currentState.equals(wantedState)){
            scoreToscoreHatch(wantedState);
        }else{
            System.out.println("I cant");
        }
    }

    double turnDistance;
    boolean j = false;
    public void scoreToBall(robotStates states){
        if(!mPivot.isPivotBallIntaking()){
            mPivot.setPivotPower(-0.75);
        }else{
            mPivot.setPivotPower(0);
            if(mElevator.getPosition() > 330){
                mElevator.setBallIntakingPos();
                mArm.setArmAngle(50);
            }else{
                mElevator.setBallIntakingPos();
                mArm.setArmToZeroFollowUp(new Command(){
            
                    @Override
                    public void command() {
                        if(mElevator.getPosition() > 15 ){
                            mElevator.setBallIntakingPos();
                        }else{
                            currentState = states;
                            j = false;
                        }
                    }
                });
            }
        
        }
    }
    
    double l = 0;
    public void scoreToHatch(robotStates states){
        if((mElevator.getPosition()>10 || l == 0 )&& currentState!=states){
           
           setArmAngleToStateFollowUp(states,new Command(){
            
                @Override
                public void command() {
                    l = 1;
                    System.out.println("jdskdks");
                }
            });
            mElevator.setBallIntakingPos();

        }else {
            System.out.println("hhhknjnjnnnjjjuuukıjuhujujujuhyhyjuju");
            mElevator.setElevatorPower(0);
            mArm.sstallArm();
            currentState = states;
            l = 0;
            j = false;
        }
         
         
        }
       
    
    boolean a = false;

    public void scoreToIntakeHandler(robotStates wantedState){
        if((currentState.equals(robotStates.BallLow)||currentState.elevatorHeight==0) && !j){
         
            mElevator.setElevatorPIDafterCommand(100,new Command(){
            
                @Override
                public void command() {
                    j = true;
                }
            });
        }else {
            if(wantedState.equals(robotStates.BallPivot)){
                scoreToBall(wantedState);
            }else{
                if(mArm.getArmAngle()<65 && !mPivot.isPivotFold()&&currentState.elevatorHeight < 250){
                    setArmAngleToState(wantedState);
                }else{
        
                    if(!mPivot.isPivotFold()){
                        setArmAngleToState(wantedState);
                        mPivot.setPivotPower(0.75);
                    }else{
                        mPivot.setPivotPower(0);
                        scoreToHatch(wantedState);
                    }
                }
                
                
            }
        }
            
           

    }
    public boolean b = false;
    public void scoreTohatch(){
        
    }
    public boolean x,z,y;
    public boolean isStateEqual(robotStates state){
         x = mArm.getArmAngle()
          + 5 > state.armAngle && mArm.getArmAngle() - 5 < state.armAngle;
         y = mElevator.getPosition() + 5 > state.elevatorHeight && mElevator.getPosition() -5 < state.elevatorHeight;
        return x && y;

    }
    public void HatchIntakeToScoring(robotStates wantedState){
        if(!isStateEqual(wantedState)){
            mElevator.setElevatorPID(wantedState.elevatorHeight);
            setArmAngleToState(wantedState);
            //mArm.setArmAngle(wantedState.armAngle);
          
        }else {
            System.out.println("ben hatchim");
            currentState = wantedState;
            mElevator.setShifterState(true);
            mArm.sstallArm();
            
        }
    }

    public void ballIntakeToScoring(robotStates states){
       
     
       
        mIntake.setRollers(-0.55);
        
        if(states.elevatorHeight > 100){
            
            if(mElevator.getPosition() < 80){
                mElevator.setElevatorPID(states.elevatorHeight);
            }else{
                if(!isStateEqual(states)){
                    mElevator.setElevatorPID(states.elevatorHeight);
                   // setArmAngleToState(states);
                    mArm.setArmAngle(states.armAngle);
                }else{
                    mElevator.setShifterState(true);
                    mArm.sstallArm();
                    
                    currentState = states;
                }
               
            }
            //Elevator Height Is Lower Than safe
        }else{
            if(currentState != states){
                if(mElevator.getPosition() <102){
                    mElevator.setElevatorPID(107);
                }else{
                    if(mArm.getArmAngle() < 70){
                        mElevator.setShifterState(true);
                        setArmAngleToState(states);
                    }else{
                        mElevator.setShifterState(true);
                        setArmAngleToStateFollowUp(states,new Command(){
                        
                            @Override
                            public void command() {
                                currentState = states;
                            }
                        });
                    }
                   
                }
            }else {
             mElevator.setBallIntakingPos();
            mArm.sstallArm();
            }
        }
    }

    public void IntakeToScoreHandler(robotStates wantedState){

       if(currentState.equals(robotStates.HatchPivot) && wantedState.usage == 1){
        HatchIntakeToScoring(wantedState);
       }else if(currentState.equals(robotStates.BallPivot) && wantedState.usage == 0){
        ballIntakeToScoring(wantedState);
       }else {
           System.err.println("fu");
       }

           
        }

       

    

    boolean firstPhaseDone = false;
    boolean isArmUp = false;
    boolean isElevatorUp = false;
    public void IntakeToIntakeHandler(robotStates wantedState){

        if(currentState.equals(robotStates.HatchPivot)&& wantedState.equals(robotStates.BallPivot)){
            if(!firstPhaseDone){
                if(isArmUp&&isElevatorUp){
                    
                    if(!mPivot.isPivotBallIntaking()){
                        mPivot.holdInBallIntakePos();
                    }else {
                        
                        mPivot.stallPivot();
                        mArm.setArmToZeroFollowUp(new Command(){
                            
                            @Override
                            public void command() {
                                System.out.println("ım going down now");
                                if(!mElevator.isElevatorDown()){
                                    mElevator.setBallIntakingPos();
                                }else{

                                    firstPhaseDone = true;
                                }
                            }
                        });
                    }
                }else{

                    mElevator.setElevatorPIDafterCommand(elevatorDefaulHeight,new Command(){
                
                        @Override
                        public void command() {
                            isElevatorUp = true;
                        }
                        });
                    mArm.setArmAngleFollowUp(70, new Command() {
                
                        @Override
                        public void command() {
                            isArmUp = true;
                        
                        }
                    });
                
                    
            }
                
            }else {
                mElevator.setBallIntakingPos();
                mArm.setPower(0);
                currentState = robotStates.BallPivot;
                firstPhaseDone = false;
                isArmUp = false;
                isElevatorUp = false;
            }
            }
        else if(currentState.equals(robotStates.BallPivot) && (wantedState.equals(robotStates.HatchPivot))){
            if(!firstPhaseDone){
                mElevator.setElevatorPIDafterCommand(elevatorDefaulHeight,new Command(){
            
                    @Override
                    public void command() {

                        if(!isArmUp){
                            System.out.println("hi");
                        setArmAngleToStateFollowUp(robotStates.Escape, new Command() {
                            
                                @Override
                                public void command() {
                                    isArmUp = true;
                                }
                            });
                        }else{
                            if(!mPivot.isPivotFold()){
                                mPivot.setPivotPower(0.75);
                            }else{
                                mPivot.setPivotPower(0);
                                mArm.setArmAngleFollowUp(robotStates.HatchPivot.armAngle, new Command() {
                                
                                    @Override
                                    public void command() {
                                        firstPhaseDone = true;
                                    }
                                });
                            }
                        }
                     }
                });
            }else {
                setArmAngleToState(robotStates.HatchPivot);
                currentState = robotStates.HatchPivot;
                firstPhaseDone = false;
                isArmUp = false;
            }
            }else{
                mElevator.setBallIntakingPos();
            }
           

    }
    public void stateTransistor(robotStates wantedState){

        if(currentState.isScoring && wantedState.isScoring){
          scoreToscoreHandler(wantedState);
            System.out.println("sctosc");
        }else if(!currentState.isScoring && wantedState.isScoring){
            System.out.println("ınttoscore");
            IntakeToScoreHandler(wantedState);
        }else if(currentState.isScoring && !wantedState.isScoring){
            System.out.println("sctoınt");
            scoreToIntakeHandler(wantedState);
        }else if(currentState.equals(robotStates.Starting)){
            if(wantedState.equals(robotStates.HatchPivot)){
                startToIntakeHandler(wantedState);
            }else{
                System.out.println("you cant do that plz go first to hatch pivot");
            }
            
         } else{
            System.out.println("ınttoın");
            IntakeToIntakeHandler(wantedState);
        }
    }
    /**
     * 
     * @param height
     * @param angle
     * method that regulates all ordering actions by checking booleans that
     * returns whether there is illegal input or not
     * 
     * Input
     * Height To ascend or descend elevator
     * Output
     * Perform Task safely
     * 
     *  WARNING
     * It only works when ball intake angle is 0 if if otherwise is case it wont work
     * @see setStructure();
     */
    

     

    public boolean isArmSafeToUp(){
        if(mArm.getArmAngle() < 30 || mElevator.getPosition() < 75){
            return false;
        }else{
            return true;
        }
 }

    public boolean isArmSafeToDown(){
        if(mArm.getArmAngle() > 90 && mElevator.getPosition() < 300){
            return false;
        }

        return true;
    }
    public boolean PivotCollisionFree(){
        return mPivot.getPivotAngle()> 90 || mArm.getArmAngle() > 90 || mElevator.getPosition() > 75;
    } 

    public boolean elevatorSlideFree(){
        return armAngle > 25;
    } 
    
    public boolean isEqualPosition(double val1,double val2){
        return (val1+5) > val2 && val2 > (val1-5);
    }

    public void changeToPivotCollisionFree(Command command){
        mElevator.setElevatorPID(75);
    }
    public boolean illegalArmUpCommand(){
        return mElevator.getPosition() < 75 && armAngle < 30;
    }

    public boolean illegalArmDownCommand(){
        return mElevator.getPosition() < 75 && armAngle > 90 ;
    }

    public double getCurrentStatesArmAngle(){
        return currentState.armAngle;
    }

    public double getCurrentStatesElevatorHeight(){
        return currentState.elevatorHeight;
    }

    public void setArmAngleToState(robotStates stateName){
        switch(stateName){
            case HatchMid: mArm.executeTrajectory(hatchMid,stateName.armAngle);
             break;
            case HatchHigh: mArm.executeTrajectory(hatchHigh,stateName.armAngle);
             break;
            case HatchHighRear: mArm.executeTrajectory(hatchHighRear,stateName.armAngle);
             break;
            case HatchMidRear: mArm.executeTrajectory(hatchMidRear,stateName.armAngle);
             break;
            case BallLow: mArm.executeTrajectory(ballLow,stateName.armAngle);
             break;
            case BallMid: mArm.executeTrajectory(ballMid,stateName.armAngle);
             break;
            case BallHigh: mArm.executeTrajectory(ballHigh,stateName.armAngle);
             break;
            case BallHighRear: mArm.executeTrajectory(ballHighRear,stateName.armAngle);
             break;
            case BallCargo: mArm.executeTrajectory(ballCargo, stateName.armAngle);
             break;
            case BallPivot: mArm.executeTrajectory(ballPivot, stateName.armAngle);
             break;
            case BallMidRear: mArm.executeTrajectory(ballMidRear, stateName.armAngle);
             break;
            case HatchLow: mArm.executeTrajectory(hatchLow, stateName.armAngle);
             break;
            case armDown: mArm.executeTrajectory(armDown, -stateName.armAngle);
             break;
            case Escape: mArm.executeTrajectory(escape, stateName.armAngle);
            break;
            case HatchPivot:mArm.executeTrajectory(hatchPivot, stateName.armAngle);
             break;
            
            
        }
    }


    public void setArmAngleToStateFollowUp(robotStates stateName,Command comm){
        switch(stateName){
            case HatchMid: mArm.executeTrajectoryWithFollowUp(hatchMid,stateName.armAngle,comm);
            break;
            case HatchHigh: mArm.executeTrajectoryWithFollowUp(hatchHigh,stateName.armAngle,comm);
            break;
            case HatchHighRear: mArm.executeTrajectoryWithFollowUp(hatchHighRear,217,comm);
             break;
            case HatchMidRear: mArm.executeTrajectoryWithFollowUp(hatchMidRear,stateName.armAngle,comm);
             break;
            case BallLow: mArm.executeTrajectoryWithFollowUp(ballLow,stateName.armAngle,comm);
            break;
            case BallMid: mArm.executeTrajectoryWithFollowUp(ballMid,stateName.armAngle,comm);
            break;
            case BallHigh: mArm.executeTrajectoryWithFollowUp(ballHigh,stateName.armAngle,comm);
            break;
            case BallHighRear: mArm.executeTrajectoryWithFollowUp(ballHighRear,stateName.armAngle,comm);
            break;
            case BallCargo: mArm.executeTrajectoryWithFollowUp(ballCargo, stateName.armAngle,comm);
             break;
            case BallPivot: mArm.executeTrajectoryWithFollowUp(ballPivot, stateName.armAngle,comm);
             break;
            case BallMidRear: mArm.executeTrajectoryWithFollowUp(ballMidRear, stateName.armAngle,comm);
             break;
            case HatchLow: mArm.executeTrajectoryWithFollowUp(hatchLow, stateName.armAngle,comm);
             break;
            case armDown: mArm.executeTrajectoryWithFollowUp(armDown, stateName.armAngle,comm);
             break;
            case Escape : mArm.executeTrajectoryWithFollowUp(escape, stateName.armAngle,comm);
             break;
            case HatchPivot:mArm.executeTrajectoryWithFollowUp(hatchPivot, stateName.armAngle,comm);
             break;
            
        }
    }

    public void createAllTrajectories(){
        mArm.createArmTrajectory(robotStates.HatchHighRear.armAngle, 0.75, hatchHighRear);
        mArm.createArmTrajectory(robotStates.HatchHigh.armAngle, 0.75, hatchHigh);
        mArm.createArmTrajectory(robotStates.Escape.armAngle, 0.75, escape);
        mArm.createArmTrajectory(robotStates.HatchPivot.armAngle, 0.75, hatchPivot);
        mArm.createArmTrajectory(robotStates.HatchMid.armAngle, 0.75, hatchMid);
        mArm.createArmTrajectory(robotStates.BallLow.armAngle, 0.75, ballLow);
        mArm.createArmTrajectory(robotStates.BallCargo.armAngle, 0.75, ballCargo);
        mArm.createArmTrajectory(robotStates.BallPivot.armAngle, 0.75, ballPivot);
        mArm.createArmTrajectory(robotStates.BallMid.armAngle, 0.75, ballMid);
        mArm.createArmTrajectory(robotStates.BallHigh.armAngle, 0.75, ballHigh);
        
        mArm.createArmTrajectory(robotStates.HatchLow.armAngle, 0.75, hatchLow);
        }


}