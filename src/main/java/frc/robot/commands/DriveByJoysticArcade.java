/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.DriverInterface;

public class DriveByJoysticArcade extends Command {
  public DriveByJoysticArcade() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double x = Robot.driverInterface.joystickRight.getX();
    double y = -Robot.driverInterface.joystickRight.getY(); //joystic's y value is reversed
    x =  Math.abs(x)<DriveByJoystickCommand.MIN_JS_VALUE ? 0 : x;
    y =  Math.abs(y)<DriveByJoystickCommand.MIN_JS_VALUE ? 0 : y;
    double leftSpeed = y - x;
    double rightSpeed = y + x;
    if(leftSpeed < -1) leftSpeed = -1;
    if(leftSpeed > 1) leftSpeed = 1;
    if(rightSpeed < -1) rightSpeed = -1;
    if(rightSpeed > 1) rightSpeed = 1;
    System.out.println("drive: " + x + " " + y + " - " + leftSpeed + "/" + rightSpeed);
    Robot.chassis.SetValue(leftSpeed,rightSpeed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
