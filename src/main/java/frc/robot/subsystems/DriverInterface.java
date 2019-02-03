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

/**
 * Add your docs here.
 */
public class DriverInterface extends Subsystem {
 

    public Joystick joystickLeft;
    public Joystick joystickRight;
    public XboxController xbox;
    public boolean isSpeedMode;
    
    public DriverInterface(){
      joystickLeft=new Joystick(1);
      joystickRight=new Joystick(2);
      xbox= new XboxController(0);
      isSpeedMode= true;
    }

    public void ChangeMode()
    {
        isSpeedMode= !isSpeedMode;
    }
    public void Reset(){
      Robot.chassis.motorsLeft.ResetEnc();
      Robot.chassis.motorsRight.ResetEnc();
    }
public void UpdateStatus(){
  
}

    @Override
    public void initDefaultCommand() {}
}
