/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.Lift;

public class LiftCommand extends Command {

public static final double UP_SPEED = 3;
public static final double DOWN_SPEED = 3;

private Lift lift;
private XboxController xbox;

double target = 0;
boolean isMoving;
boolean isMovingChanger;
boolean movingUp;
ChangeLiftDir commandChangeDir = null;

  public LiftCommand() {
    lift= Robot.lift;
    xbox= Robot.driverInterface.xbox;
    isMoving = false;
    isMovingChanger = false;
    movingUp= true;
    commandChangeDir = new ChangeLiftDir();
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
  }

  public void MoveTo(double pos)
  {
    target = pos;
    isMoving = true;
    if(lift.GetPosition()< target){
      movingUp= true;
      lift.liftMotor.set(ControlMode.Velocity,UP_SPEED);
    }else{
      movingUp= false;
      lift.liftMotor.set(ControlMode.Velocity, DOWN_SPEED);
    }
  }
  
  public void ChangeDir(){
    if(!commandChangeDir.isRunning()){
      commandChangeDir.start();
      isMovingChanger = true;
    }
  }

  public void StopChangeMotor(){
    isMovingChanger = false;
  }

  public void StopMotor(){
    lift.StopMotor();
    isMoving = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (isMoving){
      if(movingUp){
        if(lift.GetPosition()>=target){
          StopMotor();
        }
      }
      else if (lift.GetPosition()<=target){
        StopMotor();
      }
    }
      if(xbox.getRawButton(RobotMap.BUTTON_BOTTOM)){
        MoveTo(lift.BOTTOM_LEVEL);
      }
      else if (xbox.getRawButton(RobotMap.BUTTON_MIDDLE)){
        MoveTo(lift.MIDDLE_LEVEL);
      }
      else if (xbox.getRawButton(RobotMap.BUTTON_UP)){
        MoveTo(lift.UP_LEVEL);      
      }
      else if (xbox.getRawButton(RobotMap.BUTTON_CHANGE_LIFT_DIR)){
        ChangeDir();    
      }
    if(!commandChangeDir.isRunning()){
      StopChangeMotor();
    }
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
