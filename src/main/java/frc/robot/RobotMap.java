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
  public static int portClimbMotorFront1 = 5;
  public static int portClimbMotorFront2 = 8;
  public static int portClimbMotorBack = 6;
  public static int portClimbMoveMotor = 7;
  public static int portClimbBuchnaFwd = 5; // 4;
  public static int portClimbBuchnaBwd = 6; // 5;

  static public int portArmMotor = 9;
  public static int portHatchPanelBuchnaFwd = 4; // 0
  public static int portHatchPanelBuchnaBwd = 3;  // 1
  public static int portPCM = 19;

  public static int portShifterForward = 1; // 2;
  public static int portShifterReverse = 2; // 3;

  public static final int CAN_GYRO_PORT = 21;
  public static final boolean USE_CAN_GYRO = true;
}
