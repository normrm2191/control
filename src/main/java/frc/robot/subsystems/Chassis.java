/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.GroupOfMotors;
import frc.robot.Robot;
import frc.robot.RobotMap;


public class Chassis extends Subsystem implements Sendable {
  public GroupOfMotors motorsRight;
  public GroupOfMotors motorsLeft;
  public PigeonIMU _gyro = null;
  public Command activeDriveCommand= null;
  public CommandGroup activeDriveCommandGroup= null;
  public DoubleSolenoid shifter; // open = fast
  public boolean isReverseMode;
  public double max_speed;
  public boolean in_fast_mode = false;
  public boolean inSpeedMode = true;
  public double baseGyro = 0;

  public static final double WHEEL_BASE = 600;
//  public boolean isSpeedMode;

  public Chassis(){
    shifter = new DoubleSolenoid(RobotMap.portPCM,RobotMap.portShifterForward, RobotMap.portShifterReverse);
    max_speed = GroupOfMotors.MAX_SPEED_SLOW / GroupOfMotors.PULSE_DIS;
    motorsRight= new GroupOfMotors(RobotMap.portMotor1Right, RobotMap.portMotor2Right, in_fast_mode, "Right");
    motorsLeft= new GroupOfMotors(RobotMap.portMotor1Left, RobotMap.portMotor2Left, in_fast_mode, "Left");
    try{
      _gyro = new PigeonIMU(RobotMap.CAN_GYRO_PORT);
      setName("chassis");
    }
    catch(Exception e){
      _gyro= null;
    }
 //   isSpeedMode = true;
    motorsRight.SetReverseMode(true);
    isReverseMode = false;
    SmartDashboard.putData(this);
    motorsRight.reverseEncoder();
    motorsLeft.reverseEncoder();
}

public void resetGyro() {
  baseGyro = GetRawAngle();
}

public void setPIDFromSmartDashboard() {
  double k_f = SmartDashboard.getNumber("Motor K_F", 0);
  double k_p = SmartDashboard.getNumber("Motor K_P", 0);
  double k_i = SmartDashboard.getNumber("Motor K_I", 0);
  double k_d = SmartDashboard.getNumber("Motor K_D", 0);
  Set_K_D(k_d);
  Set_K_F(k_f);
  Set_K_I(k_i);
  Set_K_P(k_p);
  System.out.printf("PID = %f/%f/%f/%f\n",k_p,k_i,k_d,k_f);
}

public void Set_K_P(double K_P){
  motorsLeft.ConfigKP(K_P);
  motorsRight.ConfigKP(K_P);
}

public void Set_K_I(double K_I){
  motorsLeft.ConfigKI(K_I);
  motorsRight.ConfigKI(K_I);
}

public void Set_K_D(double K_D){
  motorsLeft.ConfigKD(K_D);
  motorsRight.ConfigKD(K_D);
}
public void Set_K_F(double K_F){
  motorsLeft.ConfigKF(K_F);
  motorsRight.ConfigKF(K_F);
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
  
public double GetRawAngle(){
  return -_gyro.getFusedHeading();
}
  public double GetAngle(){
  if(_gyro != null) {
    double h = GetRawAngle() - baseGyro;
    if(h > 180) {
      return 180 - h;
    } else if(h < -180) {
      return h + 180;
    } else {
      return h;
    }
  } else {
    return 0;
  }

}

public void SetCommand(Command cmd){
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

 public void motorsSetValue(double left, double right){
   if(!isReverseMode){
    motorsRight._setValue(right);
    motorsLeft._setValue(left);
   }
   else{
    motorsRight._setValue(-left);
    motorsLeft._setValue(-right);
     }
 }

 public void SetSpeed(double Lspeed, double Rspeed){
  if(!isReverseMode){
    motorsRight._setSpeed(Rspeed);
    motorsLeft._setSpeed(Lspeed);
   }
   else{
    motorsRight._setSpeed(-Lspeed);
    motorsLeft._setSpeed(-Rspeed);
   }
 }


public void StopMotors(){
   motorsRight.StopMotors();
   motorsLeft.StopMotors();
}

public double GetDistance() {
  double r = motorsLeft._GetPositionInMM() + motorsRight._GetPositionInMM();
  r = r/2;
  if(isReverseMode) {
    return -r;
  } else {
    return r;
  }
}

public double getLeftDistance() {
  if(isReverseMode) {
    return -motorsRight._GetPositionInMM();
  } else {
    return motorsLeft._GetPositionInMM();
  }
}

public double getRightDistance() {
  if(isReverseMode) {
    return -motorsLeft._GetPositionInMM();
  } else {
    return motorsRight._GetPositionInMM();
  }
}

public double NormalizeAngle(double angle){
  if(angle > 180){
    return 360 - angle;
  }
   else if(angle < -180){
     return 360 + angle;
   }
   else{
     return angle;
   }
}

public void setSlowMode(){
  shifter.set(DoubleSolenoid.Value.kReverse);
  in_fast_mode = false;
  max_speed = GroupOfMotors.MAX_SPEED_SLOW;
  motorsLeft.SetSlow();
  motorsRight.SetSlow();
}

public void setFastMode() { // CloseShifter(){
  shifter.set(DoubleSolenoid.Value.kForward);
  in_fast_mode = true;
  max_speed = GroupOfMotors.MAX_SPEED_FAST;
  motorsLeft.SetFast();
  motorsRight.SetFast();
}

public void offShifter(){
  shifter.set(DoubleSolenoid.Value.kOff);
}

public void SetReverseMode(boolean isReverse){
  isReverseMode = isReverse;
}

public void resetEncs(){
  motorsLeft.ResetEnc();
  motorsRight.ResetEnc();
}

  @Override
  public void initDefaultCommand() {
   
  }

  double [] ypr = {0,0,0};
  double [] wxyz = {0,0,0,0};

  public double[] getGyroYPR(){
    if(_gyro!=null){
      _gyro.getYawPitchRoll(ypr);
      return ypr;
    } else {
      return ypr;
    }
    
  }
  public double getGyroYaw(){
    if(_gyro!=null){
      _gyro.getYawPitchRoll(ypr);
      return ypr[0];
    } else {
      return 999;
    }
  }
  public double getGyroPitch(){
    if(_gyro!=null){
      _gyro.getYawPitchRoll(ypr);
      return ypr[1];
    } else {
      return 999;
    }
  }
  public double getGyroRoll(){
    if(_gyro!=null){
      _gyro.getYawPitchRoll(ypr);
      return ypr[2];
    } else {
      return 999;
    }
  }

  public double getZ() {
    _gyro.get6dQuaternion(wxyz);
    return wxyz[3];
  }
  public double getY() {
    _gyro.get6dQuaternion(wxyz);
    return wxyz[2];
  }
  public double getX() {
    _gyro.get6dQuaternion(wxyz);
    return wxyz[1];
  }
  public double getW() {
    _gyro.get6dQuaternion(wxyz);
    return wxyz[0];
  }

  public boolean isFastMode() {
    return in_fast_mode;
  }

  public boolean isReverseMode() {
    return isReverseMode;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Yaw",this::getGyroYaw, null);
    builder.addDoubleProperty("Pitch",this::getGyroPitch, null);
    builder.addDoubleProperty("Roll",this::getGyroRoll, null);
    builder.addDoubleProperty("Angle",this::GetAngle, null);
    builder.addDoubleProperty("L Enc",this::getLeftDistance, null);
    builder.addDoubleProperty("R Enc",this::getRightDistance, null);
    builder.addBooleanProperty("Fast",this::isFastMode, null);
    builder.addBooleanProperty("Reverse",this::isReverseMode, null);
  }

 // called peridically to update the shifter and reset encoders and reverse mode
  public void UpdateStatus() {
    // change shifter
    if(Robot.driverInterface.joystickRight.getRawButtonPressed(DriverInterface.BUTTON_SHIFTER)){
      System.out.println("Set Slow Mode"); 
      setSlowMode();
    } else if(Robot.driverInterface.joystickLeft.getRawButtonPressed(DriverInterface.BUTTON_SHIFTER)){
      System.out.println("Set fast Mode"); 
      setFastMode();
    } else {
      offShifter();
    }

    // reverse mode
    int pov = Robot.driverInterface.xbox.getPOV();
    if(pov == 180){  //set reverse mode 
      Robot.chassis.SetReverseMode(true);
    } else if(pov == 0) {
      Robot.chassis.SetReverseMode(false);
    } 

    // resetr encoder
    if(Robot.driverInterface.joystickLeft.getRawButtonPressed(DriverInterface.RESET_ENCODER)) { 
      resetEncs();
    }
  }
}
