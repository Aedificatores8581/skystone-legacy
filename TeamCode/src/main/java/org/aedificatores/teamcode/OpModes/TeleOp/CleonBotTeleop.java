package org.aedificatores.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.aedificatores.teamcode.Mechanisms.Robots.CleonBot;
import org.aedificatores.teamcode.Universal.Math.Vector2;
import org.json.JSONException;

import java.io.IOException;

public class CleonBotTeleop extends OpMode {
    CleonBot bot;
    Vector2 leftStick, rightStick;

    @Override
    public void init() {
        try {
            bot = new CleonBot(hardwareMap, false);
        } catch (IOException | JSONException e) {
            telemetry.addLine("Exception Caught: " + e.getMessage());
            telemetry.addLine("\n\nStopping OpMode");
            requestOpModeStop();
        }

        leftStick = new Vector2();
        rightStick = new Vector2();
    }

    @Override
    public void loop() {
        bot.intake.setIntakePower(gamepad1.right_trigger - gamepad1.left_trigger);

        leftStick.setComponents(gamepad1.left_stick_x, gamepad1.left_stick_y);
        rightStick.setComponents(gamepad1.right_stick_x, gamepad1.right_stick_y);
        bot.drivetrain.setVelocityBasedOnGamePad(leftStick, rightStick);
        bot.drivetrain.refreshMotors();
    }
}
