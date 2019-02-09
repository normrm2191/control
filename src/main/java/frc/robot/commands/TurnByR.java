/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnByR extends Command {

  public static final double widthChassis = 600;
  public static final double k_p = 0.6;
  public double targetAngle;
  public double baseSpeed;
  public double leftSpeed;
  public double rightSpeed;
  public double startAngle = 30;
  public double endAngle = 60;
  public double angle;
  public double minSpeed;
  double r;

  public TurnByR(double speed) {
    baseSpeed = speed;
    minSpeed = 0.3;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.chassis.SetCommand(this);
    angle = endAngle - startAngle;
    double calcAngle = (90 - angle)/2;
    r = (Math.tan(calcAngle)) * 200 + (widthChassis/2);
    targetAngle = Robot.chassis.GetAngle() + angle;
    Robot.chassis.motorsLeft.ConfigKP(k_p);
    Robot.chassis.motorsRight.ConfigKP(k_p);
    
  } 

  public void SetSpeed(double speed){
    baseSpeed = speed;
    if(angle < 0){
      rightSpeed = baseSpeed;
      leftSpeed = (r - widthChassis) / r;
    }
    else{
      leftSpeed = baseSpeed;
      rightSpeed = (r - widthChassis) / r;
    }
  }

  private double remaining() {
		return targetAngle - Robot.chassis.GetAngle();
  }
  
  private double AbsRemaining(){
    return Math.abs(remaining());
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(AbsRemaining() < 15 && baseSpeed != minSpeed)
    {
      if(angle > 0){
        SetSpeed(minSpeed);
      }
      else{
        SetSpeed(-minSpeed);
      }
    }
    Robot.chassis.SetSpeed(leftSpeed, rightSpeed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if((targetAngle > 0 && remaining()<=3) || ((targetAngle < 0 && remaining()>=-3))){
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.chassis.StopMotors();
    Robot.chassis.SetCommand(null);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
