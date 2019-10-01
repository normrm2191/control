/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ControlHatchByVision extends CommandGroup {
  /**
   * Add your docs here.
   */

   public static final double REDUDE_FIRTS_DIS = 200;
   public static final double SPEED = 700;
   public static final double TURN_SPEED = 90;
   public static final double TURN_RADIUS = 100;

   public ControlHatchByVision(double angle, double length, double lengthR, double angleLine, 
  double lengthFinal, boolean isPush) {
//    addSequential(new TurnByDegrees(angle, 90));
    addSequential(new GoStraight(length-REDUDE_FIRTS_DIS, SPEED, false, angle));
    addSequential(new TurnByR(angleLine, TURN_SPEED, TURN_RADIUS, false, true));
    addSequential(new GoStraight(lengthFinal, SPEED, isPush, angleLine));
    /*
    if(isPush){
      addSequential(new DropHatch());
    }
    else{
      addSequential(new GoStraight(500, 400, true, 1000));
    }
    addSequential(new ChangeRobotDirection());
    addSequential(new GoStraight(500, 400));
*/
  }
}
