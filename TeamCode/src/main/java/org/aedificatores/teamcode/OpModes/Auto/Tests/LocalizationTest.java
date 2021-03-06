package org.aedificatores.teamcode.OpModes.Auto.Tests;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.aedificatores.teamcode.Mechanisms.Drivetrains.GandalfMecanum;
import org.aedificatores.teamcode.Mechanisms.Drivetrains.SawronMecanum;
import org.aedificatores.teamcode.Universal.OpModeGroups;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 *
 * copied from roadrunner quickstart
 */
@Config
@TeleOp(name = "LocalizationTest", group = OpModeGroups.UNIVERSAL)
public class LocalizationTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        GandalfMecanum drive = new GandalfMecanum(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();
            Pose2d poseEstimate = drive.getPoseEstimate();
            double odomHeading = poseEstimate.getHeading();
            double imuHeading = drive.getRawExternalHeading();

            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", odomHeading);
            telemetry.addData("imu", imuHeading);
            telemetry.addData("imuHeadingError", odomHeading - imuHeading);
            if (odomHeading != 0.0) {
                telemetry.addData("imuHeadingErrorRatio", (odomHeading/imuHeading));
            } else {
                telemetry.addData("imuHeadingErrorRatio",0);
            }

            telemetry.update();
        }
    }
}
