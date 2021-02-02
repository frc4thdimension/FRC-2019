package frc.Auto.AutoModes;

import java.util.Arrays;

import frc.Auto.Actions.seriesAction;
import frc.Auto.AutoMain.AutoMain;
import frc.Auto.AutoMain.AutoModeEndedException;

public class simpleAuto extends AutoMain {

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Simple autooooooo");
        executeOrder66(
            new seriesAction(
                Arrays.asList(


            ))
        );

	}

}