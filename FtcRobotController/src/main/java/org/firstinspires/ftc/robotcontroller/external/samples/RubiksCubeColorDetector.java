package org.firstinspires.ftc.robotcontroller.external.samples;

import android.graphics.Color;
import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.firstinspires.ftc.vision.opencv.PredominantColorProcessor;

@TeleOp(name = "Full Cube Scanner", group = "Concept")
public class RubiksCubeColorDetector extends LinearOpMode {

    // Define regions for detection
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

    // Cube state storage
    private String[][] cubeState = new String[6][9];
    private String[] faceNames = {"FRONT", "UP", "LEFT", "BACK", "RIGHT", "DOWN"};
    private int currentFace = 0;
    private boolean scanningComplete = false;

    @Override
    public void runOpMode() {
        // Create processors for each region
        PredominantColorProcessor[] processors = new PredominantColorProcessor[9];
        for (int i = 0; i < 9; i++) {
            processors[i] = new PredominantColorProcessor.Builder()
                    .setRoi(REGIONS[i])  // Set ROI during processor creation
                    .setSwatches(
                            PredominantColorProcessor.Swatch.RED,
                            PredominantColorProcessor.Swatch.BLUE,
                            PredominantColorProcessor.Swatch.GREEN,
                            PredominantColorProcessor.Swatch.YELLOW,
                            PredominantColorProcessor.Swatch.ORANGE,
                            PredominantColorProcessor.Swatch.WHITE)
                    .build();
        }

        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessors(processors)
                .build();

        telemetry.setMsTransmissionInterval(20);
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        waitForStart();

        // Main scanning loop
        while (opModeIsActive() && !scanningComplete) {
            telemetry.clear();
            telemetry.addLine("=== CUBE SCANNER ===");
            telemetry.addLine("Current Face: " + faceNames[currentFace]);
            telemetry.addLine("");
            telemetry.addLine("Position " + faceNames[currentFace] + " face");
            telemetry.addLine("Hold still for 3 seconds...");
            telemetry.update();

            sleep(3000); // 3 second delay

            // Perform scan
            cubeState[currentFace] = scanFace(processors);

            telemetry.clear();
            telemetry.addLine("Scanned " + faceNames[currentFace] + " face:");
            displayFace(cubeState[currentFace]);
            telemetry.addLine("");
            telemetry.addLine("Press A to continue");
            telemetry.addLine("Press B to rescan");
            telemetry.update();

            boolean decisionMade = false;
            while (!decisionMade && opModeIsActive()) {
                if (gamepad1.a) {
                    currentFace++;
                    if (currentFace >= 6) scanningComplete = true;
                    decisionMade = true;
                    sleep(300);
                } else if (gamepad1.b) {
                    decisionMade = true;
                    sleep(300);
                }
            }
        }

        // Display final results
        if (scanningComplete) {
            telemetry.clear();
            telemetry.addLine("=== CUBE SOLUTION ===");
            for (int i = 0; i < 6; i++) {
                telemetry.addLine("\n" + faceNames[i] + " FACE:");
                displayFace(cubeState[i]);
            }
            telemetry.addLine("\nScanning complete!");
            telemetry.update();
        }

        while (opModeIsActive()) idle();
    }

    private String[] scanFace(PredominantColorProcessor[] processors) {
        String[] colors = new String[9];
        for (int i = 0; i < 9; i++) {
            PredominantColorProcessor.Result result = processors[i].getAnalysis();
            colors[i] = (result != null && result.closestSwatch != null) ?
                    result.closestSwatch.toString() : "???";
        }
        return colors;
    }

    private void displayFace(String[] face) {
        telemetry.addLine("╔═══════╦═══════╦═══════╗");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                formatColor(face[0]), formatColor(face[1]), formatColor(face[2])));
        telemetry.addLine("╠═══════╬═══════╬═══════╣");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                formatColor(face[3]), formatColor(face[4]), formatColor(face[5])));
        telemetry.addLine("╠═══════╬═══════╬═══════╣");
        telemetry.addLine(String.format("║ %-5s ║ %-5s ║ %-5s ║",
                formatColor(face[6]), formatColor(face[7]), formatColor(face[8])));
        telemetry.addLine("╚═══════╩═══════╩═══════╝");
    }

    private String formatColor(String color) {
        if (color == null || color.equals("???")) return " ??? ";
        return " " + color.substring(0, Math.min(3, color.length())).toUpperCase() + " ";
    }
}


