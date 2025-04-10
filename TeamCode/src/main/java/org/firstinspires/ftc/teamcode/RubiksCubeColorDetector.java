package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "Cube Scanner with Movement", group = "Concept")
public class RubiksCubeColorDetector extends LinearOpMode {

    // Motor hardware
    private DcMotorEx baseMotor;
    private DcMotorEx flipMotor;
    private final double BASE_MOTOR_ROTATION = 146.5; // Encoder ticks per face turn

    // Scanning regions (9 squares per face)
    private static final ImageRegion[] REGIONS = {
            ImageRegion.asUnityCenterCoordinates(-0.9f, 0.9f, -0.5f, 0.5f),   // Top-left
            ImageRegion.asUnityCenterCoordinates(-0.2f, 0.9f, 0.2f, 0.5f),    // Top-center
            ImageRegion.asUnityCenterCoordinates(0.5f, 0.9f, 0.9f, 0.5f),     // Top-right

            ImageRegion.asUnityCenterCoordinates(-0.9f, 0.3f, -0.5f, -0.3f),   // Middle-left
            ImageRegion.asUnityCenterCoordinates(-0.2f, 0.3f, 0.2f, -0.3f),    // Center
            ImageRegion.asUnityCenterCoordinates(0.5f, 0.3f, 0.9f, -0.3f),     // Middle-right

            ImageRegion.asUnityCenterCoordinates(-0.9f, -0.5f, -0.5f, -0.9f),  // Bottom-left
            ImageRegion.asUnityCenterCoordinates(-0.2f, -0.5f, 0.2f, -0.9f),   // Bottom-center
            ImageRegion.asUnityCenterCoordinates(0.5f, -0.5f, 0.9f, -0.9f)     // Bottom-right
    };


    private String[][] cubeState = new String[6][9];
    private String[] faceNames = {"FRONT",  "LEFT", "BACK", "RIGHT", "DOWN","UP"};
    private int currentFace = 0;
    private VisionPortal portal;
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize motors
        baseMotor = hardwareMap.get(DcMotorEx.class, "baseMotor");
        flipMotor = hardwareMap.get(DcMotorEx.class, "flipMotor");

        // Motor configuration
        baseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flipMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        baseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flipMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize color processors
        PredominantColorProcessor[] processors = new PredominantColorProcessor[9];
        for (int i = 0; i < 9; i++) {
            processors[i] = new PredominantColorProcessor.Builder()
                    .setRoi(REGIONS[i])
                    .setSwatches(
                            PredominantColorProcessor.Swatch.WHITE,
                            PredominantColorProcessor.Swatch.YELLOW,
                            PredominantColorProcessor.Swatch.RED,
                            PredominantColorProcessor.Swatch.BLUE,
                            PredominantColorProcessor.Swatch.GREEN,
                            PredominantColorProcessor.Swatch.ORANGE
                    )
                    .build();
        }

        // Build VisionPortal
        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessors(processors)
                .build();

        // Configure camera settings
        configureCamera();

        telemetry.addData("Status", "Ready - Press Start");
        telemetry.update();

        waitForStart();

        // Main scanning sequence
        while (opModeIsActive() && currentFace < 6) {
            // Position cube for scanning current face
            positionForScan(currentFace);

            // Scan the face
            scanCurrentFace(processors);

            // Move to next face
            currentFace++;
        }

        // After scanning all faces, display results
        displayFinalResults();
    }

    private void configureCamera() {
        try {
            WhiteBalanceControl whiteBalance = portal.getCameraControl(WhiteBalanceControl.class);
            if (whiteBalance != null) {
                whiteBalance.setWhiteBalanceTemperature(5500); // Optimal for yellow
                whiteBalance.setMode(WhiteBalanceControl.Mode.MANUAL);
            }
        } catch (Exception e) {
            telemetry.addData("Warning", "Could not configure white balance");
        }
    }

    private void positionForScan(int face) {
        telemetry.addData("Positioning", "Moving to scan " + faceNames[face]);
        telemetry.update();

        switch (face) {
            case 0: // FRONT - already in position
                break;

            case 1: // LEFT
                rotateCubeCCW();
                break;
            case 2: // BACK
                rotateCubeCCW();
                break;
            case 3: // RIGHT
                rotateCubeCCW();
                break;
            case 4: // DOWN
                rotateCubeCCW();
                flipCube();

                break;
            case 5: // up
                flipCube();
                flipCube();
                break;
        }

        sleep(100); // Wait for cube to stabilize
    }

    private void scanCurrentFace(PredominantColorProcessor[] processors) {
        // Lift arm to avoid obstruction
        moveFlipMotor(0, 1.0);

        // Wait for stabilization
        sleep(1000);

        // Scan and store colors
        String[] faceColors = new String[9];
        for (int i = 0; i < 9; i++) {
            PredominantColorProcessor.Result result = processors[i].getAnalysis();
            if (result != null && result.closestSwatch != null) {
                int r = Color.red(result.rgb);
                int g = Color.green(result.rgb);
                int b = Color.blue(result.rgb);

                // Enhanced color verification
                if (isPureWhite(r, g, b)) {
                    faceColors[i] = "WHITE";
                }
                else if (isDefinitelyYellow(r, g, b)) {
                    faceColors[i] = "YELLOW";
                }
                else {
                    faceColors[i] = result.closestSwatch.toString();
                }
            } else {
                faceColors[i] = "???";
            }
        }
        cubeState[currentFace] = faceColors;

        // Display results
        telemetry.clear();
        telemetry.addLine("Scanned " + faceNames[currentFace] + " face:");
        displayFace(faceColors);
        telemetry.update();

        sleep(2000); // Allow time to review scan
    }

    // Motor control methods
    private void moveFlipMotor(int target, double power) {
        flipMotor.setTargetPosition(target);
        flipMotor.setPower(power);
        flipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (flipMotor.isBusy() && opModeIsActive()) {
            idle();
        }
    }

    private void moveBaseMotor(int target, double power) {
        baseMotor.setTargetPosition(target);
        baseMotor.setPower(power);
        baseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (baseMotor.isBusy() && opModeIsActive()) {
            idle();
        }
    }

    // Cube movement primitives
    private void flipCube() {
        telemetry.addData("Action", "Flipping cube");
        telemetry.update();

        moveFlipMotor(24, 0.5);
        moveFlipMotor(90, 0.5);
        moveFlipMotor(24, 0.5);
    }

    private void rotateCubeCW() {
        telemetry.addData("Action", "Rotating cube clockwise");
        telemetry.update();

        moveFlipMotor(0, 1.0); // Arm up
        moveBaseMotor((int)(baseMotor.getCurrentPosition() - BASE_MOTOR_ROTATION), 0.5);
    }

    private void rotateCubeCCW() {
        telemetry.addData("Action", "Rotating cube counter-clockwise");
        telemetry.update();

        moveFlipMotor(0, 1.0); // Arm up
        moveBaseMotor((int)(baseMotor.getCurrentPosition() + BASE_MOTOR_ROTATION), 0.5);
    }

    private boolean isPureWhite(int r, int g, int b) {
        return r > 240 && g > 240 && b > 240 &&
                Math.abs(r-g) < 15 &&
                Math.abs(r-b) < 15;
    }

    private boolean isDefinitelyYellow(int r, int g, int b) {
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);

        boolean byHue = hsv[0] >= 40 && hsv[0] <= 70;
        boolean byRGB = (r > 180 && g > 180 && b < 120) && (g - b > 50);
        boolean notWhite = !(r > 230 && g > 230 && b > 230);

        return byHue || (byRGB && notWhite);
    }

    private void displayFinalResults() {
        telemetry.clear();
        telemetry.addLine("=== FINAL CUBE STATE ===");
        for (int i = 0; i < 6; i++) {
            telemetry.addLine("\n" + faceNames[i] + " FACE:");
            displayFace(cubeState[i]);
        }
        telemetry.update();

        // Keep showing results until stop pressed
        while (opModeIsActive()) {
            idle();
        }
    }

    private void displayFace(String[] face) {
        telemetry.addLine("╔═══════╦═══════╦═══════╗");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                face[0], face[1], face[2]));
        telemetry.addLine("╠═══════╬═══════╬═══════╣");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                face[3], face[4], face[5]));
        telemetry.addLine("╠═══════╬═══════╬═══════╣");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                face[6], face[7], face[8]));
        telemetry.addLine("╚═══════╩═══════╩═══════╝");
    }
}


