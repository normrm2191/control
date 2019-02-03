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

  static public int portMotor1Left=3;
  static public int portMotor2Left=2;
  static public int portMotor1Right=4;
  static public int portMotor2Right=1;
  static public int portLiftMotor = 0;
  static public int portSwitchMotor = 0;

  public static int portShifterForward = 0;
  public static int portShifterReverse = 1;

  public static int BUTTON_BOTTOM = 1;
  public static int BUTTON_MIDDLE = 2;
  public static int BUTTON_CHANGE_LIFT_DIR = 2;
  public static int BUTTON_UP=3;

  public static int BUTTON_SHIFTER = 1;
}
