package frc.Auto.AutoModes;

import java.util.Arrays;

import frc.Auto.Actions.Action;
import frc.Auto.Actions.Delay;
import frc.Auto.Actions.PickHatch;
import frc.Auto.Actions.StallCommand;
import frc.Auto.Actions.Stop;
import frc.Auto.Actions.actionTurn;
import frc.Auto.Actions.certainDistance;
import frc.Auto.Actions.getXY;
import frc.Auto.Actions.goDistanceStraight;
import frc.Auto.Actions.goToDistance;
import frc.Auto.Actions.hatch;
import frc.Auto.Actions.hatchIntake;
import frc.Auto.Actions.hatchTake;
import frc.Auto.Actions.parallelActions;
import frc.Auto.Actions.resetEncoder;
import frc.Auto.Actions.resetGyro;
import frc.Auto.Actions.seriesAction;
import frc.Auto.Actions.slowDistance;
import frc.Auto.Actions.startHandler;
import frc.Auto.Actions.stateHandler;
import frc.Auto.Actions.turnFunction;
import frc.Auto.Actions.vision;
import frc.Auto.AutoMain.AutoMain;
import frc.Auto.AutoMain.AutoModeEndedException;
import frc.states.Command;
import frc.states.Structure.robotStates;
import frc.subSystems.Drive;


public class mainAuto extends AutoMain {

 Drive mDrive = Drive.getInstance();
 StallCommand staller = new StallCommand();

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Main autoooooooo");
        executeOrder66(new seriesAction(Arrays.asList(
            
            
                new resetEncoder(),
                new resetGyro(),
                //new hatchIntake(1, true, 0),
                new hatchTake(),
                
               new goToDistance(75, 0, 0.7,staller),
                 new startHandler(),
                 new stateHandler(robotStates.HatchMid),
                new actionTurn(0, 53,staller),
                new resetEncoder(),
                new resetGyro(),
                new goToDistance(200, 0, 0.7,staller),
                
                
                //new vision(0,staller),
                new slowDistance(1.1,staller),
                //new hatch(),
                
                
                //new hatchIntake(0, true, 0),
                //new hatchIntake(1, false, 0),
                new Stop()
            
        )));

            
        

	}

    

}