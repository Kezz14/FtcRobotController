import java.util.*;

public class Solver {
    private char[][] cubeState = new char[6][9];
    private String[] f2lAlgorithms;
    private String[] ollAlgorithms;
    private String[] pllAlgorithms;
    private List<String> solution = new ArrayList<>();




    public class CubeSolver extends LinearOpMode {
        private char[][] c = new char[6][9]; // Cube state representation
        private Map<char[], char[]> leftLayer = new HashMap<>();
        private Map<char[], char[]> rightLayer = new HashMap<>();
        private char[] front, back, left, right, up, down;
        private Map<Integer, char[]> above = new HashMap<>();
        private Map<Integer, char[]> below = new HashMap<>();
        private String[] f2lAlgorithms = new String[41];
        private String[] ollAlgorithms = new String[57];
        private String[] pllAlgorithms = new String[21];
        private List<String> solution = new ArrayList<>();
        private boolean loaded = true;
        private boolean[] solved = new boolean[8];

        // Robot hardware components
        private DcMotorEx baseMotor;
        private DcMotorEx flipMotor;
        private double baseMotorRotation = 146.5; // Encoder ticks per 90-degree rotation
        private int flipTarget;

        public CubeSolver(DcMotorEx baseMotor, DcMotorEx flipMotor) {
            this.baseMotor = baseMotor;
            this.flipMotor = flipMotor;
            initialize();
        }
        private void initializeAlgorithms() {
            f2lAlgorithms = new String[]{"U R U' R'",
                    "F R' F' R",
                    "F' U' F",
                    "R U R'",
                    "U' R U' R' U F' U' F",
                    "U' R U R' U R U R'",
                    "F U2 F2 U' F2 U' F'",
                    "R' U2 R2 U R2 U R",
                    "U F' U F U' F' U' F",
                    "U' R U' R' U R U R'",
                    "U' R U R' U2 R U' R'",
                    "U F' U' F U2 F' U F",
                    "U' R U2 R' U2 R U' R'",
                    "U F' U2 F U2 F' U F",
                    "U R U2 R2 F R F'",
                    "U' F' U2 F2 R' F' R",
                    "R U' R' U2 R U R'",
                    "F' L' U2 L F",
                    "F' U F U2 R U R'",
                    "R U' R' U2 F' U' F",
                    "R U2 R' U' R U R'",
                    "F' U2 F U F' U' F",
                    "R' U' F' U F R F' U F",
                    "F U R U' R' F' R U' R'",
                    "R' F' R U R U' R' F",
                    "U R U' R' U' F' U F",
                    "R U' R2 F R F'",
                    "F' U F2 R' F' R",
                    "F' U' F U F' U' F",
                    "R U R' U' R U R'",
                    "R U' R' F' U2 F",
                    "U R U' R' U R U' R' U R U' R'",
                    "U' R U' R' U2 R U' R'",
                    "U R U R' U2 R U R'",
                    "U' R U R' U F' U' F",
                    "U F' U' F U' R U R'",
                    "R U' R' U F' U2 F U2 F' U F",
                    "R U' R' U' R U R' U2 R U' R'",
                    "R U' R' U R U2 R2 F R F'",
                    "F' U F U2 R U R' U R U' R'",
                    "R U' R' U2 F' U' F2 R' F' R" };
            ollAlgorithms = new String[]{ "R U2 R' U' R U' R'",
                    "R U R' U R U2 R'",
                    "R U2 R' U' R U R' U' R U' R'",
                    "R U2 R2 U' R2 U' R2 U2 R",
                    "r U R' U' r' F R F'",
                    "F' r U R' U' r' F R",
                    "R2 D R' U2 R D' R' U2 R'",
                    "R U R' U' R' F R F'",
                    "F R U R' U' F'",
                    "r' U2 R U R' U r",
                    "r U2 R' U' R U' r'",
                    "R U R2 U' R' F R U R U' F'",
                    "R' U' R' F R F' U R",
                    "R' U' R U' R' U R U l U' R' U x",
                    "R U R' U R U' R' U' R' F R F'",
                    "r U R' U' M U R U' R'",
                    "R U R' U' M' U R U' r'",
                    "R' U' F U R U' R' F' R",
                    "R U B' U' R' U R B R'",
                    "R' U' F' U F R",
                    "F U R U' R' F'",
                    "F U R U' R' U R U' R' F'",
                    "r' U' r U' R' U R U' R' U R r' U r",
                    "R U R' U R U' y R U' R' F'",
                    "R' F R U R U' R2 F' R2 U' R' U R U R'",
                    "R U R' U' R' F R2 U R' U' F'",
                    "R U R' U R' F R F' R U2 R'",
                    "R U2 R2 F R F' R U2 R'",
                    "F R U' R' U' R U R' F'",
                    "F U R U' R2 F' R U R U' R'",
                    "R' F R U R' F' R F U' F'",
                    "r U r' R U R' U' r U' r'",
                    "r' U' r R' U' R U r' U r",
                    "M U R U R' U' R' F R F' M'",
                    "F U R U2 R' U' R U2 R' U' F'",
                    "R U R' U R U2 R' F R U R' U' F'",
                    "R' U' R U' R' U2 R F R U R' U' F'",
                    "F R U R' U' R U R' U' F'",
                    "F' L' U' L U L' U' L U F",
                    "r U' r2 U r2 U r2 U' r",
                    "r' U r2 U' r2 U' r2 U r'",
                    "r' U' R U' R' U R U' R' U2 r",
                    "r U R' U R U' R' U R U2 r'",
                    "r U R' U R U2 r'",
                    "r' U' R U' R' U2 r",
                    "r' R2 U R' U R U2 R' U M'",
                    "M' R' U' R U' R' U2 R U' M",
                    "L F' L' U' L U F U' L'",
                    "R' F R U R' U' F' U R",
                    "R U2 R2 F R F' U2 R' F R F'",
                    "r U r' U2 R U2 R' U2 r U' r'",
                    "r' R2 U R' U r U2 r' U M'",
                    "r R2 U' R U' r' U2 r U' M",
                    "R U2 R2 F R F' U2 M' U R U' r'",
                    "M U R U R' U' M' R' F R F'",
                    "R U R' U R' F R F' U2 R' F R F'",
                    "M U R U R' U' M2 U R U' r'"};
            pllAlgorithms = new String[]{"R2 U R U R' U' R' U' R' U R'",
                    "R U' R U R U R U' R' U' R2",
                    "M2 U M2 U M' U2 M2 U2 M'",
                    "M2 U M2 U2 M2 U M2",
                    "x R' U R' D2 R U' R' D2 R2 x'",
                    "x R2 D2 R U R' D2 R U' R x'",
                    "x' R U' R' D R U R' D' R U R' D R U' R' D' x",
                    "R U' R' U' R U R D R' U' R D' R' U2 R'",
                    "R' U2 R U2 R' F R U R' U' R' F' R2",
                    "R' U L' U2 R U' R' U2 R L",
                    "R U R' F' R U R' U' R' F R2 U' R'",
                    "R U R' U' R' F R2 U' R' U' R U R' F'",
                    "R' U2 R' U' y R' F' R2 U' R' U R' F R U' F",
                    "R' U R' U' y R' F' R2 U' R' U R' F R F",
                    "F R U' R' U' R U R' F' R U R' U' R' F R F'",
                    "z U R' D R2 U' R D' U R' D R2 U' R D' z'",
                    "R' U L' U2 R U' L R' U L' U2 R U' L",
                    "R2 u R' U R' U' R u' R2 y' R' U R",
                    "F' U' F R2 u R' U R U' R u' R2",
                    "R2 U' R U' R U R' U R2 D' U R U' R' D",
                    "R U R' y' R2 u' R U' R' U R' u R2"};
        }

        private void initialize() {
            // Cube face initialization
            up = c[0];
            down = c[1];
            front = c[2];
            back = c[3];
            left = c[4];
            right = c[5];

            // Map cube layers
            rightLayer.put(front, right);
            rightLayer.put(right, back);
            rightLayer.put(back, left);
            rightLayer.put(left, front);

            leftLayer.put(right, front);
            leftLayer.put(back, right);
            leftLayer.put(left, back);
            leftLayer.put(front, left);

            above.put(1, front);
            above.put(5, right);
            above.put(7, back);
            above.put(3, left);

            below.put(1, back);
            below.put(5, right);
            below.put(7, front);
            below.put(3, left);

            try {
                // Load algorithms
                BufferedReader read = new BufferedReader(new InputStreamReader(CubeSolver.class.getResourceAsStream("/F2L.txt")));
                for (int i = 0; i < 41; i++) f2lAlgorithms[i] = read.readLine();

                read = new BufferedReader(new InputStreamReader(CubeSolver.class.getResourceAsStream("/OLL.txt")));
                for (int i = 0; i < 57; i++) ollAlgorithms[i] = read.readLine();

                read = new BufferedReader(new InputStreamReader(CubeSolver.class.getResourceAsStream("/PLL.txt")));
                for (int i = 0; i < 21; i++) pllAlgorithms[i] = read.readLine();
            } catch (Exception e) {
                telemetry.addData("Error", "Couldn't load algorithm files. Solution will not work.");
                loaded = false;
            }

            // Initialize motors
            baseMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            flipMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            baseMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            flipMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }

        @Override
        public void runOpMode() {
            waitForStart();

            // Example of solving a cube
            solve();
        }

        public void solve() {
            if (!loaded || !checkForCorrectInput()) {
                telemetry.addData("Error", "Invalid cube state or algorithms not loaded.");
                return;
            }

            char[][] copy = new char[6][9];
            for (int i = 0; i < 6; i++) {
                System.arraycopy(c[i], 0, copy[i], 0, 9);
            }

            solution.clear();
            try {
                telemetry.addData("Step", "Solving Cross...");
                solveCross();

                telemetry.addData("Step", "Solving F2L...");
                F2L();

                telemetry.addData("Step", "Solving OLL...");
                OLL();

                telemetry.addData("Step", "Solving PLL...");
                PLL();

                executeSolution();
            } catch (Exception e) {
                restore(copy);
                telemetry.addData("Error", "An exception occurred during solving.");
            }
        }

        private boolean checkForCorrectInput() {
            HashMap<Character, Integer> count = new HashMap<>();
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 9; j++) {
                    char color = c[i][j];
                    count.put(color, count.getOrDefault(color, 0) + 1);
                }
            }

            for (int occurrences : count.values()) {
                if (occurrences != 9) {
                    telemetry.addData("Error", "Each color must appear exactly 9 times.");
                    return false;
                }
            }

            return true;
        }

        private void executeSolution() {
            for (String move : solution) {
                telemetry.addData("Executing", move);
                executeMove(move);
            }
        }

        private void executeMove(String move) {
            switch (move) {
                case "U":
                    rotateFaceClockwise(up);
                    break;
                case "U'":
                    rotateFaceCounterClockwise(up);
                    break;
                case "U2":
                    rotateFaceTwice(up);
                    break;
                case "D":
                    flipCube();
                    rotateFaceClockwise(down);
                    flipCube();
                    break;
                case "D'":
                    flipCube();
                    rotateFaceCounterClockwise(down);
                    flipCube();
                    break;
                case "D2":
                    flipCube();
                    rotateFaceTwice(down);
                    flipCube();
                    break;
                case "F":
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    rotateFaceClockwise(front);
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    break;
                case "F'":
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    rotateFaceCounterClockwise(front);
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    break;
                case "F2":
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    rotateFaceTwice(front);
                    rotateCubeCW();
                    rotateCubeCW();
                    flipCube();
                    break;
                case "R":
                    rotateCubeCCW();
                    rotateFaceClockwise(right);
                    break;
                case "R'":
                    rotateCubeCCW();
                    rotateFaceCounterClockwise(right);
                    break;
                case "R2":
                    rotateCubeCCW();
                    rotateFaceTwice(right);
                    break;
                // Add more cases for other moves
            }
        }

        private void rotateFaceClockwise(char[] face) {
            baseMotor.setTargetPosition((int) baseMotorRotation);
            baseMotor.setPower(0.5);
            baseMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            while (baseMotor.isBusy() && opModeIsActive()) {
                telemetry.update();
            }
        }

        private void rotateFaceCounterClockwise(char[] face) {
            baseMotor.setTargetPosition((int) -baseMotorRotation);
            baseMotor.setPower(0.5);
            baseMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            while (baseMotor.isBusy() && opModeIsActive()) {
                telemetry.update();
            }
        }

        private void rotateFaceTwice(char[] face) {
            rotateFaceClockwise(face);
            rotateFaceClockwise(face);
        }

        private void flipCube() {
            flipTarget = 35; // Adjust these angles based on your robot's hardware
            flipMotor.setTargetPosition(flipTarget);
            flipMotor.setPower(1);
            flipMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            while (flipMotor.isBusy() && opModeIsActive()) {
                telemetry.update();
            }
            flipTarget = 120;
            flipMotor.setTargetPosition(flipTarget);
            while (flipMotor.isBusy() && opModeIsActive()) {
                telemetry.update();
            }
        }

        private void rotateCubeCW() {
            rotateFaceCounterClockwise(up);
        }

        private void rotateCubeCCW() {
            rotateFaceClockwise(up);
        }

        // Add methods for solving Cross, F2L, OLL, PLL, and cube state validation
    } }
