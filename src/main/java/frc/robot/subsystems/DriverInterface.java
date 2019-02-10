/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.OpenShifter;

/**
 * Add your docs here.
 */
public class DriverInterface extends Subsystem {
 

    public Joystick joystickLeft;
    public Joystick joystickRight;
    public Joystick Bottom_Button ;
    public Joystick Botton_Forward ;
    public XboxController xbox;
    public boolean isSpeedMode;
    
    
    public DriverInterface(){
      joystickRight=new Joystick(2);
      joystickLeft=new Joystick(1);
      xbox= new XboxController(0);
      isSpeedMode= true;
    }

    public void Reset(){
      Robot.chassis.motorsLeft.ResetEnc();
      Robot.chassis.motorsRight.ResetEnc();
    }
    
    public void UpdateStatus(){
      if(joystickRight.getRawButtonPressed(RobotMap.BUTTON_SHIFTER)){
        //Robot.chassis.ChangeShifter(true);
        System.out.println("open buchna");
        Robot.chassis.setSlowMode();
      }
      else if(joystickLeft.getRawButtonPressed(RobotMap.BUTTON_SHIFTER)){
        //Robot.chassis.ChangeShifter(false);
        System.out.println("close buchna");
        Robot.chassis.setFastMode();
      }
      else{
        Robot.chassis.offShifter();
      }

    if(joystickLeft.getRawButtonPressed(7)){  //set reverse mode 
      Robot.chassis.SetReverseMode(true);
    }
    else if(joystickLeft.getRawButtonPressed(8)){   //cancel reverse mode
      Robot.chassis.SetReverseMode(false);
    }
    if(joystickLeft.getRawButtonPressed(9)){ //go straight

    }

  }
    @Override
    public void initDefaultCommand() {}
    }   
