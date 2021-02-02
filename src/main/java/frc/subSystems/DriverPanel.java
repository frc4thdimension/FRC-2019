package frc.subSystems;

import edu.wpi.first.wpilibj.Joystick;

public class DriverPanel {

    static DriverPanel mInstance = new DriverPanel();

    public static DriverPanel getInstance(){
        return mInstance;
    }

    Joystick driverPanel1;
    Joystick driverPanel2;

    public DriverPanel(){
        driverPanel1 = new Joystick(1);
        driverPanel2 = new Joystick(2);
    }
    
    public boolean getCargoLevel(){
        return driverPanel2.getRawButton(9);
    }

    public boolean getBallLow(){
        return driverPanel2.getRawButton(10);
    }

    public boolean getBallMid(){
        return driverPanel2.getRawButton(11);
    }

    public boolean getBallHigh(){
        return driverPanel2.getRawButton(12);
    }

    public boolean getHatchLow(){
        return driverPanel2.getRawButton(6);
    }

    public boolean getHatchMid(){
        return driverPanel2.getRawButton(7);
    }

    public boolean getHatchHigh(){
        return driverPanel2.getRawButton(8);
    }

    public boolean getIfReverse(){
        return driverPanel2.getRawButton(5);
    }

    public boolean cargoIn(){
        return driverPanel1.getRawButton(6);
    }

    public boolean cargoOut(){
        return driverPanel1.getRawButton(10);
    }

    public boolean HatchIn(){
        return driverPanel1.getRawButton(5);
    }

    public boolean HatchOut(){
        return driverPanel1.getRawButton(9);
    }

    public boolean slowArmUp(){
        return driverPanel1.getRawButton(2);
    }

    public boolean slowArmDown(){
        return driverPanel1.getRawButton(1);
    }

    public boolean slowElvUp(){
        return driverPanel1.getRawButton(3);
    }

    public boolean slowElvDown(){
        return driverPanel1.getRawButton(4);
    }

    public boolean slowPivotDown(){
        return driverPanel1.getRawButton(8);
    }
    public boolean slowPivotUp(){
        return driverPanel1.getRawButton(7);
    }

}