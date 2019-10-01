/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Chassis;

public class TurnByDegrees extends Command {

  static final double K_I = 0.0;
	static final double K_D = 0.0;
	static final double K_P = 1.0/30.0; // reduce rotation rate from 30 degrees
	static final double FULL_CIRCLE = Math.PI * Chassis.WHEEL_BASE; // Wheel Base is the Diameter
	static final double MM_PER_DEGREE = FULL_CIRCLE / 360;
	static final double VELOCITY = MM_PER_DEGREE / 10;
	static final double MIN_SPEED = 500;
	double targetAngle; //  the abs target angle = angle at start + angle
	double angle; // turn from start angle by this amount
	double maxRotationRate;
	double lastError;
	double sumError;
	boolean isAbsAngle;



  public TurnByDegrees(double angle , double speedAngle) {
		this(angle, speedAngle, false);
	}
  public TurnByDegrees(double angle , double speedAngle, boolean isAbsAngle) {
		this.angle = angle;
		maxRotationRate = speedAngle;
		this.isAbsAngle = isAbsAngle;
	}
	
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
		double currAngle = Robot.chassis.GetAngle();
		Robot.chassis.SetCommand(this);
		if(isAbsAngle) {
			targetAngle = angle;
			angle = Robot.chassis.NormalizeAngle(targetAngle - currAngle);
		} else {
			targetAngle= Robot.chassis.NormalizeAngle(angle + currAngle);
		}
		System.out.println("Turn By Degree to " + angle + " start angle=" + Robot.chassis.GetAngle() + 
			" Target=" + targetAngle + " K_P= " + K_P + " max speed angle= " + maxRotationRate);
		sumError = 0;
		lastError = 0;
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
		double rotationRate;
		double currAngle = Robot.chassis.GetAngle();
		double error = targetAngle - currAngle;
		double p;
		double i;
		double d;
		double corr;
		double speed;
		sumError += error; 
		p = error * K_P;
		i = sumError * K_I;
		d = (error - lastError) * K_D;
		lastError = error;
		corr = p + i + d;
		rotationRate = maxRotationRate * corr;
		if(rotationRate > maxRotationRate) {
			rotationRate = maxRotationRate;
		} else if(rotationRate < -maxRotationRate) {
			rotationRate = -maxRotationRate;
		}
		speed = rotationRate * MM_PER_DEGREE;
		if(speed > 0 && speed < MIN_SPEED) {
			speed = MIN_SPEED;
		} else if(speed < 0 && speed > -MIN_SPEED) {
			speed = -MIN_SPEED;
		}
		//speed = SetSpeed(speed);
		Robot.chassis.SetSpeed(speed, -speed);
		System.out.println("speed: " + speed + " /error: " + error);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if((angle >= 0 && lastError <=3 ) || (angle < 0 && lastError >= -3)) {
			return true;
		}
		return false;
	}


  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("END Turn By Degree to " + angle +  " end angle=" + Robot.chassis.GetAngle() + "error= " + lastError); 
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
