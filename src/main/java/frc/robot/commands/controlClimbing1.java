/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class controlClimbing1 extends Command {

  double K_PF = 2.0/100; // 1 degree error = correct +/- 1% absolute power
  double K_PB = 2.8/100; // 1 degree error = correct +/- 1% absolute power
  double K_I = 0.0;
  double K_D = 0;
  double MAX_ERROR = 5;
  double MIN_POWER = 0.5;
  double MIN_MOVE_POWER = 0.0;
  double MAX_CHASSIS_SPEED = 1000;
  double FRONT_RATIO = 0.85;
  double basePitch = 0;
  boolean inRaiseBackJack = false;
  double sumError;
  double lastError;
  boolean useMaxBack = false;


  public controlClimbing1() {
    sumError = 0.0;
    lastError = 0;
  }

  @Override
  protected void initialize() {
    basePitch = Robot.chassis.getGyroPitch() + 1; // assume current pitch is horizontal
    // set the chassis data - slow, forward and disabled JS
    Robot.chassis.setSlowMode(); 
    Robot.chassis.SetReverseMode(false);
    Robot.chassis.SetCommand(this);
    // use this to keep the back jack raised at the end
    inRaiseBackJack = false; 
    sumError = 0;
    useMaxBack = false;
  }

  @Override
  protected void execute() {
    Robot.chassis.StopMotors();
    Robot.compressor.stop();
    // get the JS values and normalized
    double moveV = -Robot.driverInterface.joystickRight.getY();
    double jacksV = Robot.driverInterface.joystickRight.getRawAxis(3);
    double frontJackV = Robot.driverInterface.joystickLeft.getRawAxis(3);
    double backJackV = -Robot.driverInterface.joystickLeft.getY();
    moveV = Math.max(moveV, MIN_MOVE_POWER);
    jacksV = (jacksV - 1) / -2; // down is 0, up is 1 (joystick provide 1 at up, -1 at down)
    jacksV = jacksV * jacksV;
    frontJackV = (frontJackV - 1) / -2; // move from 0 to -1 (lift fron tp)
    // calculate the pitch correction
    double pitchError = Robot.chassis.getGyroPitch() - basePitch; // error < 0 - front is higher
    System.out.printf("Climb values - move=%f jacks=%f front=%f back=%f\n",
      moveV, jacksV, frontJackV, backJackV);
    sumError += pitchError;
    double i = sumError * K_I;
    double pf = K_PF * pitchError;
    double pb = K_PB * pitchError;
    double d = (lastError - pitchError) * K_D;
    lastError = pitchError;
    double bjack = jacksV - (pb + i + d);
    double fjack = jacksV*FRONT_RATIO + (pf + i + d);
    if(jacksV == 0) {
      bjack = 0;
      fjack = 0;
    } else {
      bjack = Math.max(bjack, MIN_POWER);
      fjack = Math.max(fjack, MIN_POWER);
    }
    if(pitchError > MAX_ERROR) {
      bjack = Math.min(MIN_POWER,bjack);
    } else if(pitchError < -MAX_ERROR) {
      fjack = Math.min(MIN_POWER,fjack);
    }
    // temp code
    bjack = 0.4;
    fjack = jacksV;
    // check if raising/raised front jack (fjack is less then min) - in this case - ignore correction
    if(frontJackV > 0.1) {
      fjack = -frontJackV;
      useMaxBack = true;
    }
    if(useMaxBack) {
      bjack = 0.8; // jacksV
    }
    // check if raising back jack
    if(backJackV < -0.1) { 
      inRaiseBackJack = true;
      bjack = backJackV;
    }
  // if raised back jack and the JS value is 0 - forced a back jack mainintain value
    if(inRaiseBackJack && backJackV > -0.1) {
      bjack = -0.2;
    }
    // show results
   // SmartDashboard.putNumber("Jack Front", fjack);
   // SmartDashboard.putNumber("Jack back", bjack);
    //SmartDashboard.putNumber("Jack picth", pitchError);
    System.out.println("move " + moveV + " b/f jack: " + bjack +  "/" + fjack + " pitch:" + pitchError);

    // put values
    Robot.climb.setValue_backJack(bjack);
    Robot.climb.setValue_frontJack(fjack);
    Robot.climb.setValue_moveMotor(moveV);
    Robot.chassis.SetSpeed(moveV*MAX_CHASSIS_SPEED,moveV*MAX_CHASSIS_SPEED);
  }

  @Override
  protected boolean isFinished() {
    return Robot.driverInterface.xbox.getXButtonPressed();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
		Robot.chassis.SetCommand(null);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
