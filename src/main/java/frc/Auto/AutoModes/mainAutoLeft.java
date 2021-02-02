package frc.Auto.AutoModes;

import java.util.Arrays;

import frc.Auto.Actions.StallCommand;
import frc.Auto.Actions.Stop;
import frc.Auto.Actions.actionTurn;
import frc.Auto.Actions.goToDistance;
import frc.Auto.Actions.hatchIntake;
import frc.Auto.Actions.parallelActions;
import frc.Auto.Actions.resetEncoder;
import frc.Auto.Actions.resetGyro;
import frc.Auto.Actions.seriesAction;
import frc.Auto.Actions.slowDistance;
import frc.Auto.Actions.startHandler;
import frc.Auto.Actions.vision;
import frc.Auto.AutoMain.AutoMain;
import frc.Auto.AutoMain.AutoModeEndedException;
import frc.subSystems.Drive;

public class mainAutoLeft extends AutoMain {

    Drive mDrive = Drive.getInstance();
    StallCommand staller = new StallCommand();   

    @Override
    protected void routine() throws AutoModeEndedException {
        executeOrder66(new seriesAction(Arrays.asList(
            
            
                   
            new hatchIntake(1, true, 0),
            new parallelActions(Arrays.asList(
                new startHandler(),
                new hatchIntake(1, false, 0.5)
            )),
                

                new goToDistance(100, 0, 0.7,staller),
                 
                

                new actionTurn(0, -50,staller),
                new resetEncoder(),
                new resetGyro(),
                new goToDistance(210, 0, 0.7,staller),
                //new vision(0,staller),
                new slowDistance(1,staller),
                new hatchIntake(0, true, 0),
                new hatchIntake(1, false, 0),
                
                
           
              
             
       
            
            
                
                new Stop()
            
        )));
                

      
	}

} 