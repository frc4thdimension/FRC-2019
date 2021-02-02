package frc.subSystems;

import java.awt.List;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Arduino {
    
    AnalogInput ultrasonic;
    I2C arduinoI2C;
    short pixyVal;
    short ultraSonicVal;
    byte[] buffer = new byte[2];
    Solenoid visionLed;


    
    static Arduino mInstance= new Arduino();

    public static Arduino getInstance(){

        return mInstance;

    }

    public Arduino(){
      
      ultrasonic = new AnalogInput(1);
        arduinoI2C = new I2C(I2C.Port.kOnboard,4);
    }


    public double getPixy(){
        
        arduinoI2C.write(4,2);
        arduinoI2C.read(4,2, buffer);
    
        pixyVal = ByteBuffer.wrap(buffer).getShort();
    
        System.out.println(pixyVal/100);
      
        return pixyVal/100;
        
    
    }


}