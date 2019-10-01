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
  public int direction;
  public boolean stopAtEnd;
  public long maxTime;
  public boolean isAbsAngle;
  public double absAngle;
  
  public static final double K_P = 1.0 / 50.0;
  public static final double K_I = K_P / 100;
  public static final double K_D = 0;
  public static final int FINAL_DISTANCE = 500;
  public static final double MIN_SPEED = 300;
  
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
    maxSpeed=speed * 2;
    this.maxTime=maxTime;
    this.distance=distance;
    this.speed=speed;
    this.isAbsAngle = false;
    if(distance<0){
      direction=-1;
    }
    else{
      direction=1;
    }
    this.stopAtEnd = stopAtEnd;
  }

  public GoStraight(double distance,double speed,boolean stopAtEnd, double absAngle)
  {
    this(distance,speed,stopAtEnd,-1);
    isAbsAngle = true;
    this.absAngle = absAngle;
  }
  
  public GoStraight(double distance,double speed,boolean stopAtEnd,long maxTime, double absAngle){
    this(distance,speed,stopAtEnd,maxTime);
    isAbsAngle = true;
    this.absAngle = absAngle;
  }
 
  private void SetDistance(){
    distance += Robot.chassis.GetDistance();
  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.chassis.SetCommand(this);
    if(isAbsAngle) {
      gyroStartValue = absAngle;
    } else {
      gyroStartValue = Robot.chassis.GetAngle();
    }
    if(maxTime > 0){
      maxTime += System.currentTimeMillis();
    }
    SetDistance();
    remaining();
    System.out.println("remaining = " + remain);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    try{
      double leftSpeed = speed;
      double rightSpeed = speed;
      double corr = 0;
      double local_speed = speed;
      remaining();
      if(remain < FINAL_DISTANCE && stopAtEnd){
        local_speed = speed  * remain / FINAL_DISTANCE;
      }
      if(local_speed < MIN_SPEED) {
        local_speed = MIN_SPEED;
      }
        double angle = Robot.chassis.GetAngle();
        double error = angle - gyroStartValue;
        sumError += error;
        double p = error * K_P;
        double i = sumError *K_I;
        double d = (lastError - error) * K_D;
        lastError = error;
        corr = 2* (p + i + d);
        leftSpeed = local_speed * (1 - corr);
        rightSpeed = local_speed * (1 + corr);
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
        System.out.println(
                  "r enc = " + Robot.chassis.getRightDistance() +
                  "l enc = " + Robot.chassis.getLeftDistance() +
                  "remaining = " + remain + 
                  "/ angle = " + angle + 
                  "/ local speed = " + local_speed +
                  "/ error = " + error + 
                  "/ corr = " + corr + 
                  "/ left speed = " + leftSpeed +
                  "/ right speed = " + rightSpeed );
        if(direction > 0){
          Robot.chassis.SetSpeed(leftSpeed, rightSpeed);
        }
        else{
          Robot.chassis.SetSpeed(-rightSpeed, -leftSpeed);
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
      return (distance - Robot.chassis.GetDistance()) < 5;
    }else{
      return(distance-Robot.chassis.GetDistance())>-5;

    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("GoStright End:Encoder +"+(int)(Robot.chassis.getLeftDistance()) + 
    "/" + (int)(Robot.chassis.getRightDistance())+" angle= "+(int)(Robot.chassis.GetAngle())+ 
     " remain = "+ remain);
     if(stopAtEnd){
       Robot.chassis.SetSpeed(0 , 0);
     }
     Robot.chassis.SetCommand(null);;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
