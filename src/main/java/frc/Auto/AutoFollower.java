package frc.Auto;

import java.util.ArrayList;

import frc.subSystems.DataCollector;
import frc.subSystems.Drive;
import frc.subSystems.Teleop;

public class AutoFollower extends AutoConstants {

    static AutoFollower mInstance = new AutoFollower();

    /** 
     * return the ınstance of the class object
    */
    public static AutoFollower getInstance(){
    return mInstance;
    }

    Teleop mTeleop;

    private ArrayList<DataCollector> preCreatedList;
    double outputLeftMotor;
    double outputRightMotor;
    public double onlyLeft,onlyRight;
    private int index = 0;
    Drive mDrive;
    public AutoFollower(){
        mDrive = Drive.getInstance();
        mTeleop = Teleop.getInstance();
        preCreatedList = mTeleop.allDatasCollected;
    }

    /**
     * 
     * @param recordedValue
     * @param dynamicInput
     * @return output depending on the error amount
     */
    public double distancePID(double recordedValue,double dynamicInput){
        encoderError = recordedValue-dynamicInput;
        encoderDerivative = encoderError-preEncoderError;
        preEncoderError = encoderError;
        return encoderError*kPdistance + encoderDerivative*kDdistance; 
    }

    public double gyroPID(double recordedValue,double dynamicInput){
        gyroError = recordedValue-dynamicInput;
        gyroDerivative = gyroError-preEncoderError;
        preGyroError = gyroError;
        return gyroError*kPdistance + gyroDerivative*kDdistance;
    }

    public void pathPIDcontrol(){
        DataCollector momentaryData = preCreatedList.get(index);
        index++;
        /* onlyLeft = distancePID(momentaryData.getLeftEncoderData(),mDrive.getDistanceLeft());
        onlyRight = distancePID(momentaryData.getRightEncoderData(),mDrive.getDistance());
        outputLeftMotor = onlyLeft;
        outputRightMotor = onlyRight;
        mDrive.setSeperately(outputRightMotor, outputLeftMotor); */
        /* if(gyroPID(momentaryData.getGyroData(),mDrive.getAngle()) > 0 ){
            outputLeftMotor = onlyLeft;
              
        }else{

        } */
        
        mDrive.robotDrive.arcadeDrive(momentaryData.getAxisLeft(), momentaryData.getAxisRight());

    }




    
}