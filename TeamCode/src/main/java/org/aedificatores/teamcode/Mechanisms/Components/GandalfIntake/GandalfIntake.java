package org.aedificatores.teamcode.Mechanisms.Components.GandalfIntake;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.aedificatores.teamcode.Mechanisms.Robots.GandalfBotConfig;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class GandalfIntake {

    enum IntakeState { FOREWARD, REVERSE, OFF}
    private IntakeState state = IntakeState.OFF;

    public DcMotorEx actuator;
    public GandalfIntakeLift lift;
    public GandalfTransfer transfer;

    private static final double INTAKE_POW = 1.0;
    private static final double TOP_DIST_THRESHOLD = 9.0;
    private static final double BOTTOM_DIST_THRESHOLD = 6.0;
    private static final double INTAKE_DIST_THRESHOLD = 1.0;

    private DistanceSensor topSensor;
    private DistanceSensor bottomSensor;
    private DistanceSensor intakeSensor;

    public GandalfIntake(HardwareMap map, double angle, GandalfIntakeLift.Mode m) {
        actuator = map.get(DcMotorEx.class, GandalfBotConfig.INTAKE.MOT);
        lift = new GandalfIntakeLift(map, angle, m);
        transfer = new GandalfTransfer(map);

        topSensor = map.get(DistanceSensor.class, GandalfBotConfig.INTAKE.TOP_DETECT);
        bottomSensor = map.get(DistanceSensor.class, GandalfBotConfig.INTAKE.BOTTOM_DETECT);
        intakeSensor = map.get(DistanceSensor.class, GandalfBotConfig.INTAKE.INTAKE_DETECT);
    }

    public void update() {
        lift.update();
        transfer.update();
    }

    public boolean ringInIntake() {
        return intakeSensor.getDistance(DistanceUnit.CM) < INTAKE_DIST_THRESHOLD;
    }

    public boolean ringAtTopTransfer() {
        return bottomSensor.getDistance(DistanceUnit.CM) < BOTTOM_DIST_THRESHOLD || topSensor.getDistance(DistanceUnit.CM) < TOP_DIST_THRESHOLD;
    }

    public void toggleIntake() {
        if (state != IntakeState.FOREWARD) {
            forward();
        } else {
            off();
        }
    }

    public void toggleOuttake() {
        if (state != IntakeState.REVERSE) {
            backward();
        } else {
            off();
        }
    }

    public void forward() {
        state = IntakeState.FOREWARD;
        actuator.setPower(INTAKE_POW);
    }

    public void backward() {
        state = IntakeState.REVERSE;
        actuator.setPower(-INTAKE_POW);
    }

    public void off() {
        state = IntakeState.OFF;
        actuator.setPower(0.0);
    }
}
