package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="RubiksCubeMasterAuto")
public class RubiksCubeMasterAuto extends LinearOpMode {

    @Override
    public void runOpMode() {
        // --- 1. SCAN ---
        RubiksCubeColorDetector scanner = new RubiksCubeColorDetector(); // Your OpMode, so use it directly!
        // Run scanner's logic; after scan, cubeState and faceNames are filled
        // (You may need to refactor scanner to allow calling scan logic from here, or
        // simply run the full scanner as TeleOp, then manually start this Auto mode.)

        // For demonstration, let's assume after scanning:
        String[][] cubeState = scanner.cubeState;
        String[] faceNames = scanner.faceNames;

        // --- 2. CONVERT ---
        String cubeString = CubeStringConverter.cubeStateToSolverString(cubeState, faceNames);
        telemetry.addData("Solver cube string", cubeString);
        telemetry.update();

        // --- 3. SOLVE ---
        String solution = JaapSolver.solve(cubeString);
        telemetry.addData("Solution", solution);
        telemetry.update();

        // --- 4. EXECUTE ---
        DcMotorEx baseMotor = hardwareMap.get(DcMotorEx.class, "baseMotor");
        DcMotorEx flipMotor = hardwareMap.get(DcMotorEx.class, "flipMotor");
        RunMotors runner = new RunMotors();
        runner.baseMotor = baseMotor;
        runner.flipMotor = flipMotor;
        runner.telemetry = telemetry;
        runner.runMoveSequence(solution);

        telemetry.addLine("Done!");
        telemetry.update();
        sleep(5000);
    }
}


