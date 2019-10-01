/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPanelsSystem;

public class ChangeDir extends Command {

  public static final double MIN_ERROR = 5;
  double target = 0;
  double powerUp = 0.5;
  double powerDown = 0.2;
  long startTime;
  final double upTime = 500;
  final double downTime = 1000;

  public ChangeDir(boolean forward) {
    double dir = forward ? 1: -1;;
    powerUp = dir * powerUp;
    powerDown = dir * powerDown;
    startTime = System.currentTimeMillis();
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
 /*   double pos = Robot.hatchPanelsSystem.getEncoder();
    if(Robot.hatchPanelsSystem.isForward()) { 
      // set position to back
      target = pos - HatchPanelsSystem.CHANGE_DIR_MOVE;
    } else {
      target = pos + HatchPanelsSystem.CHANGE_DIR_MOVE;
    }
    Robot.hatchPanelsSystem.SetPosituon(target); */

    if(System.currentTimeMillis() - startTime > upTime){
      Robot.hatchPanelsSystem.SetPower(powerDown);
    }
    else{
      Robot.hatchPanelsSystem.SetPower(powerUp);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
//    return Math.abs(Robot.hatchPanelsSystem.getEncoder() - target) < MIN_ERROR;
      return System.currentTimeMillis() - startTime > upTime + downTime;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.hatchPanelsSystem.ChangeDir();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
