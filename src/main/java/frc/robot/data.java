/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class data implements Sendable {
    private double data1,data2;
    private String name;

    public data(String name){
        data1 = 3.5432;
        data2 = 7.2345;
        this.name = name;
    }

    public void setData1(double data1){
        this.data1 = data1;
        SmartDashboard.putData(this);
    }

    public void setData2(double data2){
        this.data2 = data2;
    }

    public double getData1(){
        return data1;
    }
    
    public double getData2(){
        return data2;
    }

    public double[] getDatas(){
        return new double[] {data1,data2};
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSubsystem() {
        return null;
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleArrayProperty("data", () -> getDatas(), (double[] datas) -> {setData1(datas[0]); setData2(datas[1]);});
        builder.addDoubleProperty("data2", () -> getData2() ,(double getD) -> setData2(getD));
        builder.addDoubleProperty("data1", () -> getData1() ,(double getD) -> setData1(getD));
    }
}
