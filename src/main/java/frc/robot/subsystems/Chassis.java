/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.GroupOfMotors;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.CloseShifter;
import frc.robot.commands.OpenShifter;


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
  public DoubleSolenoid shifter; // open = fast
  public boolean isReverseMode;
  public double max_speed;
  public boolean in_fast_mode = false;
//  public boolean isSpeedMode;

  public Chassis(){
    shifter = new DoubleSolenoid(11,RobotMap.portShifterForward, RobotMap.portShifterReverse);
    max_speed = GroupOfMotors.MAX_SPEED_SLOW / GroupOfMotors.PULSE_DIS;
    if(shifter.get() == DoubleSolenoid.Value.kReverse) {
      in_fast_mode = true;
      max_speed = GroupOfMotors.MAX_SPEED_FAST / GroupOfMotors.PULSE_DIS;
    }
    motorsRight= new GroupOfMotors(3, RobotMap.portMotor2Right, in_fast_mode);
    motorsLeft= new GroupOfMotors(2, RobotMap.portMotor2Left, in_fast_mode);
    try{
      gyro= new ADXRS450_Gyro();
      gyro.calibrate();
    }
    catch(Exception e){
      gyro= null;
    }
 //   isSpeedMode = true;
    motorsRight.SetReverseMode(true);
    isReverseMode = false;
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
  
public double GetAngle(){
  return gyro.getAngle();
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
    calibrateStartAngle = gyro.getAngle();
    inCalibration = true ;
  }
  
}

 public void SetValue(double left, double right){
   if(!isReverseMode){
    motorsRight.setValue(right);
    motorsLeft.setValue(left);
    System.out.println("reverse mode: false");
   }
   else{
    motorsRight.setValue(-left);
    motorsLeft.setValue(-right);
    System.out.println("reverse mode: true");
   }
 }

 public void SetValue(int left, int right){
  if(!isReverseMode){
    motorsRight.setValue(right);
    motorsLeft.setValue(left);
   }
   else{
    motorsRight.setValue(-left);
    motorsLeft.setValue(-right);
   }
 }

public void SetSpeedMode(boolean isSpeedMode){
  motorsLeft.SetSpeedMode(isSpeedMode);
  motorsRight.SetSpeedMode(isSpeedMode);
}

public void StopMotors(){
  motorsRight.StopMotors();
   motorsLeft.StopMotors();
}

public double GetDistance(){
  return(motorsLeft.GetPositionInMM()+motorsRight.GetPositionInMM())/2;
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
  shifter.set(DoubleSolenoid.Value.kForward);
  in_fast_mode = false;
  max_speed = GroupOfMotors.MAX_SPEED_SLOW / GroupOfMotors.PULSE_DIS;
  motorsLeft.SetSlow();
  motorsRight.SetSlow();
}

public void setFastMode() { // CloseShifter(){
  shifter.set(DoubleSolenoid.Value.kReverse);
  in_fast_mode = true;
  max_speed = GroupOfMotors.MAX_SPEED_FAST / GroupOfMotors.PULSE_DIS;
  motorsLeft.SetFast();
  motorsRight.SetFast();
}

public void ChangeShifter(boolean slow) { // boolean open){
  if(slow){
    setSlowMode();
  } else {
    setFastMode();
  }
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
}
