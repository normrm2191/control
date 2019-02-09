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

public class TurnByDegrees extends Command {

	double K_P;
  static final double K_I = 0.0;
	static final double K_D = 0.0;
	static final double WHEEL_BASE = 635;
	static final double FULL_CIRCLE = Math.PI * WHEEL_BASE; // Wheel Base is the Diameter
	static final double MM_PER_DEGREE = FULL_CIRCLE/360;
	static final double VELOCITY = MM_PER_DEGREE / 10;
	static final double FINAL_ANGLE = 30;
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
	int nLoop = 0;


  public TurnByDegrees(double angle , double speedAngle) {
		this.angle = angle;
		K_P = 1 / FINAL_ANGLE;
		maxSpeedAngle = speedAngle;
	}
	
	


  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
		currAngle = Robot.chassis.GetAngle();
    Robot.chassis.SetCommand(this);
		_startTime = System.currentTimeMillis();
		targetAngle= Robot.chassis.NormalizeAngle(angle + currAngle);
		nLoop = 0;
		System.out.println("Turn By Degree to " + angle +  " startTime = " + _startTime + " start angle=" + Robot.chassis.GetAngle() + 
				" Target=" + targetAngle + " K_P= " + K_P + " max speed angle= " + maxSpeedAngle);
		sumError = 0;
		lastError = 0;
		error = remaining();
  }
  
  private double remaining() {
		return Math.abs(targetAngle - currAngle);
	}
	
	private void setPosition(double angle) {
		/*if(angle < FINAL_ANGLE){
			speedAngle = (int)( speed * 0.5 * angle / FINAL_ANGLE);
		}
		System.out.println("Turn to " + angle + " speedAngle = " + speedAngle);
		Robot.chassis.SetSpeed(speedAngle,-speedAngle);*/
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
		Robot.chassis.SetSpeed(speed, -speed);
		System.out.println("speed: " + speed + " /error: " + error);
		lastError = error;
    /*nLoop++;
		if(nLoop != 1) {
			setPosition(remaining());
		}*/
		//setPosition(remaining());
		/*if(Math.abs(speedAngle) > VELOCITY && Math.abs(remaining()) < 30) {
			setPosition(remaining());
		}*/

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
