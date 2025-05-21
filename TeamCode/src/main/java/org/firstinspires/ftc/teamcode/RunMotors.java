package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "RunMotors ", group = "Cam")
public class RunMotors extends LinearOpMode {

    public DcMotor flipMotor;
    public DcMotor baseMotor;
    public TouchSensor ButtonDown;
    public TouchSensor ButtonUp;

    String Func;
    String Moves;
    String ButtonDown2;
    String Side_B;
    int Flip_Target;
    int NumbMoves;
    int BaseTarget;
    String Side_D;
    int MoveNumb;
    String ButtonUp2;
    double Base_Motor_Rotation;
    String Side_F;
    String Side_U;
    String A;
    String Side_L;
    String Side_R;
    String B;
    String X;
    public class RubiksCubeColorDetector {
        private DcMotorEx baseMotor;
        private DcMotorEx flipMotor;
        // private ColorSensor colorSensor; // If you use a color sensor, add it here

    /**
     * Describe this function...
     */
    public void Run_Moves() {
        Func = "Run Moves";
        while (opModeIsActive()) {
            Telemetry2();
            if (MoveNumb <= NumbMoves) {
                if ("D".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Turn_Face("D");
                } else if ("B".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Flip_Cube();
                    Turn_Face("B");
                    Flip_Cube();
                    Flip_Cube();
                    Flip_Cube();
                } else if ("U".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Flip_Cube();
                    Flip_Cube();
                    Turn_Face("U");
                    Flip_Cube();
                    Flip_Cube();
                } else if ("F".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Rotate_Cube_CW();
                    Rotate_Cube_CW();
                    Flip_Cube();
                    Turn_Face("F");
                    Rotate_Cube_CW();
                    Rotate_Cube_CW();
                    Flip_Cube();
                } else if ("R".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Rotate_Cube_CCW();
                    Flip_Cube();
                    Turn_Face("R");
                    Rotate_Cube_CCW();
                    Rotate_Cube_CCW();
                    Flip_Cube();
                    Rotate_Cube_CCW();
                } else if ("L".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Rotate_Cube_CW();
                    Flip_Cube();
                    Turn_Face("L");
                    Rotate_Cube_CW();
                    Rotate_Cube_CW();
                    Flip_Cube();
                    Rotate_Cube_CW();
                }
            } else {
                Arm_Up();
                break;
            }
            MoveNumb += 1;
        }
    }

    /**
     * Describe this function...
     */
    public void Controller_Arm() {
        Func = "Flip";
        flipMotor.setTargetPosition(0);
        while (opModeIsActive()) {
            Telemetry2();
            Toggle_Button_Check();
            if (X.equals("True")) {
                break;
            }
            if (A.equals("True")) {
                Run_Flip_Sequence(25);
                Run_Flip_Sequence(90);
                Run_Flip_Sequence(25);
            }
            if (B.equals("True")) {
                Run_Flip_Sequence(0);
            }
            Update_Arm();
        }
        baseMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Describe this function...
     */
    public void Base_Continuous() {
        Func = "Base Continuous";
        while (opModeIsActive()) {
            Telemetry2();
            Toggle_Button_Check();
            if (X.equals("True")) {
                break;
            }
            baseMotor.setPower(1 * gamepad1.left_stick_x);
        }
    }

    /**
     * Describe this function...
     */
    public void Controller_Base() {
        Func = "Base";
        while (opModeIsActive()) {
            Telemetry2();
            Update_Base();
            Toggle_Button_Check();
            if (X.equals("True")) {
                break;
            }
            if (A.equals("True")) {
                BaseTarget += Base_Motor_Rotation;
            }
            if (B.equals("True")) {
                BaseTarget += -Base_Motor_Rotation;
            }
        }
    }

    /**
     * Describe this function...
     */

    public void runOpMode() {
        boolean TOGGLE;
        String OverShoot;

        flipMotor = hardwareMap.get(DcMotor.class, "flipMotor");
        baseMotor = hardwareMap.get(DcMotor.class, "baseMotor");
        ButtonDown = hardwareMap.get(TouchSensor.class, "ButtonDown");
        ButtonUp = hardwareMap.get(TouchSensor.class, "ButtonUp");

        // Put initialization blocks here.
        waitForStart();
        MoveNumb = 0;
        Func = "Err";
        TOGGLE = true;
        BaseTarget = 0;
        Flip_Target = 0;
        ButtonUp2 = "False";
        ButtonDown2 = "False";
        OverShoot = "False";
        A = "False";
        B = "False";
        X = "False";
        Base_Motor_Rotation = 146.5;
        Initialize_Orientation();
        baseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flipMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Set_Sequence("R_L_U2B'L2U_F_L2U'R_F'R_U_B2D'L2B2D_B2D_L2F2");
        if (opModeInInit()) {
            Telemetry2();
        }
        if (opModeIsActive()) {
            Run_Moves_Alt();
        }
        while (opModeIsActive() && (flipMotor.isBusy() || baseMotor.isBusy())) {
            Telemetry2();
        }
    }

    /**
     * Describe this function...
     */
    public void Set_Sequence(String Seq) {
        Moves = Seq;
        NumbMoves = Moves.length() / 2;
        MoveNumb = 0;
    }

    /**
     * Describe this function...
     */
    public void Toggle_Button_Check() {
        if (ButtonDown.isPressed()) {
            ButtonDown2 = "Hold";
        } else if (ButtonDown2.equals("Hold")) {
            ButtonDown2 = "True";
        } else if (ButtonDown2.equals("True")) {
            ButtonDown2 = "False";
        }
        if (ButtonUp.isPressed()) {
            ButtonUp2 = "Hold";
        } else if (ButtonUp2.equals("Hold")) {
            ButtonUp2 = "True";
        } else if (ButtonUp2.equals("True")) {
            ButtonUp2 = "False";
        }
        if (gamepad1.a) {
            A = "Hold";
        } else if (A.equals("Hold")) {
            A = "True";
        } else if (A.equals("True")) {
            A = "False";
        }
        if (gamepad1.b) {
            B = "Hold";
        } else if (B.equals("Hold")) {
            B = "True";
        } else if (B.equals("True")) {
            B = "False";
        }
        if (gamepad1.x) {
            X = "Hold";
        } else if (X.equals("Hold")) {
            X = "True";
        } else if (X.equals("True")) {
            X = "False";
        }
    }

    /**
     * Describe this function...
     */
    public void Telemetry2() {
        if (ButtonDown2.equals("True")) {
            while (opModeIsActive()) {
                Telem();
                if (ButtonDown2.equals("True")) {
                    break;
                }
            }
            Toggle_Button_Check();
        } else {
            Telem();
        }
    }

    /**
     * Describe this function...
     */
    public void Update_Base() {
        baseMotor.setTargetPosition(BaseTarget);
        baseMotor.setPower(0.7);
        baseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Describe this function...
     */
    public void Set_Pos() {
        Func = "Run Moves";
        flipMotor.setTargetPosition(Flip_Target);
        flipMotor.setPower(0);
        flipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (opModeIsActive()) {
            Telemetry2();
        }
    }

    /**
     * Describe this function...
     */
    public void Update_Arm() {
        flipMotor.setTargetPosition(Flip_Target);
        flipMotor.setPower(1);
        flipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Describe this function...
     */
    public void Telem() {
        Toggle_Button_Check();
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
        telemetry.addData("Motor", flipMotor.isBusy());
        telemetry.addData("Flip Target =", flipMotor.getTargetPosition());
        telemetry.addData("Flip Pos =", flipMotor.getCurrentPosition());
        telemetry.addData("Base Target =", baseMotor.getTargetPosition());
        telemetry.addData("Base Pos =", baseMotor.getCurrentPosition());
        telemetry.addData("Move", JavaUtil.inTextGetSubstring(Moves, JavaUtil.AtMode.FROM_START, ((2 * MoveNumb + 1) - 1), JavaUtil.AtMode.FROM_START, ((2 * MoveNumb + 2) - 1)));
        telemetry.addLine(Current_Oreintation());
        telemetry.update();
    }

    /**
     * Describe this function...
     */
    public void Run_Flip_Sequence(int Target) {
        Flip_Target = Target;
        Update_Arm();
        while (flipMotor.isBusy()) {
            Telemetry2();
            if (flipMotor.getTargetPosition() == 0 && flipMotor.getCurrentPosition() < 10) {
                break;
            }
            if (flipMotor.getTargetPosition() == 120 && flipMotor.getCurrentPosition() > 90) {
                break;
            }
            if (flipMotor.getTargetPosition() == 30 && flipMotor.getCurrentPosition() < 35) {
                break;
            }
            if (flipMotor.getTargetPosition() == 45 && flipMotor.getCurrentPosition() > 40) {
                break;
            }
        }
    }

    /**
     * Describe this function...
     */
    public void Flip_Cube() {
        Run_Flip_Sequence(35);
        Run_Flip_Sequence(120);
        Run_Flip_Sequence(30);
    }

    /**
     * Describe this function...
     */
    public void Scan_Cube() {
        Func = "Run Moves";
        Telemetry2();
        Update_Arm();
        Update_Base();
        for (int count = 0; count < 3; count++) {
            Arm_Up();
            Wait_For_Input();
            Rotate_Cube_CW();
        }
        Wait_For_Input();
        Flip_Cube();
        Arm_Up();
        Wait_For_Input();
        Flip_Cube();
        Flip_Cube();
        Arm_Up();
        Wait_For_Input();
    }

    /**
     * Describe this function...
     */
    public void Arm_Down() {
        Run_Flip_Sequence(45);
    }

    /**
     * Describe this function...
     */
    public void Arm_Up() {
        Run_Flip_Sequence(0);
    }

    /**
     * Describe this function...
     */
    public void Wait_For_Input() {
        while (opModeIsActive()) {
            Telemetry2();
            if (ButtonUp2.equals("True")) {
                break;
            }
        }
    }

    /**
     * Describe this function...
     */
    public void Turn_Face_CW() {
        Arm_Down();
        BaseTarget += Base_Motor_Rotation;
        Wait_for_base();
    }

    /**
     * Describe this function...
     */
    public void Wait_for_base() {
        Update_Base();
        if (baseMotor.getCurrentPosition() < baseMotor.getTargetPosition()) {
            while (baseMotor.isBusy()) {
                Telemetry2();
                if (baseMotor.getCurrentPosition() > baseMotor.getTargetPosition()) {
                    break;
                }
            }
        } else if (baseMotor.getCurrentPosition() > baseMotor.getTargetPosition()) {
            while (baseMotor.isBusy()) {
                Telemetry2();
                if (baseMotor.getCurrentPosition() < baseMotor.getTargetPosition()) {
                    break;
                }
            }
        } else {
            while (baseMotor.isBusy()) {
                Telemetry2();
            }
        }
    }

    /**
     * Describe this function...
     */
    public void Turn_Face_CCW() {
        Arm_Down();
        BaseTarget += -Base_Motor_Rotation;
        Wait_for_base();
    }

    /**
     * Describe this function...
     */
    public void Turn_Face_X2() {
        Arm_Down();
        BaseTarget += Base_Motor_Rotation;
        BaseTarget += Base_Motor_Rotation;
        Wait_for_base();
    }

    /**
     * Describe this function...
     */
    public void Initialize_Orientation() {
        Side_B = "B";
        Side_D = "D";
        Side_F = "F";
        Side_L = "L";
        Side_R = "R";
        Side_U = "U";
    }

    /**
     * Describe this function...
     */
    public String Current_Oreintation() {
        telemetry.addLine("    " + Side_U);
        telemetry.addLine(" " + Side_L + "  " + Side_F + "  " + Side_R + "  " + Side_B);
        telemetry.addLine("    " + Side_D);
        return " ";
    }

    /**
     * Describe this function...
     */
    public void Rotate_Cube_CCW() {
        Arm_Up();
        BaseTarget += Base_Motor_Rotation;
        Wait_for_base();
    }

    /**
     * Describe this function...
     */
    public void Run_Moves_Alt() {
        Func = "Run Moves Alt";
        while (opModeIsActive()) {
            Telemetry2();
            Arm_Up();
            if (MoveNumb <= NumbMoves) {
                if ("D".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("D");
                    Turn_Face("D");
                } else if ("B".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("B");
                    Turn_Face("B");
                } else if ("U".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("U");
                    Turn_Face("U");
                } else if ("F".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("F");
                    Turn_Face("F");
                } else if ("R".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("R");
                    Turn_Face("R");
                } else if ("L".equals(JavaUtil.inTextGetLetter(Moves, JavaUtil.AtMode.FROM_START, ((1 + 2 * MoveNumb) - 1)))) {
                    Update_Oreintation("L");
                    Turn_Face("L");
                }
            } else {
                Arm_Up();
                break;
            }
            MoveNumb += 1;
        }
        Update_Oreintation("F");
        Update_Oreintation("D");
        Arm_Up();
    }

    /**
     * Describe this function...
     */
    public void Rotate_Cube_CW() {
        Arm_Up();
        BaseTarget += -Base_Motor_Rotation;
        Wait_for_base();
    }

    /**
     * Describe this function...
     */
    public void Update_Oreintation(String Next_Side) {
        String Shuffle;

        if (!Next_Side.equals(Side_D)) {
            if (Next_Side.equals(Side_U)) {
                Flip_Cube();
                Flip_Cube();
                Shuffle = Side_U;
                Side_U = Side_D;
                Side_D = Shuffle;
                Shuffle = Side_F;
                Side_F = Side_B;
                Side_B = Shuffle;
            } else if (Next_Side.equals(Side_L)) {
                Rotate_Cube_CW();
                Flip_Cube();
                Shuffle = Side_D;
                Side_D = Side_L;
                Side_L = Side_F;
                Side_F = Shuffle;
                Shuffle = Side_U;
                Side_U = Side_R;
                Side_R = Side_B;
                Side_B = Shuffle;
            } else if (Next_Side.equals(Side_F)) {
                Rotate_Cube_CW();
                Rotate_Cube_CW();
                Flip_Cube();
                Shuffle = Side_D;
                Side_D = Side_F;
                Side_F = Shuffle;
                Shuffle = Side_R;
                Side_R = Side_L;
                Side_L = Shuffle;
                Shuffle = Side_U;
                Side_U = Side_B;
                Side_B = Shuffle;
            } else if (Next_Side.equals(Side_R)) {
                Rotate_Cube_CCW();
                Flip_Cube();
                Shuffle = Side_D;
                Side_D = Side_R;
                Side_R = Side_F;
                Side_F = Shuffle;
                Shuffle = Side_U;
                Side_U = Side_L;
                Side_L = Side_B;
                Side_B = Shuffle;
            } else if (Next_Side.equals(Side_B)) {
                Flip_Cube();
                Shuffle = Side_D;
                Side_D = Side_B;
                Side_B = Side_U;
                Side_U = Side_F;
                Side_F = Shuffle;
            }
        }
    }

    /**
     * Describe this function...
     */
    public boolean CheckMove(String Notation) {
        if (Notation.equals(JavaUtil.inTextGetSubstring(Moves, JavaUtil.AtMode.FROM_START, ((2 * MoveNumb + 1) - 1), JavaUtil.AtMode.FROM_START, ((2 * MoveNumb + 2) - 1)))) {
            return true;
        }
        return false;
    }

    /**
     * Describe this function...
     */
    public void Turn_Face(String Next_Side) {
        if (CheckMove(Next_Side + "_")) {
            Turn_Face_CW();
        } else if (CheckMove(Next_Side + "'")) {
            Turn_Face_CCW();
        } else if (CheckMove(Next_Side + "2")) {
            Turn_Face_X2();
        }}}}
