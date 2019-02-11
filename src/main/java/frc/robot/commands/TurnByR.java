/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.GroupOfMotors;
import frc.robot.Robot;

public class TurnByR extends Command {

	double K_P;
  static final double K_I = 0.0;
	static final double K_D = 0.0;
	double widthChassis;
	static double FULL_CIRCLE = Math.PI * Robot.chassis.WHEEL_BASE; // Wheel Base is the Diameter
	static double MM_PER_DEGREE = FULL_CIRCLE / 360;
	static double VELOCITY = MM_PER_DEGREE / 10;
	static double FINAL_ANGLE = 30;
	public double targetAngle;
	public double angle;
	double error;
	double lastError;
	double sumError;
	double lastSetPosition;
	double speedAngle;
	double maxSpeedAngle;
	double currAngle;
	double p;
	double i;
	double d;
	long _startTime;

	double r;
	double left_speed;
	double right_speed;


  public TurnByR(double angle , double speedAngle, double r) {
		this.angle = angle;
		K_P = 1 / FINAL_ANGLE;
		maxSpeedAngle = speedAngle;
		widthChassis = Robot.chassis.WHEEL_BASE;
		this.r = r;
//		FULL_CIRCLE = Math.PI * 
	}
	
	


  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
		currAngle = Robot.chassis.GetAngle();
    Robot.chassis.SetCommand(this);
		_startTime = System.currentTimeMillis();
		targetAngle= Robot.chassis.NormalizeAngle(angle + currAngle);
		System.out.println("Turn By Degree to " + angle +  " startTime = " + _startTime + " start angle=" + Robot.chassis.GetAngle() + 
				" Target=" + targetAngle + " K_P= " + K_P + " max speed angle= " + maxSpeedAngle);
		sumError = 0;
		lastError = 0;
		error = remaining();
  }
  
  private double remaining() {
		return Math.abs(targetAngle - currAngle);
	}
	

	public void SetSpeed(double speed){
    if(angle < 0){
      right_speed = speed * (r + 0.5 * widthChassis) / r;
      left_speed = speed * (r - 0.5 * widthChassis) / r;
    }
    else{
      left_speed = speed * (r + 0.5 * widthChassis) / r;
      right_speed = speed * (r - 0.5 * widthChassis) / r;
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
		double corr;
		double speed;
		currAngle = Robot.chassis.GetAngle();
		error = remaining();
		sumError += error; 
		p = error * K_P;
		i = sumError * K_I;
		d = (error - lastError) * K_D;
		corr = p + i + d;
		speedAngle = maxSpeedAngle * corr;
		if(speedAngle > maxSpeedAngle)
			speedAngle = maxSpeedAngle;
		speed = speedAngle * MM_PER_DEGREE;
		if(Math.abs(speed) < GoStraight.MIN_SPEED){
			speed = angle < 0 ? -GoStraight.MIN_SPEED : GoStraight.MIN_SPEED; 
		}
    SetSpeed(speed);
		Robot.chassis.SetSpeed(left_speed, -right_speed);
		System.out.println("speed: " + speed + " /error: " + error);
		lastError = error;

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if((angle >= 0 && error <=3 ) || (angle < 0 && error >= -3)) {
			return true;
		}
		return false;
	}


  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("END Turn By Degree to " + angle +  " end angle=" + currAngle + "error= " + error); 
    /*Robot.chassis.motorsLeft.ConfigKP(GroupOfMotors.K_P);
    Robot.chassis.motorsRight.ConfigKP(GroupOfMotors.K_P);*/
		Robot.chassis.StopMotors();
		Robot.chassis.SetCommand(null);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
