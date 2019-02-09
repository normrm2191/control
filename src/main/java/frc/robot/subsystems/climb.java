/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Climb extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public TalonSRX liftMotor_top;
  public TalonSRX liftMotor_bot;
  public TalonSRX move_motor;

public Climb()  {
  liftMotor_bot = new TalonSRX(RobotMap.portClimbMotorBack);
  liftMotor_top = new TalonSRX(RobotMap.portClimbMotorTop);
  move_motor = new TalonSRX(RobotMap.portClimbMoveMotor);
}

public void setValue_moveMotor(double value){
  move_motor.set(ControlMode.PercentOutput, value);
} 

public void setValue_liftMotorTop(double value){
  liftMotor_top.set(ControlMode.PercentOutput, value);
} 

public void setValue_liftMotorBot(double value){
  liftMotor_bot.set(ControlMode.PercentOutput, value);
} 

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
