package frc.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.states.Command;


public class Delay implements Action{
    double timestarted;
    double waittime;
    Command comm;

    public Delay(double wait,Command command){
        waittime = wait;
        comm = command;
    }
    @Override
    public void start() {
        timestarted = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
    comm.command();
    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp()-timestarted > waittime;
    }

}