
package frc.Auto.Actions;

import frc.states.Command;
import frc.subSystems.Arm;
import frc.subSystems.Elevator;

public class StallCommand implements Command{


    Arm mArm;
    Elevator mElevator;
    

    public StallCommand(){
        mArm = Arm.getInstance();
        mElevator = Elevator.getInstance();
    }
    @Override
    public void command() {
mArm.sstallArm();
mElevator.lockElevator();
    }

}