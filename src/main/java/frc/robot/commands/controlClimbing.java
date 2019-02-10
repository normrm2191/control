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
import frc.robot.subsystems.climb;

public class controlClimbing extends Command {
  public controlClimbing() {
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
    double move_value = Robot.driverInterface.joystickLeft.getRawAxis(3);
    double lift_value = Robot.driverInterface.joystickRight.getRawAxis(3);
    move_value = (move_value + 1) / 2;
    lift_value = (lift_value + 1) / 2;
    Robot.climb.setValue_moveMotor(move_value);
    Robot.climb.setValue_liftMotorBot(lift_value);
    Robot.climb.setValue_liftMotorTop(lift_value);
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
