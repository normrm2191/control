/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Utils.CameraHandler;
import frc.robot.Vision.VisionServer;
import frc.robot.commands.ControlArm;
import frc.robot.commands.DriveByJoysticArcade;
import frc.robot.commands.DriveByJoystickCommand;
import frc.robot.commands.LockJacks;
import frc.robot.commands.TurnByR;
import frc.robot.commands.UnDropHatch;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.DriverInterface;
import frc.robot.subsystems.HatchPanelsSystem;
import frc.robot.subsystems.Climb;;

public class Robot extends TimedRobot {
  public static Chassis chassis;
  public static DriverInterface driverInterface;
  public static Robot robot = null;
  public static HatchPanelsSystem hatchPanelsSystem;
  public static Compressor compressor;
  public static Climb climb;
  public VisionServer visionServer;

  public static Command autoCommand = null;
  public static Command teleopCommand = null;

  SendableChooser<Command> driveChooser = new SendableChooser<>();
  ControlArm controlArm;
  CameraHandler camera;

  @Override
  public void robotInit() {
    visionServer = new VisionServer();
    robot = this;
    chassis= new Chassis();
    driverInterface= new DriverInterface();
    driveChooser.setDefaultOption("Tank", new DriveByJoystickCommand());
    driveChooser.addOption("Arcade", new DriveByJoysticArcade());
    driveChooser.setName("Drive Mode");
 //   SmartDashboard.putData(driveChooser);
    compressor = new Compressor(RobotMap.portPCM);
    compressor.start();
    hatchPanelsSystem = new HatchPanelsSystem();
//    SmartDashboard.putData(new CalcKF());
    climb = new Climb();
    controlArm = new ControlArm();
    camera = new CameraHandler();
    SmartDashboard.putNumber("dis - start", 0);
    SmartDashboard.putNumber("angle - start", 0);
    SmartDashboard.putNumber("dis - end", 0);
    SmartDashboard.putNumber("angle - end", 0);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    initEnabled();
    chassis.setSlowMode();
    autoCommand = new TurnByR(90, 60, 500);
    if (autoCommand != null) {
      autoCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    enabledPeriodic();
  }

  @Override
  public void teleopInit() {
    initEnabled();
    teleopCommand = driveChooser.getSelected();
    System.out.println("Starting " + teleopCommand);
    chassis.setSlowMode();
    controlArm.start();
    teleopCommand.start();
   
  }


  @Override
  public void teleopPeriodic() {
    enabledPeriodic();  
  }

  @Override
  public void testPeriodic() {
  }

  private void initEnabled() {
    if (autoCommand != null) {
      autoCommand.cancel();
    }
    if(teleopCommand != null && teleopCommand.isRunning()) {
      teleopCommand.close();
    }
    if(controlArm.isRunning()) {
      controlArm.close();
    }
    if(driverInterface.climbCmd != null && driverInterface.climbCmd.isRunning()) {
      driverInterface.climbCmd.close();
    }
    chassis.resetEncs();
    compressor.start();
    Command c = new LockJacks();
    c.start();
    c = new UnDropHatch();
    c.start(); 
  }

  private void enabledPeriodic() {
    chassis.UpdateStatus();
    hatchPanelsSystem.UpdateStatus();
    Scheduler.getInstance().run();
  }
}
