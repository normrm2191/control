/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class GoStraight extends Command {

  public double distance;
  public double speed;
  public double remain;
  public double gyroStartValue;
  public double sumError;
  public double lastError;
  public double maxSpeed;
  public static final double MAX_SPEED = 0.2;
  public int direction;
  public boolean stopAtEnd;
  public long maxTime;
  
  public static final double K_P = 1.0 / 100.0;
  public static final double K_I = K_P / 100;
  public static final double K_D = 0;
  public static final double MIN_SPEED = 0.2;
  public static final int FINAL_DISTANCE = 300;
  
  public GoStraight(double distance,double speed) {
    this(distance,speed,true,-1);    
  }
  public GoStraight(double distance,double speed,long maxTime){
    this(distance, speed,true,maxTime);
  }
  public GoStraight(double distance,double speed,boolean stopAtEnd){
    this(distance, speed,stopAtEnd,-1);
  }
  public GoStraight(double distance,double speed,boolean stopAtEnd,long maxTime){
    maxSpeed=speed;
    this.maxTime=maxTime;
    this.distance=distance;
    this.speed=speed;
    if(distance<0){
      direction=-1;
    }
    else{
      direction=1;
    }
    this.stopAtEnd = stopAtEnd;
  }
 
  private void SetDistance(){
    distance += Robot.chassis.GetDistance();
  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.chassis.SetCommand(this);
    gyroStartValue = Robot.chassis.GetAngle();
    if(maxTime > 0){
      maxTime += System.currentTimeMillis();
    }
    SetDistance();
    remaining();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    try{
      double leftSpeed = speed;
      double rightSpeed = speed;
      double corr = 0;
      remaining();
      if(remain < FINAL_DISTANCE && stopAtEnd){
        leftSpeed = leftSpeed * 0.9 * remain / FINAL_DISTANCE + 0.1;
        rightSpeed = rightSpeed * 0.9 * remain / FINAL_DISTANCE + 0.1;
      }
      else{
        double angle = Robot.chassis.GetAngle();
        double error = angle - gyroStartValue;
        sumError += error;
        double p = error * K_P;
        double i = sumError *K_I;
        double d = (lastError - error) * K_D;
        lastError = error;
        corr = 2* (p + i + d);
        leftSpeed = speed - corr;
        rightSpeed = speed + corr;
        if(leftSpeed < 0){
          leftSpeed = 0;
        }
        if(rightSpeed < 0){
          rightSpeed = 0;
        }
        if(Math.abs(leftSpeed) > maxSpeed){
          rightSpeed = (rightSpeed / leftSpeed) * maxSpeed;
          leftSpeed = maxSpeed;
        }
        if(Math.abs(rightSpeed) > maxSpeed){
          leftSpeed = (leftSpeed / rightSpeed) * maxSpeed;
          rightSpeed = maxSpeed;
        }
        if(direction > 0){
          Robot.chassis.motorsLeft.setValue(leftSpeed);
          Robot.chassis.motorsRight.setValue(rightSpeed);
        }
        else{
          Robot.chassis.motorsLeft.setValue(-rightSpeed);
          Robot.chassis.motorsRight.setValue(-leftSpeed);
        }
      }
      
    }
    catch(Exception e){
      System.out.println("error execute GoStraight");
    }
  }

protected void remaining(){
  remain = Math.abs(distance - Robot.chassis.GetDistance());
}

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(maxTime > 0 && System.currentTimeMillis() > maxTime){
      return true;
    }
    if(direction > 0){
      return (distance - Robot.chassis.GetDistance()) < 50;
    }else{
      return(distance-Robot.chassis.GetDistance())>-50;

    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("GoStright End:Encoder +"+(int)(Robot.chassis.motorsLeft.GetPositionInMM()) + 
    "/" + (int)(Robot.chassis.motorsRight.GetPositionInMM())+" angle= "+(int)(Robot.chassis.GetAngle())+ 
     " remain = "+ remain);
     if(stopAtEnd){
       Robot.chassis.SetValue(0, 0);
     }
     Robot.chassis.SetCommand(null);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
