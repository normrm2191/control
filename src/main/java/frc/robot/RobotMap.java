/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  //chassis
  public static int portMotor1Left=1;
  public static int portMotor2Left=2;
  public static int portMotor1Right=3;
  public static int portMotor2Right=4;

  //climb
  public static int portClimbMotorFront = 5;
  public static int portClimbMotorBack = 6;
  public static int portClimbMoveMotor = 7;

  static public int portArmMotor = 8;

  public static int portShifterForward = 0;
  public static int portShifterReverse = 1;

  public static int BUTTON_BOTTOM = 1;
  public static int BUTTON_MIDDLE = 2;
  public static int BUTTON_CHANGE_LIFT_DIR = 2;
  public static int BUTTON_UP=3;

  public static int BUTTON_SHIFTER = 1;

  public static final int CAN_GYRO_PORT = 21;
  public static final boolean USE_CAN_GYRO = true;
}
