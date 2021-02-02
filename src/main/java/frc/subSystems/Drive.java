package frc.subSystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.Math.pid;
import frc.states.Command;

public class Drive {

    /*
    Initialization
    */
    
     /**
    *
    */

    private static final String SMART_DASHBOARD = "SmartDashboard";
    double tL, tR;
    double avs;
    double turnFstart;
    int counterT;
    double maxOutput, maxOutputR;
    public VictorSP driveMotorRight;
    public VictorSP driveMotorLeft;
    public Encoder driveEncoderRight;
    private Encoder driveEncoderLeft;
    public ADXRS450_Gyro gyro;
    int tieFighter = 0;
    double accEncoder;
    double angleTol;
    public double totalX;
    double totalY;
    double deltaX;
    double deltaY;
    final double turnCenter = 35;
    final double robotWidth = 33;
    double rightEnc;
    double leftEnc;
    double realRot;
    double angle;
    public boolean bRots = false;
    public boolean bEncRots = false;
    public boolean bTurnFunc = false;
    public boolean isTurnFinished = false;
    int count = 0;
    int count2 = 0;
    int switchc = 0;
    double timed;
    double res;
    double prevTime;
    double seperatistLeftPower;
    double seperatistRightPower;
    double seperatistLeftPowerAcc;
    double seperatistRightPowerAcc;
    double accValue;
    double brakeDistance;
    double brakeLeft, brakeRight;
    double PidResultRight = 0;
    double PidResultLeft = 0;
    double imageSpeedLeft, imageSpeedRight;
    double rampDownValue;
    double xValue;
    double fTime;
    double turnTime;
    double targetX;
    double targetY;
    public pid PID;
    public DifferentialDrive robotDrive;
    PowerDistributionPanel pdp;
    public Solenoid shifter;

    int aa;

    static Drive mInstance = new Drive();

    public static Drive getInstance() {
        return mInstance;
    }

    public Drive() {
        /*
         * Initialization
         */
        PID = pid.getInstance();
        pdp = new PowerDistributionPanel();
        counterT = 0;
        shifter = new Solenoid(5);
        shifter.setPulseDuration(1);
        gyro = new ADXRS450_Gyro();
        driveMotorLeft = new VictorSP(1);
        driveMotorRight = new VictorSP(0);
        driveEncoderRight = new Encoder(12, 13, false);
        driveEncoderLeft = new Encoder(10, 11, true);

        driveMotorLeft.setInverted(true);
        driveMotorRight.setInverted(true);
        driveEncoderRight.setDistancePerPulse(Math.PI * 2 * 5.08 / 500);
        driveEncoderLeft.setDistancePerPulse(Math.PI * 2 * 5.08 / 500);

        seperatistLeftPowerAcc = 0.01;
        seperatistRightPowerAcc = 0.01;
        robotDrive = new DifferentialDrive(driveMotorLeft, driveMotorRight);

    }

    /*
     * Prints needed values to dashboard
     */

    public void driveOutputDashboard() {
        // SmartDashboard.putNumber("PID",PID.rampDownRes);
        // SmartDashboard.putNumber("valuedeacc",rampDownValue);
        SmartDashboard.putNumber("EncoderLeft: ", getDistanceLeft());
        // SmartDashboard.putNumber("cosinus", Math.cos(Math.toRadians(getAngle())));
        // SmartDashboard.putNumber("angleTol", angleTol);
        SmartDashboard.putNumber("deltay", deltaY);
        SmartDashboard.putNumber("totaly", totalY);
        SmartDashboard.putNumber("deltax", deltaX);
        SmartDashboard.putNumber("totalx", totalX);
        SmartDashboard.putNumber("totalxNew", getTotalX());
        SmartDashboard.putNumber("Gyro: ", getAngle());
        SmartDashboard.putNumber("EncoderRight: ", getDistance());
        // SmartDashboard.putNumber("AccSpeedLeft", seperatistLeftPowerAcc);
        // SmartDashboard.putNumber("AccSpeedRight", seperatistRightPowerAcc);
        // SmartDashboard.putNumber("MaxPowerRight", seperatistRightPower);
        // SmartDashboard.putNumber("MaxPowerLeft", seperatistLeftPower);
        // SmartDashboard.putNumber("AccValuePower", accValue);
        SmartDashboard.putNumber("speedRight", -driveEncoderRight.getRate());
        SmartDashboard.putNumber("speedLeft", -driveEncoderLeft.getRate());
        // SmartDashboard.putNumber("AccTimeToVmax", res);
        SmartDashboard.putNumber("AccEncoder", accEncoder);
        SmartDashboard.putBoolean("turn done:", isTurnFinished);

    }

    /**
     * Seperatist drive(right motor velocity, left motor velocity,accelaration
     * m/ms^2)
     * 
     * 1-Convert Velocity into power 2- If accelaration is 0 give direct
     * Speed:WARNING --- high jerk 3- Else in a loop being repeated in every 20ms
     * update motor power until reaches wanted value Addition;If you write 0.05 acc
     * to reach max speed 2.5 it will take 2.5 seconds to do so
     **/

    /**
     * real seperatist with acc
     * 
     * @param vRight
     * @param vLeft
     * @param acc
     * @param anglee
     */

    public void seperatistDrive(double vLeft, double vRight) {
        switch (aa) {
        case 0:

            timed = Timer.getFPGATimestamp();
            aa++;
            break;
        case 1:
            seperatistRightPower = -powerToVelocity(vRight, true);
            seperatistLeftPower = -powerToVelocity(vLeft, true);
            driveLimit(seperatistRightPower);
            driveLimit(seperatistLeftPower);
            aa++;

            break;

        case 2:
            driveMotorLeft.set(-seperatistLeftPower);
            driveMotorRight.set(seperatistRightPower);
            break;

        }
    }

    public void seperatistDrivePID(double vRight, double vLeft, double acc, double anglee) {
        switch (count) {
        case 0:

            timed = Timer.getFPGATimestamp();
            count++;
            break;
        case 1:
            seperatistRightPower = -powerToVelocity(vRight, true);
            seperatistLeftPower = -powerToVelocity(vLeft, true);
            driveLimit(seperatistRightPower);
            driveLimit(seperatistLeftPower);
            accValue = powerToVelocity(acc, true);

            if (acc == 0) {
                PID.gyroPID(anglee, getAngle());
                maxOutput = Math.max(Math.abs(seperatistLeftPower), Math.abs(PID.gyroPIDres));
                maxOutputR = Math.max(Math.abs(seperatistRightPower), Math.abs(PID.gyroPIDres));

                if (vRight < 0 && vLeft < 0) {
                    if (getAngle() <= anglee) {
                        System.out.println("below zero");
                        PidResultRight = Math.abs(PID.gyroPIDres);
                        PidResultLeft = 0;
                        driveMotorRight.set(maxOutputR);
                        driveMotorLeft.set(-maxOutputR - PID.gyroPIDres);

                    } else {
                        System.out.println("above zero");
                        PidResultLeft = Math.abs(PID.gyroPIDres);
                        PidResultRight = 0;
                        driveMotorLeft.set(-maxOutput);
                        driveMotorRight.set(maxOutput - PID.gyroPIDres);
                    }

                } else {
                    if (getAngle() <= anglee) {
                        System.out.println("below zero");
                        PidResultRight = Math.abs(PID.gyroPIDres);
                        PidResultLeft = 0;
                        driveMotorLeft.set(maxOutput);
                        driveMotorRight.set(-maxOutput - PID.gyroPIDres);
                    } else {
                        System.out.println("above zero");
                        PidResultLeft = Math.abs(PID.gyroPIDres);
                        PidResultRight = 0;
                        driveMotorRight.set(-maxOutputR);
                        driveMotorLeft.set(maxOutputR - PID.gyroPIDres);
                    }

                }
            } else {
                // acc greater than zero

                if (vRight >= 0 || vLeft >= 0) {
                    /*
                     * PID.gyroPID(anglee,getAngle()); if(PID.gyroPIDres < 0){ PidResultRight =
                     * PID.gyroPIDres; PidResultLeft = 0; driveMotorLeft.set(PID.rampingUpPI(0.5,
                     * getDistance())); driveMotorRight.set(-PID.rampingUpPI(0.5, getDistance())) ;
                     * }else { PidResultLeft = PID.gyroPIDres; PidResultRight = 0; }
                     */
                    if (getDistance() < 50 && PID.rampingUpPI(0.5, getDistance()) < Math.abs(seperatistLeftPower)) {

                        driveMotorLeft.set(PID.rampingUpPI(0.5, getDistance()));
                        driveMotorRight.set(-PID.rampingUpPI(0.5, getDistance()));
                        count2 = 0;

                    } else {

                        switch (count2) {
                        case 0:
                            PID.setRampZero();
                            res = Timer.getFPGATimestamp() - timed;
                            accEncoder = getDistance();
                            count2++;
                            break;

                        case 1:
                            PID.gyroPID(anglee, getAngle());
                            maxOutput = Math.max(Math.abs(seperatistLeftPower), Math.abs(PID.gyroPIDres));
                            maxOutputR = Math.max(Math.abs(seperatistRightPower), Math.abs(PID.gyroPIDres));

                            if (getAngle() <= 0) {
                                System.out.println("below zero");
                                PidResultRight = Math.abs(PID.gyroPIDres);
                                PidResultLeft = 0;
                                driveMotorLeft.set(maxOutput);
                                driveMotorRight.set(-maxOutput - PID.gyroPIDres);
                            } else {
                                System.out.println("above zero");
                                PidResultLeft = Math.abs(PID.gyroPIDres);
                                PidResultRight = 0;
                                driveMotorRight.set(-maxOutputR);
                                driveMotorLeft.set(maxOutputR - PID.gyroPIDres);
                            }

                            break;
                        }
                    }

                } else {
                    // negative part
                    if (getDistance() > -50 && PID.rampingUpPI(-0.5, getDistance()) < Math.abs(seperatistLeftPower)) {

                        driveMotorLeft.set(PID.rampingUpPI(-0.5, getDistance()));
                        driveMotorRight.set(-PID.rampingUpPI(-0.5, getDistance()));
                        count2 = 0;

                    } else {

                        switch (count2) {
                        case 0:
                            PID.setRampZero();
                            res = Timer.getFPGATimestamp() - timed;
                            accEncoder = getDistance();
                            count2++;
                            break;

                        case 1:
                            PID.gyroPID(anglee, getAngle());
                            maxOutput = Math.max(Math.abs(seperatistLeftPower), Math.abs(PID.gyroPIDres));
                            maxOutputR = Math.max(Math.abs(seperatistRightPower), Math.abs(PID.gyroPIDres));

                            if (getAngle() <= anglee) {
                                System.out.println("below zero");
                                driveMotorLeft.set(-maxOutput);
                                driveMotorRight.set(maxOutput - PID.gyroPIDres);
                            } else {
                                System.out.println("above zero");
                                driveMotorRight.set(maxOutputR);
                                driveMotorLeft.set(-maxOutputR - PID.gyroPIDres);
                            }

                            break;
                        }
                    }
                }

            }
            break;
        }
    }

    public void turnFunc2commanded(double wantedAngle, Command command) {

        if (getAngle() < wantedAngle + 2 && getAngle() > wantedAngle - 2) {
            System.out.println("turn finished!!!!" + getAngle());
            isTurnFinished = true;
            command.command();

        } else {
            isTurnFinished = false;
            PID.turnPID(wantedAngle, getAngle());

            robotDrive.arcadeDrive(0, -PID.turnPIDres);

        }

    }

    public void turnFunc2(double wantedAngle) {
        if (getAngle() < wantedAngle + 2 && getAngle() > wantedAngle - 2) {
            if (Timer.getFPGATimestamp() - inZone > 0.2) {
                b = true;
            } else {
                b = false;
            }
        } else {
            b = false;
            inZone = Timer.getFPGATimestamp();
        }

        if (getAngle() < wantedAngle + 2 && getAngle() > wantedAngle - 2) {
            System.out.println("turn finished!!!!" + getAngle());
            isTurnFinished = true;

        } else {
            isTurnFinished = false;
            PID.turnPID(wantedAngle, getAngle());

            robotDrive.arcadeDrive(0, -PID.turnPIDres);

        }
    }

    public boolean b;
    public double inZone;

    public void turnFunc2followUp(double wantedAngle, Command command) {
        if (getAngle() < wantedAngle + 3.5 && getAngle() > wantedAngle - 3.5) {
            if (Timer.getFPGATimestamp() - inZone > 0.1) {
                b = true;
            } else {
                b = false;
            }
        } else {
            b = false;
            inZone = Timer.getFPGATimestamp();
        }

        if (getAngle() < wantedAngle + 3 && getAngle() > wantedAngle - 3) {
            System.out.println("turn finished!!!!" + getAngle());
            if (b) {
                command.command();
            } else {

            }
            isTurnFinished = true;

        } else {
            isTurnFinished = false;
            PID.turnPID(wantedAngle, getAngle());

            robotDrive.arcadeDrive(0, -PID.turnPIDres);

        }
    }

    /**
     * 
     * @param maxSpeed
     * @param yxValue  Algorithm to create three main sectors(Trapezoidal) to travel
     *                 certain distance
     */
    public void ROTS(double maxSpeed, double yxValue) {

        if (maxSpeed > 0) {
            // Beta for x
            xValue = yxValue - 30;
            if (getTotalX() < xValue - 80) {
                System.out.println("Going!!!!");
                if (getTotalX() < 50) {
                    System.out.println("segment update!!!!");

                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(0), encGetter); voltageR =
                     * -PID.rampingUpPI(xSegments.get(0), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0.15, 0);

                } else {
                    System.out.println("Go straight wa!!!!");
                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(CheckCounter), encGetter); voltageR
                     * = -PID.rampingUpPI(xSegments.get(CheckCounter), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0, 0);
                }
            } else {
                System.out.println("RampDown!!!!");
                if (getTotalX() < xValue - 10) {

                    robotDrive.arcadeDrive(0, 0);
                    fTime = Timer.getFPGATimestamp();
                } else {
                    if (Timer.getFPGATimestamp() - fTime < 0.3 * xValue / 200) {
                        seperatistDrive(-0.34, -0.34);
                    } else {
                        bRots = true;
                    }

                }
            }

        } else {
            xValue = yxValue + 30;
            if (getTotalX() + 20 > xValue + 80) {
                System.out.println("Going!!!!");
                if (getTotalX() + 20 > -50) {
                    System.out.println("segment update!!!!");

                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(0), encGetter); voltageR =
                     * -PID.rampingUpPI(xSegments.get(0), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, -0.15, 0);

                } else {
                    System.out.println("Go straight wa!!!!");
                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(CheckCounter), encGetter); voltageR
                     * = -PID.rampingUpPI(xSegments.get(CheckCounter), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0, 0);
                }
            } else {
                System.out.println("RampDown!!!!");
                if (getTotalX() + 20 > xValue + 10) {

                    robotDrive.arcadeDrive(0, 0);
                    fTime = Timer.getFPGATimestamp();
                } else {
                    if (-1 * (Timer.getFPGATimestamp() - fTime) > 0.3 * xValue / 200) {
                        seperatistDrive(0.6, 0.6);
                    } else {
                        bRots = true;
                    }
                }

            }

        }

    }
    /*
     * if(totalX>xValue-2){
     * 
     * }
     */

    public void encROTS(double maxSpeed, double encValue) {
        if (maxSpeed > 0) {
            // Beta for x
            xValue = encValue - 30;
            if (getAvgDistance() < xValue - 80) {
                System.out.println("Going!!!!");
                if (getAvgDistance() < 50) {
                    System.out.println("segment update!!!!");

                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(0), encGetter); voltageR =
                     * -PID.rampingUpPI(xSegments.get(0), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0.15, 0);

                } else {
                    System.out.println("Go straight wa!!!!");
                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(CheckCounter), encGetter); voltageR
                     * = -PID.rampingUpPI(xSegments.get(CheckCounter), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0, 0);
                }
            } else {
                System.out.println("RampDown!!!!");
                if (getAvgDistance() < xValue - 10) {

                    robotDrive.arcadeDrive(0, 0);
                    fTime = Timer.getFPGATimestamp();
                } else {
                    if (Timer.getFPGATimestamp() - fTime < 0.3 * xValue / 200)
                        seperatistDrive(-0.34, -0.34);
                    bEncRots = true;

                }

            }
        } else {
            xValue = encValue + 30;
            if (getAvgDistance() > xValue + 80) {
                System.out.println("Going!!!!");
                if (getAvgDistance() < -50) {
                    System.out.println("segment update!!!!");

                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(0), encGetter); voltageR =
                     * -PID.rampingUpPI(xSegments.get(0), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0.15, 0);

                } else {
                    System.out.println("Go straight wa!!!!");
                    /*
                     * voltageL = PID.rampingUpPI(xSegments.get(CheckCounter), encGetter); voltageR
                     * = -PID.rampingUpPI(xSegments.get(CheckCounter), encGetter);
                     */
                    seperatistDrivePID(maxSpeed, maxSpeed, 0, 0);
                }
            } else {
                System.out.println("RampDown!!!!");
                if (getAvgDistance() > xValue + 10) {

                    robotDrive.arcadeDrive(0, 0);
                    fTime = Timer.getFPGATimestamp();
                } else {
                    if (Timer.getFPGATimestamp() - fTime < 0.3 * xValue / 200)
                        seperatistDrive(0.34, 0.34);
                    bEncRots = true;

                }

            }
        }
    }

    public void xyTurnPID(double destinationX, double destinationY, double angle) {
        this.targetX = destinationX;
        this.targetY = destinationY;

        if (destinationX > 0) {
            PID.xyGyroPID(angle, getAngle(), getTotalX(), destinationX, 0.4);
            /*
             * if(destinationX-3<=getTotalX()||angle-3<=getAngle()){
             * robotDrive.arcadeDrive(0, 0); }else{ robotDrive.curvatureDrive(0.5,
             * PID.xyRes, true); }
             */
            if (destinationX - 40 <= getTotalX()) {
                if (getTotalX() > destinationX - 10) {
                    turnFunc2(angle);
                } else {
                    robotDrive.arcadeDrive(0, 0);
                }

            } else {
                robotDrive.curvatureDrive(PID.xyRes, -PID.xaRes, false);
            }
        } else {
            PID.xyGyroPID(angle, getAngle(), getTotalX(), destinationX, -0.4);
            /*
             * if(destinationX-3<=getTotalX()||angle-3<=getAngle()){
             * robotDrive.arcadeDrive(0, 0); }else{ robotDrive.curvatureDrive(0.5,
             * PID.xyRes, true); }
             */
            if (destinationX + 40 >= getTotalX()) {
                if (getTotalX() < destinationX + 10) {
                    turnFunc2(angle);
                } else {
                    robotDrive.arcadeDrive(0, 0);
                }

            } else {
                robotDrive.curvatureDrive(PID.xyRes, -PID.xaRes, false);
            }
        }

    }
    /*
     * if(destinationX-3<getTotalX()&&getTotalX()<destinationX+3&&
     * angle-3>getAngle()&&getAngle()>angle+3){
     * 
     * 
     * }
     */

    public void pixyDrive(double pixyX) {
        if (pixyX < 98) {
            robotDrive.arcadeDrive(0, 1 / pixyX * 0.5);
        } else if (pixyX > 106) {
            robotDrive.arcadeDrive(0, -1 / pixyX * 0.5);
        } else {
            robotDrive.arcadeDrive(0, 0);
        }
    }

    public void shift(boolean shifted) {

        shifter.set(shifted);

    }

    /*
     * Power to m/s calculator(isReverse) if isReverse true then executes inverse
     * calculation 1- Turn power into voltage 2- Directly proportion it to 12V =
     * 3.9m/s 3- Output
     */
    public double powerToVelocity(double voltageOrVelocity, boolean isReverse) {
        if (isReverse) {
            return ((12 * voltageOrVelocity) / 3.9) / 12;
        } else {
            return (3.9 * (12 * voltageOrVelocity)) / 12;
        }
    }
    double realAppliedVoltage;
    double initialVoltage = 12.7;
    double outputPower;
    public double getVoltaged(double power){
        realAppliedVoltage = initialVoltage * power;
        outputPower = realAppliedVoltage / pdp.getVoltage();
        return outputPower;
    }
    

    /*
     * Get real distance traveled of mid of the robot
     */
    public double getDistance() {
        return -driveEncoderRight.getDistance();

    }

    public double getDistanceLeft() {
        return -driveEncoderLeft.getDistance();

    }

    public double getAvgDistance() {

        return (getDistance() + getDistanceLeft()) / 2;

    }

    public double getDistanceBackUp() {

        return -driveEncoderRight.get() * 0.06429 - 10;

    }

    public void Stop() {

        robotDrive.arcadeDrive(-0.05, 0);

    }

    /*
     * Get gyro value
     */
    public double getAngle() {
        return gyro.getAngle();
    }

    /*
     * public void resetGyro(){ gyro.reset(); }
     */
    public double getTotalX() {
        return totalX;
    }

    public double getTotalY() {
        return totalY;
    }

    public boolean isGyroReset() {
        return getAngle() > -0.2 && getAngle() < 0.2;
    }

    public boolean isEncoderReset() {
        return getAvgDistance() > -0.3 && getAvgDistance() < 0.3;
    }

    public boolean isXYreset() {
        return getTotalX() > -8 && getTotalX() > -12 && getTotalY() > -2 && getTotalY() < 2;
    }

    /*
     * Get accelerometer value and convert to m/s^2
     */

    /*
     * Get real distance travelled for left
     */
    // public double getLeftDist(){

    // }

    /*
     * Get real distance travelled for right
     */
    // public double getRightDist(){

    // }

    /*
     * Get the location and heading of the robot using encoder and gyro
     */

    public void driveLimit(double a) {
        if (a <= -1) {
            a = -1;
        } else if (a >= 1) {
            a = 1;

        } else {

        }
    }

    /**
     * Delta x,delta Y,Delta theta
     */

    public double getDeltaX() {
        deltaX = (-driveEncoderLeft.getRate() - driveEncoderRight.getRate()) / 2
                * Math.cos(Math.toRadians(getAngle() % 360)) * 0.02;
        totalX += deltaX;
        return deltaX;
    }

    public double getDeltaY() {
        angleTol = getAngle() >= -1 && getAngle() <= 1 ? 0 : getAngle();
        deltaY = (-driveEncoderRight.getRate() - driveEncoderLeft.getRate()) / 2
                * Math.sin(Math.toRadians(getAngle() % 360)) * 0.02;
        totalY += deltaY;
        return deltaY;
    }

    public void resetXY() {
        this.totalX = 0;
        this.totalY = 0;

    }

    public void resetGyro() {
        gyro.reset();

    }

    public void resetEncoder() {
        driveEncoderLeft.reset();
        driveEncoderRight.reset();
    }

    public double zeroLimitter(double i) {
        if (i < 0) {
            i = -0.1;

        } else {

        }
        return i;
    }

    double distanceError = 0;
    double prevError = 0;
    double distanceIntegral = 0;
    double distanceDerv = 0;
    double resDist = 0;
    double kP = 1;
    double kI = 0;
    double kD = 0;

    public double pidDistance(double maxSpeed, double wantedDistance) {
        distanceError = wantedDistance - getTotalX();
        distanceIntegral += distanceError * .02;
        distanceDerv = (distanceError - prevError) / .02;
        resDist = kP * distanceError + kI * distanceIntegral + kD * distanceDerv;
        if (wantedDistance > 0 && resDist > maxSpeed) {
            resDist = maxSpeed;
        } else if (wantedDistance < 0 && resDist < -maxSpeed) {
            resDist = -maxSpeed;
        } else {

        }
        return resDist;
    }

    public void goToDistanceDeepSpaceToHatch(double wantedX, double angle) {
        PID.gyroPID(0, getAngle());

        if (getDistance() < wantedX - 120) {
            robotDrive.curvatureDrive(0.6, PID.gyroPIDres, false);
        } else {
            robotDrive.curvatureDrive(0.1, PID.gyroPIDres, false);
        }

    }

    public void goToDistanceToHatch(double wantedx, double angle, Command command) {
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {

            if (Timer.getFPGATimestamp() - inTheZone > 0.2) {
                c = true;
            } else {

            }
        } else {

            c = false;
            inTheZone = Timer.getFPGATimestamp();
        }
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {
            robotDrive.arcadeDrive(-0.35, 0);
            if (c) {
                command.command();
            } else {

            }

        } else if (getDistance() > wantedx + 5) {
            robotDrive.arcadeDrive(-0.5, 0);
        } else {
            goToDistanceDeepSpaceToHatch(wantedx, angle);
        }

    }

    boolean c;
    double inTheZone;

    public void goToDistance(double wantedx, double angle, Command command) {
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {

            if (Timer.getFPGATimestamp() - inTheZone > 0.2) {
                c = true;
            } else {

            }
        } else {

            c = false;
            inTheZone = Timer.getFPGATimestamp();
        }
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {
            robotDrive.arcadeDrive(-0.25, 0);
            if (c) {
                command.command();
            } else {

            }

        } else if (getDistance() > wantedx + 5) {
            robotDrive.arcadeDrive(-0.5, 0);
        } else {
            goToDistanceDeepSpace(wantedx, angle);
        }

    }

    public void goToDistanceDeepSpace(double wantedX, double angle) {
        PID.gyroPID(0, getAngle());

        if (getDistance() < wantedX - 120) {
            robotDrive.curvatureDrive(0.65, PID.gyroPIDres, false);
        } else {
            robotDrive.curvatureDrive(0.25, PID.gyroPIDres, false);
        }

    }

    public void goToDistanceBack(double wantedx, double angle) {
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {
            robotDrive.arcadeDrive(0.25, 0);

        } else if (getDistance() < wantedx - 5) {
            robotDrive.arcadeDrive(0.5, 0);
        } else {
            goToDistanceDeepSpaceBack(wantedx, angle);
        }

    }

    public void goToDistanceDeepSpaceBack(double wantedX, double angle) {
        PID.gyroPID(0, getAngle());

        if (getDistance() > wantedX + 120) {
            robotDrive.curvatureDrive(-0.65, PID.gyroPIDres, false);
        } else {
            robotDrive.curvatureDrive(-0.17, PID.gyroPIDres, false);
        }

    }

    public double timeIn;
    public boolean a;

    public void goToDistanceBack(double wantedx, double angle, Command command) {
        if (getDistance() > wantedx + 5) {

            timeIn = Timer.getFPGATimestamp();
        } else {
            if (Timer.getFPGATimestamp() - timeIn > 0.35) {
                a = true;
            } else {
                a = false;
            }
        }
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {
            robotDrive.arcadeDrive(0.25, 0);
            if (a) {
                command.command();
            }

        } else if (getDistance() < wantedx - 5) {
            robotDrive.arcadeDrive(0.5, 0);
        } else {
            goToDistanceDeepSpaceBack(wantedx, angle);
        }

    }

    public void goToDistanceToRocket(double wantedx, double angle, Command command) {
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {

            if (Timer.getFPGATimestamp() - inTheZone > 0.15) {
                c = true;
            } else {

            }
        } else {

            c = false;
            inTheZone = Timer.getFPGATimestamp();
        }
        if (getDistance() > wantedx - 5 && getDistance() < wantedx + 5) {
            robotDrive.arcadeDrive(-0.13, 0);
            command.command();

        } else if (getDistance() > wantedx + 5) {
            robotDrive.arcadeDrive(-0.5, 0);
        } else {
            goToDistanceDeepSpaceRock(wantedx, angle);
        }

    }

    public void goToDistanceDeepSpaceRock(double wantedX, double angle) {
        PID.gyroPID(0, getAngle());

        if (getDistance() < wantedX - 120) {
            robotDrive.curvatureDrive(0.45, PID.gyroPIDres, false);
        } else {
            robotDrive.curvatureDrive(0.13, PID.gyroPIDres, false);
        }

    }

    public void setSeperately(double right,double left){
        robotDrive.tankDrive(right, left);
    }

       

    
         
     }

     




