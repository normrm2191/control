/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.GroupOfMotors;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;


public class Chassis extends Subsystem {
  public GroupOfMotors motorsRight;
  public GroupOfMotors motorsLeft;
  public ADXRS450_Gyro gyro = null;
  public Command activeDriveCommand= null;
  public CommandGroup activeDriveCommandGroup= null;
  private double calibrateStartTime = 0;
  private double calibrateStartAngle = 0;
  private boolean inCalibration = true;
  private double fixRate = 0;

  public Chassis(){
    motorsRight= new GroupOfMotors(RobotMap.portMotor1Right, RobotMap.portMotor2Right);
    motorsLeft= new GroupOfMotors(RobotMap.portMotor1Left, RobotMap.portMotor2Left);
    try{
      gyro= new ADXRS450_Gyro();
      gyro.calibrate();
    }
    catch(Exception e){
      gyro= null;
    }
}

public boolean HaveActiveCommand()
{
  if(activeDriveCommand != null && !activeDriveCommand.isRunning())
  {
    activeDriveCommand= null;
  }
  if(activeDriveCommandGroup != null && !activeDriveCommandGroup.isRunning())
  {
    activeDriveCommandGroup= null;
  }
  return activeDriveCommand != null || activeDriveCommandGroup != null;
}

public void DisableDriveCommand(){

  if(activeDriveCommand != null){
    activeDriveCommand.cancel();
    activeDriveCommand = null;
  }else if(activeDriveCommandGroup != null){
    activeDriveCommandGroup.cancel();
    activeDriveCommandGroup= null;
  }
}

public void SetCoomand(Command cmd){
  if(cmd!= null && activeDriveCommand != null){
    try{
      activeDriveCommand.cancel();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  activeDriveCommand= cmd;
}

public void Calibrate(){
  if(gyro != null){
    if(!Robot.robot.isEnabled() && inCalibration){
      fixRate = (gyro.getAngle() - calibrateStartAngle / System.currentTimeMillis() - calibrateStartTime);
    }else{
      inCalibration = false;
    }
  }
}

public void GyroReset(){
  if(gyro!= null){
    gyro.reset();
    calibrateStartAngle=System.currentTimeMillis();
    calibrateStartAngle = gyro.getAngle();
    inCalibration = true ;
  }
}

 public void SetValue(double speedLeft, double speedRight){
   motorsRight.setValue(speedRight);
   motorsLeft.setValue(speedLeft);
 }

public void StopMotors(){
  motorsRight.StopMotors();
   motorsLeft.StopMotors();
}

  @Override
  public void initDefaultCommand() {
   
  }
}
