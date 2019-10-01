/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.VectorsCalculate;
import frc.robot.Utils.Vector;
import frc.robot.Utils.VectorPoint;
import frc.robot.Vision.VisionData;

public class CalcCameraWhileMoving extends Command {

  boolean isPush;
  Vector  lastPos = null;
  Vector  last2Pos = null;
  double maxSpeed;
  double lastAngle;
  double lastDistance;
  int cycle;


  public CalcCameraWhileMoving(boolean isPush) {
    this.isPush = isPush;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // set Robot to drive slow
    Robot.chassis.setSlowMode();
    maxSpeed = Robot.chassis.max_speed;
    Robot.chassis.max_speed = 700;
    // reset last data
    lastPos = null;
    last2Pos = null;
    lastAngle = Robot.chassis.GetAngle();
    lastDistance = Robot.chassis.GetDistance();
    cycle = -1;

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    cycle++;
    if(cycle > 0) {
      double d = Robot.chassis.GetDistance();
      double a = Robot.chassis.GetAngle();
      last2Pos = lastPos;
      if(lastPos == null) {
        lastPos = new Vector();
      }
      lastPos.angle = (a - lastAngle)/2;
      lastPos.length = d - lastDistance;
      lastDistance = d;
      lastAngle = a;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(last2Pos == null || lastPos == null) {
      return false;
    }
    if (Robot.chassis.isReverseMode()) {
      return VisionData.backData.found();
    } else {
      return VisionData.frontData.found();
    }
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    VectorsCalculate vectors;
    VectorPoint v1;
    VectorPoint v2;
    VectorPoint v1p;
    VectorPoint v2p;
    VectorPoint vp;
    VisionData vd = Robot.chassis.isReverseMode()? VisionData.backData:VisionData.frontData;
      v1 = new VectorPoint(new Vector(vd.p1.a, vd.p2.d));
      v2 = new VectorPoint(new Vector(vd.p2.a, vd.p2.d));
      last2Pos.angle = +lastPos.angle;
      vp = new VectorPoint(lastPos);
      vp = VectorPoint.add(vp, new VectorPoint(last2Pos));
      v1p = VectorPoint.sub(v1,vp);
      v2p = VectorPoint.sub(v2,vp);
      System.out.printf(" lastP= %s\n last2=%s\n vp=%s\n v1=%s\n v2=%s\n v1p=%s\n v2p=%s\n",
                      lastPos, last2Pos, vp, v1, v2, v1p, v2p);
      vectors = new VectorsCalculate(v1p.GetAngleOfVector(), 
                                     v1p.GetLengthOfVector(),
                                     v2p.GetAngleOfVector(), 
                                     v2p.GetLengthOfVector());
      // Robot.hatchPanelsSystem.SetPosituon(HatchPanelsSystem.CHANGE_DIR_MOVE);

    // -------------------------------------------------------------------------------
    /*
     * vectors = new VectorsCalculate(SmartDashboard.getNumber("angle - start", 0),
     * SmartDashboard.getNumber("dis - start", 0),
     * SmartDashboard.getNumber("angle - end", 0),
     * SmartDashboard.getNumber("dis - end", 0));
     */

    System.out.println(vectors.toString());
    System.out.println(vectors.Get_First_vector().toString());
    System.out.println(vectors.Get_Final_Vector().toString());
    System.out.println(vectors.Get_Line_vector().toString());
    Robot.chassis.resetGyro();
    Robot.chassis.resetEncs();
    Command c = new ControlHatchByVision(vectors.Get_First_vector().angle, // angle to turn
                                        vectors.Get_First_vector().length, // distance to turn point
                                        250, // radius - not used
                                        vectors.Get_Line_vector().angle, // turn angle
                                        150, // distnace to drive
                                        isPush); // end action (push/drop)
    c.start();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
