/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
/**
 * Add your docs here.
 */
public class Lift extends Subsystem {
  public static final double K_P = 0;
  public static final double K_I = 0;
  public static final double K_D = 0;
  public static final double MM_PER_PULSE = 0;
  public final double BOTTOM_LEVEL = 0 * MM_PER_PULSE;
  public final double MIDDLE_LEVEL = 0 * MM_PER_PULSE;
  public final double UP_LEVEL = 0 * MM_PER_PULSE;
  public static final double JOYSTICK_MOVE = 100 * MM_PER_PULSE;
  public static final double CHANGE_DIR_MOVE = 100;
 
  public double encoderBase = 0;
  public boolean isforward;
 // public static LiftCommand command = null;

  public TalonSRX liftMotor;
  public TalonSRX switchMotor; 

  public Lift(){
  //  liftMotor= new TalonSRX(RobotMap.portLiftMotor);
    switchMotor= new TalonSRX(RobotMap.portArmMotor);
    liftMotor.config_kP(0, K_P,0);
    liftMotor.config_kI(0, K_I,0);
    liftMotor.config_kD(0, K_D,0);
    ResetEnc();
    //command= new LiftCommand();
    isforward = true;
  }

  public void ResetEnc(){
    encoderBase = liftMotor.getSelectedSensorPosition(0);
    StopMotor();
  }

  public void ChangeDir(){
    isforward = !isforward;
  }

  /*public void Enable(){
    if(!command.isRunning()){
      command.start();
    }else{
      command.StopMotor();;
    }
  }*/

  public double GetPosition(){
    return liftMotor.getSelectedSensorPosition(0)-encoderBase;
   }

   /*public void MoveTo(double pos){
     command.MoveTo(pos); 
   }*/

  public void StopMotor(){
    SetValue(0);
  }

  public void SetValue(double value){
    liftMotor.set(ControlMode.Velocity, value);
  }

  public void StopChangeMotor(){
    SetChangeMotorValue(0);
  }

  public void SetChangeMotorValue(double value){
    liftMotor.set(ControlMode.Velocity, value);
  }

 

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
