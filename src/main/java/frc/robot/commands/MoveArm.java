package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchPanelsSystem;

public class MoveArm extends Command {

    public static final double UP_POWER = 0.5;
    public static final double DOWN_POWER = 0.25;
    public static final int DOWN_T = 10;
    public static final int UP_T = 20;
    
    int cycles = 0;
    double upPOwer = 0;
    double downPower = 0;
    HatchPanelsSystem.ArmPosition target;


    public MoveArm(HatchPanelsSystem.ArmPosition target) {
      this.target = target;
    }
  
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
      cycles = 0;
      switch(Robot.hatchPanelsSystem.armPosition) {
        case BACK:
          switch(target) {
            case BACK:
              cycles = 0;
              upPOwer = 0;
              downPower = 0;
              break;
            case FRONT:
              cycles = UP_T + DOWN_T;
              upPOwer = -UP_POWER;
              downPower = -DOWN_POWER;
              break;
          case UP:
              cycles = UP_T;
              upPOwer = -UP_POWER;
              downPower = 0;
              break;
        }
        break;
        case FRONT:
        switch(target) {
          case BACK:
            cycles = UP_T + DOWN_T;
            upPOwer = UP_POWER;
            downPower = DOWN_POWER;
            break;
          case FRONT:
            cycles = 0;
            upPOwer = 0;
            downPower = 0;
          break;
        case UP:
            cycles = UP_T;
            upPOwer = UP_POWER;
            downPower = 0;
            break;
      }
      break;
      case UP:
      switch(target) {
        case BACK:
          cycles = DOWN_T;
          upPOwer = 0;
          downPower = DOWN_POWER;
          break;
        case FRONT:
          cycles = DOWN_T;
          upPOwer = 0;
          downPower = -DOWN_POWER;
        break;
      case UP:
        cycles = 0;
        upPOwer = 0;
        downPower = 0;
        break;
    }
    break;
    }
    System.out.println("Move arm from " + Robot.hatchPanelsSystem.armPosition + 
          " to " + target + " up=" + upPOwer + " down=" + downPower + " cycles=" + cycles);
   }
  
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      if(upPOwer != 0) {
        Robot.hatchPanelsSystem.SetPower(upPOwer);
        if(cycles == DOWN_T + 1) {
          upPOwer = 0; // last cycle
        }
      } else if(downPower != 0) {
        Robot.hatchPanelsSystem.SetPower(downPower);
      }
      cycles--;
    }
  
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
      return cycles <= 0;
    }
  
    // Called once after isFinished returns true
    @Override
    protected void end() {
      Robot.hatchPanelsSystem.SetPower(0);
      Robot.hatchPanelsSystem.armPosition = target;
    }
  
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
  }
  