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

  public final double END_DIS = 50;
  public final double K_P = 1 / END_DIS;
  public final double MAX_SPEED = 50;
  public final double SPEED = 35;
  public final double MIN_SPEED = 20;
  public double target;
  public double startDis;
  private HatchPanelsSystem hatchPanelsSystem;

  public ChangeDir() {
    target = hatchPanelsSystem.isforward ? -hatchPanelsSystem.CHANGE_DIR_MOVE : hatchPanelsSystem.CHANGE_DIR_MOVE; 
    hatchPanelsSystem = Robot.hatchPanelsSystem;   
    
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startDis = hatchPanelsSystem.GetPositionInMM(); 
    target = target + startDis;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double speed = SPEED * K_P;
    if(speed > MAX_SPEED)
      speed = MAX_SPEED;
    else if(speed < MIN_SPEED)
      speed = MIN_SPEED;
    if(hatchPanelsSystem.isforward){
      hatchPanelsSystem.SetValue(-speed);
    }else{
      hatchPanelsSystem.SetValue(speed);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (hatchPanelsSystem.motor.getSensorCollection().isFwdLimitSwitchClosed()){
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    hatchPanelsSystem.StopMotor();
    hatchPanelsSystem.ChangeDir();
    Robot.hatchPanelsSystem.changeDirCommand = null;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
