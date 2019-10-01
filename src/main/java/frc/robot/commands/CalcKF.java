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

public class CalcKF extends Command {

  long startTime;
  double sumVelocityL;
  double sumVelocityR;
  double nVelocities;

  double power = 0.5;
  long maxTime = 1000;

  public CalcKF() {
  }

  @Override
  protected void initialize() {
    Robot.chassis.SetCommand(this);
//    Robot.chassis.inSpeedMode = false;
    Robot.chassis.motorsSetValue(power, power);
    System.out.println("gs - start");
    sumVelocityL = 0;
    sumVelocityL = 0;
    nVelocities = 0;
    startTime = System.currentTimeMillis();
  }

  @Override
  protected void execute() {
    Robot.chassis.motorsSetValue(power, power);
    if(System.currentTimeMillis() > startTime + 1000) {
      sumVelocityR -= Robot.chassis.motorsRight.motorVeocity();
      sumVelocityL += Robot.chassis.motorsLeft.motorVeocity();
      nVelocities += 1.0;
    }
    System.out.println("gs - exec - " + nVelocities);
  }

  @Override
  protected boolean isFinished() {
    return System.currentTimeMillis() > (startTime + 4000);
  }

  @Override
  protected void end() {
    Robot.chassis.inSpeedMode = true;
    Robot.chassis.StopMotors();
    double avgVl = sumVelocityL / nVelocities;
    double kfl = 1023.0 * power  / avgVl;
    double avgVr = sumVelocityR / nVelocities;
    double kfr = 1023.0 * power  / avgVr;
    SmartDashboard.putNumber("Avg Velocity L", avgVl);
    SmartDashboard.putNumber("Avg V K_F L", kfl);
    SmartDashboard.putNumber("Avg Velocity R", avgVr);
    SmartDashboard.putNumber("Avg V K_F R", kfr);
    Robot.chassis.SetCommand(null);
  }
}
