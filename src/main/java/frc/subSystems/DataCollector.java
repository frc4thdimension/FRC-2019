package frc.subSystems;

public class DataCollector{

    double rightEncoderData;
    double leftEncoderData;
    double gyroData;
    
    //Joystick axis values
    double axisLeft;
    double axisRight;



	public double getRightEncoderData() {
		return this.rightEncoderData;
	}

	public void setRightEncoderData(double rightEncoderData) {
		this.rightEncoderData = rightEncoderData;
	}

	public double getLeftEncoderData() {
		return this.leftEncoderData;
	}

	public void setLeftEncoderData(double leftEncoderData) {
		this.leftEncoderData = leftEncoderData;
	}

	public double getGyroData() {
		return this.gyroData;
	}

	public void setGyroData(double gyroData) {
		this.gyroData = gyroData;
	}

	public double getAxisLeft() {
		return this.axisLeft;
	}

	public void setAxisLeft(double axisLeft) {
		this.axisLeft = axisLeft;
	}

	public double getAxisRight() {
		return this.axisRight;
	}

	public void setAxisRight(double axisRight) {
		this.axisRight = axisRight;
	}

}

	
