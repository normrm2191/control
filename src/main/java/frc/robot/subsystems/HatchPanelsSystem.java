/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
/**
 * Add your docs here.
 */
import frc.robot.commands.DropHatch;
public class HatchPanelsSystem extends Subsystem {
  
  public static final double K_P = 0.1;
  public static final double K_I = 0;
  public static final double K_D = 0;
  public static final double CHANGE_DIR_MOVE = 250;

 
  public double encoderBase = 0;
  public boolean isforward;
  public TalonSRX motor; 
  public Command changeDirCommand;
  public DoubleSolenoid buchna;

  public enum ArmPosition { FRONT, UP, BACK};

  public ArmPosition armPosition;

  public HatchPanelsSystem() {
    super();
    armPosition = ArmPosition.UP;
    isforward = true;
    motor= new TalonSRX(RobotMap.portArmMotor);
    motor.config_kP(0, K_P);
    motor.config_kI(0, K_I);
    motor.config_kD(0, K_D);
    ResetEnc();
    changeDirCommand = null;
    setName("Hatch");
    try {
      buchna = new DoubleSolenoid(RobotMap.portPCM, RobotMap.portHatchPanelBuchnaFwd, RobotMap.portHatchPanelBuchnaBwd);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    SmartDashboard.putData(this);
  }

  public void ResetEnc() { 
    encoderBase = motor.getSelectedSensorPosition();
  }
  public void ResetEnc(double value) {
    encoderBase = motor.getSelectedSensorPosition() - value;
  }

  public void ChangeDir(){
    isforward = !isforward;
    ResetEnc();
  }

  public void StopMotor(){
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void SetPosituon(double value){
//    motor.set(ControlMode.Position, value + encoderBase);
  }

  public void SetSpeed(double value){
//    motor.set(ControlMode.Velocity, value);
  }

  public void SetPower(double value){
    motor.set(ControlMode.PercentOutput, value);
  }

  public double getEncoder() {
    return motor.getSelectedSensorPosition(); // - encoderBase;
  }

  boolean dropHatchPressed = false;
  DropHatch dropHatch = new DropHatch();

   public void UpdateStatus(){
     if(Robot.driverInterface.joystickLeft.getRawButtonPressed(DriverInterface.RESET_ARM_ENCODER)){
       ResetEnc();
     }
     double v = Robot.driverInterface.xbox.getTriggerAxis(Hand.kRight);
     if(v > 0.5 && !dropHatchPressed) {
       dropHatchPressed = true;
       dropHatch.start();
     } else if(v < 0.5) {
       dropHatchPressed = false;
     }
   }

  @Override
  public void initDefaultCommand() {
  }

  public boolean isForward() {
    return isforward;
  }
  public boolean isFrontSwitch() {
    return motor.getSensorCollection().isFwdLimitSwitchClosed();
  }
  public boolean isBackSwitch() {
    return motor.getSensorCollection().isRevLimitSwitchClosed();
  }

  public void dropPanel(){
    buchna.set(DoubleSolenoid.Value.kReverse);
  }
  
  public void unDropPanel(){
    buchna.set(DoubleSolenoid.Value.kForward);
  }

  public void stopBuchna(){
    buchna.set(DoubleSolenoid.Value.kOff);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Encoder",this::getEncoder ,null);
    builder.addBooleanProperty("Is Front",this::isForward ,null);
  }

}
