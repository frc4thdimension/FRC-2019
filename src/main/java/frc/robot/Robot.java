/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Collection;
import java.util.Collections;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SolenoidBase;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Auto.AutoFollower;
import frc.Auto.AutoMain.AutoMain;
import frc.Auto.AutoMain.AutoModeExecutor;
import frc.Auto.AutoModes.mainAuto;
import frc.Auto.AutoModes.mainAutoLeft;
import frc.Auto.AutoModes.simpleAuto;
import frc.Auto.SwitchAutos.Auto2Hatch;
import frc.Auto.SwitchAutos.mainSwitchAuto;
import frc.Auto.SwitchAutos.simpleSwitchAuto;
import frc.states.Structure;
import frc.states.Structure.robotStates;
import frc.subSystems.Arduino;
import frc.subSystems.Arm;
import frc.subSystems.Drive;
import frc.subSystems.Gamepad;
import frc.subSystems.Pivot;
import frc.subSystems.Teleop;
import frc.subSystems.TeleopRemastered;


public class Robot extends IterativeRobot {
  Arduino mArduino;
  TeleopRemastered newTeleop;
  
  Teleop mTeleop;
  Pivot mPivot;
  Gamepad mGamepad;
  boolean isTeleop;
  Drive mDrive;
  Arm mArm;
  Structure mStructure;
  Auto2Hatch a7x;
  AutoModeExecutor ame;
  boolean targetingFinished;
  double pixyValue;
  double resPower;
  double forPow;

  SendableChooser autoChooser;

  //mainAuto mMainAuto;
  mainAutoLeft  mMainAutoLeft;
  simpleAuto mSimpleAuto;
  mainSwitchAuto mMainSwitchAuto;
  simpleSwitchAuto mSimpleSwitchAuto;
  Solenoid light;
  double visionValue;
  AutoModeExecutor AME;
  boolean isSwitch = false;
  mainAuto mmms;
  Thread m_visionThread;
  AutoFollower mautotr;
  Compressor mCom = new Compressor();
  
  public static NetworkTable visionData = NetworkTable.getTable("Vision");
  
 Thread createTrajectories = new Thread(new Runnable(){
 
   @Override
   public void run() {
     mStructure.createAllTrajectories();
   }
 });




  @Override
  public void robotInit() {
    ame = new AutoModeExecutor();
    newTeleop = TeleopRemastered.getInstance();
    mArduino = Arduino.getInstance();
    mPivot = Pivot.getInstance();
    mmms = new mainAuto();
    mautotr = AutoFollower.getInstance();
    //NetworkTableInstance.getDefault().setUpdateRate(0.01);
    mStructure = Structure.getInstance();
  
    a7x = new Auto2Hatch();
    light = new Solenoid(3);
    mGamepad = Gamepad.getInstance();
    mArm = Arm.getInstance();
    mCom.setClosedLoopControl(true);
    
/*
     m_visionThread = new Thread(() -> {

      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();

      camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 15);

      CvSink cvSink = CameraServer.getInstance().getVideo();

      CvSource outputStream = CameraServer.getInstance().putVideo("ArmCam", 320, 240);

      Mat mat = new Mat();

      while (!Thread.interrupted()) {

        if (cvSink.grabFrame(mat) == 0) {

          outputStream.notifyError(cvSink.getError());

          continue;
        }

        if(mArm.getArmAngle()>140){
          Core.rotate(mat, mat, Core.ROTATE_180);
        }

        outputStream.putFrame(mat);
      }

    });
    m_visionThread.setDaemon(true);
    m_visionThread.start(); 

  */
  //CameraServer.getInstance().startAutomaticCapture();
    mDrive = Drive.getInstance();

    mTeleop = Teleop.getInstance();
    autoChooser = new SendableChooser();
    //CameraServer.getInstance().startAutomaticCapture();
    //mMainAuto = new mainAuto();
    mMainAutoLeft = new mainAutoLeft();
    mSimpleAuto = new simpleAuto();
    mMainSwitchAuto = new mainSwitchAuto();
    mSimpleSwitchAuto = new simpleSwitchAuto();
    AME = new AutoModeExecutor();


    autoChooser.addDefault("Main Auto Left:", mMainAutoLeft);
    autoChooser.addDefault("Main Auto:", mmms);
    //autoChooser.addDefault("Simple Auto SC:", mMainSwitchAuto);
    //autoChooser.addDefault("Main Auto SC:", mSimpleSwitchAuto);
    SmartDashboard.putData("Au:", autoChooser);
  
    }

 
  @Override
  public void robotPeriodic() {

  }

 
  @Override
  public void autonomousInit() {

    mDrive.resetEncoder();
    mDrive.resetGyro();
    ame.start();
    /*
    if(!isSwitch){
      AME.start();
    }
    */
   // AME.start();
  }

  
  @Override
  public void autonomousPeriodic() {
    SmartDashboard.putNumber("gyro", mDrive.getAngle());

    mDrive.getDeltaX();
    mDrive.getDeltaY();
    mDrive.driveOutputDashboard();
    if(mTeleop.isAutoActive){
      a7x.executeAuto(); 
    }
    else{
      mTeleop.start();
    }
    try {
      
    }
    catch (Throwable t) {
      throw t;
    }
    mMainSwitchAuto.switchMainAutoExecute();
    mSimpleSwitchAuto.switchEasyAutoExecute();
    if(!isSwitch){
      if(!mGamepad.getButtonA()&&!isTeleop){
        try {
        
        } catch (Throwable t) {
          throw t;
        }
      }else{
        isTeleop=true;
        AME.stop();
        mTeleop.start();
      }
    }
    else{
      if(autoChooser.getSelected()==mMainSwitchAuto){
        mMainSwitchAuto.switchMainAutoExecute();
      }else if(autoChooser.getSelected()==mSimpleSwitchAuto){
        mSimpleSwitchAuto.switchEasyAutoExecute();
      }
    }
  
  }

  
  @Override
  public void teleopPeriodic() {
    if(ame != null){
      ame.stop();
    }
    
    newTeleop.teleopStart();
  }

  
  @Override
  public void testPeriodic() {
   // mTest.start();
    

  }
  
  @Override
  public void disabledPeriodic() {
    //mArduino.getPixy();
    
   
    //mArm.createArmTrajectory(200, 0.75, mArm.feedForwardVoltage200);
    try {
      createTrajectories.start();
      ame.setAutoMode(mmms);
    }
    catch (Exception e) {
    }
    SmartDashboard.putNumber("angle", mArm.getArmAngle());
  }
  
  @Override
  public void disabledInit() {
   /*
    if(!isSwitch){
      if(AME!=null){
	  		AME.stop();
      }
    }
    */

    if(AME!=null){
      AME.stop();
    }
    

  }
  
  @Override
  public void teleopInit() {
    mArm.getArmAngle();
    mArm.getArmCosinus();
    /*
    if(!isSwitch){
      if(AME!=null){
			  AME.stop();
	  	}
    }*/
    try {
      if(ame!=null){
        ame.stop();
      }
       
    } catch (Exception e) {
      //TODO: handle exception
    }

    
  }
  
}
