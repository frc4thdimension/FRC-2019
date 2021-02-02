package frc.Auto.Actions;

import frc.states.Structure;
import frc.states.Structure.robotStates;
import frc.subSystems.Arm;
import frc.subSystems.Elevator;

public class stateHandler implements Action {

    Structure mStructure;
    robotStates states;

    public stateHandler(robotStates state){
        states = state;
        mStructure = Structure.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        mStructure.stateTransistor(states);
    }

    @Override
    public void done() {
        

    }

    @Override
    public boolean isFinished() {
        return mStructure.currentState.equals(states);
    }



}