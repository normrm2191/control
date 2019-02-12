/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GroupOfMotors implements Sendable {
    
    public TalonSRX motor1;
  //  public TalonSRX motor2;
    public double reverse;
    public boolean isSpeedMode;

    public static final double K_P = 1.0 / 5.0;
    public static final double K_I = K_P / 50;
    public static final double K_D = 0;
    public static final double MAX_POWER_FOR_MAX_SPEED_SLOW = 1023 * 0.5; // 1/3 power
    public static final double MAX_POWER_FOR_MAX_SPEED_FAST = 1023 * 0.8; // 1/2 power
    public static final double PULSE_DIS=0.116;
    public static final double SPEED_TO_TALON_SPEED = 0.1 / PULSE_DIS;
    public static final double MAX_SPEED_SLOW =2000;
    public static final double MAX_SPEED_FAST =7000;
    public static final double K_F_FAST = MAX_POWER_FOR_MAX_SPEED_FAST / (MAX_SPEED_FAST * SPEED_TO_TALON_SPEED);
    public static final double K_F_SLOW = MAX_POWER_FOR_MAX_SPEED_SLOW / (MAX_SPEED_SLOW * SPEED_TO_TALON_SPEED);
    public double baseEncoder=0;

    public String name;


    public GroupOfMotors(int port1, int port2, boolean in_fast_mode, String name) {
        this.name = name;
        System.out.println("Starting " + name + " motor on port " + port1);
        motor1= new TalonSRX(port1);
    //    motor2= new TalonSRX(port2);
    //    motor2.follow(motor1);
        // set PID
        motor1.config_kP(0, K_P,0);
    //    motor2.config_kP(0, K_P,0);
        motor1.config_kI(0, K_I,0);
    //    motor2.config_kI(0, K_I,0);
        motor1.config_kD(0, K_D,0);
    //    motor2.config_kD(0, K_D,0);
        motor1.configClosedloopRamp(0.5);
        motor1.configContinuousCurrentLimit(40);
        motor1.configPeakCurrentDuration(100);
        motor1.enableCurrentLimit(true);
        /*motor2.configClosedloopRamp(0.5);
        motor2.configContinuousCurrentLimit(40);
        motor2.configPeakCurrentDuration(100);
        motor2.enableCurrentLimit(true);*/
        isSpeedMode = true;
        reverse = 1;
        if(in_fast_mode) {
            ConfigKF(K_F_FAST);
        } else {
            ConfigKF(K_F_SLOW);
        }
        SmartDashboard.putData(this);
    }

    public void ConfigKP(double k_p){
        motor1.config_kP(0, k_p, 0);
    }

    public void ConfigKI(double k_i){
        motor1.config_kP(0, k_i, 0);
    }

    public void ConfigKD(double k_d){
        motor1.config_kD(0, k_d, 0);
    }
    public void ConfigKF(double k_f){
        motor1.config_kF(0, k_f, 0);
    }

    public void _setValue(double value){
        if(Robot.driverInterface.isSpeedMode){
            _setSpeed(value * Robot.chassis.max_speed);
        }else{
            motor1.set(ControlMode.PercentOutput, reverse * value);
        }
    }

    public void _setSpeed(double speed){
        System.out.println("speed = " + reverse * speed * SPEED_TO_TALON_SPEED);
        motor1.set(ControlMode.Velocity, reverse * speed * SPEED_TO_TALON_SPEED);
    }

    public void StopMotors(){
        _setValue(0);
    }

    public void ResetEnc(){
        baseEncoder=motor1.getSelectedSensorPosition();
    }
    public double GetAbsPosition(){
        return motor1.getSelectedSensorPosition();
    }
    public double _GetPosition(){
        int reverseMode = Robot.chassis.isReverseMode ? -1 : 1;
        return reverse * (GetAbsPosition()-baseEncoder);
    }
    public double _GetPositionInMM(){
        return _GetPosition()*PULSE_DIS;
    }
    public void SetReverseMode(Boolean reverseMoode){
        reverse= reverseMoode? -1 : 1;
    }
    public void _SetSpeedMode(boolean isSpeedMode)
    {
        this.isSpeedMode = isSpeedMode;
    }
    public void SetSlow(){
        ConfigKF(K_F_SLOW);
    }
    public void SetFast(){
        ConfigKF(K_F_FAST);
    }

    public double motorVeocity() {
        return motor1.getSelectedSensorVelocity();
    }
    public double motorPIDTarget() {
        return motor1.getClosedLoopTarget();
    }
    public double motorPIDError() {
        return motor1.getClosedLoopError();
    }
    public double motorPowerPercent() {
        return motor1.getMotorOutputPercent();
    }
    public double motorOutputVolt() {
        return motor1.getMotorOutputVoltage();
    }
    public double motorBusVolt() {
        return motor1.getBusVoltage();
    }
    public double motorCurrent() {
        return motor1.getOutputCurrent();
    }
    public void PrintSpeed(){
        double v = motorVeocity();
        double t = motorPIDTarget();
        double e = motorPIDError();
        double p = motorPowerPercent();
        double ov = motorOutputVolt();
        double bv = motorBusVolt();
        double c = motorCurrent();
        System.out.printf("v=%.1f pwr=%.2f ovlt=%.1f bvlt=%.1f %%=%.0f amp=%.2f tgt=%.1f err=%.1f\n",
                v, p, ov, bv, 100.0 * ov / bv, c, t, e);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "Chassis";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty(name + " velocity", this::motorVeocity, null);
        builder.addDoubleProperty(name + " target", this::motorPIDTarget, null);
        builder.addDoubleProperty(name + " error", this::motorPIDError, null);
        builder.addDoubleProperty(name + " power", this::motorPowerPercent, null);
        builder.addDoubleProperty(name + " volt", this::motorOutputVolt, null);
        builder.addDoubleProperty(name + " bus volt", this::motorBusVolt, null);
        builder.addDoubleProperty(name + " amps", this::motorCurrent, null);
	}
}
