package frc.Auto.Actions;

import frc.states.Structure;
import frc.states.Structure.robotStates;

public class startHandler implements Action{

    Structure mStructure;

    public startHandler(){
        mStructure = Structure.getInstance();
    }
    @Override
    public void start() {

    }

    @Override
    public void update() {
        mStructure.startToIntakeHandler(robotStates.HatchPivot);
    }

    @Override
    public void done() {
        mStructure.setArmAngleToState(robotStates.HatchPivot);
    }

    @Override
    public boolean isFinished() {
        return mStructure.currentState.equals(robotStates.HatchPivot);
    }

}