/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class UnDropHatch extends Command {

  int cycles = 0;

  public UnDropHatch() {
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    cycles = 0;
    Robot.hatchPanelsSystem.unDropPanel();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    cycles++;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return cycles >= 10;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.hatchPanelsSystem.stopBuchna();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
