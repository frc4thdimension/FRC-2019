 package frc.subSystems;

import edu.wpi.first.wpilibj.Joystick;

public class ControlPanel{

     Joystick GamePad1 = new Joystick(2);

    static ControlPanel mInstance = new ControlPanel();

    public static ControlPanel getInstance(){
        return mInstance;
    }


    public boolean getButton1(){
        return GamePad1.getRawButton(1);
    }
    public boolean getButton2(){
        return GamePad1.getRawButton(2);
    }
    public boolean getButton3(){
        return GamePad1.getRawButton(3);
    }
  
    public boolean getButton4(){
        return GamePad1.getRawButton(4);
    }
    public boolean getButton5(){
        return GamePad1.getRawButton(5);
    }
    public boolean getButton6(){
        return GamePad1.getRawButton(6);
    }
    public boolean getButton7(){
        return GamePad1.getRawButton(7);
    }
    public boolean getButton8(){
        return GamePad1.getRawButton(8);
    }
    public boolean getButton9(){
        return GamePad1.getRawButton(9);
    }
    

 
}