/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.GamepadBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.CalcCameraData;
import frc.robot.commands.ReleaseJacks;
import frc.robot.commands.controlClimbing1;

/**
 * Add your docs here.
 */
public class DriverInterface {

    public Joystick joystickLeft;
    public Joystick joystickRight;
    public XboxController xbox; 
    
    // Right and Lefgt buttons
    public static int BUTTON_SHIFTER = 1;   // trigger button

    // Right Buttons
    public static int CALC_KF = 4;  // on JS buttom right button
    public static int CHANGE_ARM = 5;  // on JS top left button
    public static int PUT_HATCH_BY_VISION = 8;
    public static int TAKE_HATCH_BY_VISION = 7;
    public static int ARM_FRONT = 9;
    public static int ARM_UP = 10;
    public static int ARM_BACK = 11;

    // Left Buttons
    public static int REVERSE_MODE = 9;   // big center button
    public static int FORWARD_MODE = 11;  // big front button
    public static int RESET_ENCODER = 7;  // big back button
    public static int RESET_ARM_ENCODER = 8;  // small back button


    //xbox axises
    public static int ARM = 5;
    

    //xbox button
    public static int START_CLIMB = 2;  // B button
    public static int STOP_CLIMB = 3;  // X button
    public static int RELEASE_JACK = 1;  // A button
    

    // buttons
    JoystickButton climbButton;
    public JoystickButton stopClimbButton;
  //  JoystickButton calcKF;
    JoystickButton releaseJack;

    public JoystickButton putHatchByVision;
    public JoystickButton takeHatchByVision;

    public JoystickButton armFront;
    public JoystickButton armUp;
    public JoystickButton armBack;
    
    public controlClimbing1 climbCmd;
    
    public DriverInterface(){
      joystickRight=new Joystick(1);
      joystickLeft=new Joystick(2);
      xbox= new XboxController(0);
      climbCmd = new controlClimbing1();
      climbButton = new JoystickButton(xbox, START_CLIMB);
      stopClimbButton = new JoystickButton(xbox, STOP_CLIMB);
      climbButton.whenPressed(climbCmd);
      stopClimbButton.cancelWhenPressed(climbCmd);
//      calcKF = new JoystickButton(joystickRight, CALC_KF);
//      calcKF.whenPressed(new CalcKF());
      releaseJack = new JoystickButton(xbox, RELEASE_JACK);
      releaseJack.whenPressed(new ReleaseJacks());
      putHatchByVision = new JoystickButton(joystickRight, PUT_HATCH_BY_VISION);
      putHatchByVision.whenPressed(new CalcCameraData(true));
      takeHatchByVision = new JoystickButton(joystickRight, TAKE_HATCH_BY_VISION);
      takeHatchByVision.whenPressed(new CalcCameraData(false));
    //  SmartDashboard.putData(new GoStraight(500, 1000));
    //  SmartDashboard.putData(new TurnByDegrees(30, 90));
    //  SmartDashboard.putData(new TurnByR(30, 90, 500));
/*    armFront = new JoystickButton(joystickRight, ARM_FRONT);
    armBack = new JoystickButton(joystickRight, ARM_BACK);
    armUp = new JoystickButton(joystickRight, ARM_UP);
    armFront.whenPressed(new MoveArm(ArmPosition.FRONT));
    armUp.whenPressed(new MoveArm(ArmPosition.UP));
    armBack.whenPressed(new MoveArm(ArmPosition.BACK));
    */
    
    }
}

