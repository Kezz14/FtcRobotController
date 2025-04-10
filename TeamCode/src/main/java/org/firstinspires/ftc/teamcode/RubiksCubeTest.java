package org.firstinspires.ftc.teamcode;


import android.graphics.Color;


public class RubiksCubeTest {
            private Solver solver;
            private CubeNotation cubeNotation;
            private RubiksCubeColorDetector scanner;

            public RubiksCubeTest() {
                // Initialize components
                this.scanner = new RubiksCubeColorDetector();
                this.cubeNotation = new CubeNotation();

                // Dummy UI components (replace with your actual ones if needed)
                JButton[][] dummyFaces = new JButton[6][9];
                JLabel dummyLabel1 = new JLabel();
                JLabel dummyLabel2 = new JLabel();
                Color[] dummyColors = new Color[7];

                this.solver = new Solver(dummyFaces, dummyLabel1, dummyLabel2, dummyColors);
            }

            public void solveCube() {
                // 1. Scan cube state
                char[][] cubeState = scanner.scanCube();

                // 2. Load state into solver
                loadCubeState(cubeState);

                // 3. Solve the cube
                solver.solve();

                // 4. Execute solution
                String solution = getCleanSolution();
                cubeNotation.executeSolution(solution);
            }

            private void loadCubeState(char[][] state) {
                // Map scanner's color codes to solver's expected format if needed
                for (int face = 0; face < 6; face++) {
                    for (int sticker = 0; sticker < 9; sticker++) {
                        solver.c[face][sticker] = state[face][sticker];
                    }
                }
            }

            private String getCleanSolution() {
                // Extract clean move sequence
                String raw = solver.message.getText()
                        .replaceAll("<[^>]*>", "")  // Remove HTML tags
                        .replaceAll("\\b(Cross|F2L|OLL|PLL|Twophase):", "")  // Remove step labels
                        .trim();

                // Combine consecutive spaces
                return raw.replaceAll(" +", " ");
            }

            public static void main(String[] args) {
                RubiksCubeTest test = new RubiksCubeTest();
                test.solveCube();
            }
        }
