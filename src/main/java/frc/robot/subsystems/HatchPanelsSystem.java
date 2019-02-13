/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.ChangeDir;
/**
 * Add your docs here.
 */
public class HatchPanelsSystem extends Subsystem {
  
  public static final double K_P = 0;
  public static final double K_I = 0;
  public static final double K_D = 0;
  public  final double CHANGE_DIR_MOVE = 100;
  public static final double PULSE_DIS=0.116;

 
  public double encoderBase = 0;
  public boolean isforward;
  public TalonSRX motor; 
  public Command changeDirCommand;

  public HatchPanelsSystem(){
    isforward = true;
    motor= new TalonSRX(RobotMap.portArmMotor);
    motor.config_kP(0, K_P,0);
    motor.config_kI(0, K_I,0);
    motor.config_kD(0, K_D,0);
    ResetEnc();
    changeDirCommand = null;
  }

  public void ResetEnc(){
    encoderBase = motor.getSelectedSensorPosition(0);
    StopMotor();
  }

  public void ChangeDir(){
    isforward = !isforward;
  }

  public void StopMotor(){
    SetValue(0);
  }

  public void SetValue(double value){
    motor.set(ControlMode.Velocity, value);
  }

  public double GetPositionInMM(){
    return (motor.getSelectedSensorPosition(0)- encoderBase) * PULSE_DIS;
  }

   public void UpdateStatus(){
     if(changeDirCommand == null && Robot.driverInterface.joystickRight.getRawButtonPressed(3)){
       changeDirCommand= new ChangeDir();
       changeDirCommand.start();
     }
   }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
