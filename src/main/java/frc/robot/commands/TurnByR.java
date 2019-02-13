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

public class TurnByR extends Command {

	public double targetAngle;
	public double angle;
	double rateOfRotation;
	double r;

  public TurnByR(double angle , double speedAngle, double r) {
		this.angle = angle;
		this.rateOfRotation = speedAngle;
		this.r = r;
	}

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
		double outer_mm_per_degree = (r + Chassis.WHEEL_BASE/2) * Math.PI / 180;
		double inner_mm_per_degree = (r - Chassis.WHEEL_BASE/2) * Math.PI / 180;
		System.out.println("outer_mm_per_degree: " + outer_mm_per_degree + 
		"  inner_mm_per_degree: " + inner_mm_per_degree);
    Robot.chassis.SetCommand(this);
		targetAngle= Robot.chassis.NormalizeAngle(angle + Robot.chassis.GetAngle());
		double outerSpeed = outer_mm_per_degree * rateOfRotation;
		double innerSpeed = inner_mm_per_degree * rateOfRotation;
		System.out.println("outerSpeed: " + outerSpeed + "  innerSpeed: " + innerSpeed);
		if(angle > 0) {
			Robot.chassis.SetSpeed(outerSpeed, innerSpeed);
		} else {
			Robot.chassis.SetSpeed(innerSpeed, outerSpeed);
		}
  }
	 
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {

		double currAngle = Robot.chassis.GetAngle();
		double remain = targetAngle - currAngle;
		System.out.println("TuurnR remain - " + remain);
    if((angle >= 0 && remain <=3 ) || (angle < 0 && remain >= -3)) {
			return true;
		}
		return false;
	}


  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("END Turn By Degree to " + angle +  " end angle=" + Robot.chassis.GetAngle()); 
		Robot.chassis.StopMotors();
		Robot.chassis.SetCommand(null);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
		close();
  }
}
