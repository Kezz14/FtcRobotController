package org.firstinspires.ftc.teamcode;

    public class CubeNotation {

        public void executeMoves(String moveSequence) {
            String[] moves = moveSequence.split("\\s+");
            for (String move : moves) {
                if (move.isEmpty()) continue;

                switch (move) {
                    // Basic moves
                    case "U":  rotateUp(); break;
                    case "U'": rotateUpPrime(); break;
                    case "U2": rotateUpTwice(); break;
                    case "D":  rotateDown(); break;
                    case "D'": rotateDownPrime(); break;
                    case "D2": rotateDownTwice(); break;
                    case "F":  rotateFront(); break;
                    case "F'": rotateFrontPrime(); break;
                    case "F2": rotateFrontTwice(); break;
                    case "B":  rotateBack(); break;
                    case "B'": rotateBackPrime(); break;
                    case "B2": rotateBackTwice(); break;
                    case "L":  rotateLeft(); break;
                    case "L'": rotateLeftPrime(); break;
                    case "L2": rotateLeftTwice(); break;
                    case "R":  rotateRight(); break;
                    case "R'": rotateRightPrime(); break;
                    case "R2": rotateRightTwice(); break;

                    // Wide moves
                    case "u":  wideU(); break;
                    case "u'": wideUPrime(); break;
                    case "u2": wideUTwice(); break;
                    // Add other wide moves as needed...

                    // Cube rotations
                    case "x":  rotateX(); break;
                    case "x'": rotateXPrime(); break;
                    case "x2": rotateXTwice(); break;
                    case "y":  rotateY(); break;
                    case "y'": rotateYPrime(); break;
                    case "y2": rotateYTwice(); break;
                    case "z":  rotateZ(); break;
                    case "z'": rotateZPrime(); break;
                    case "z2": rotateZTwice(); break;

                    default:
                        System.out.println("Unknown move: " + move);
                }
                System.out.println("Executed: " + move);
                // Add delay if needed for physical cube robots
            }
        }

        // Implement all move methods below
        private void rotateUp() {
            System.out.println("Rotating Up clockwise");
            // Implement actual move logic
        }

        private void rotateUpPrime() {
            System.out.println("Rotating Up counter-clockwise");
        }

        private void rotateUpTwice() {
            System.out.println("Rotating Up twice");
        }

        // Implement all other moves similarly...
        private void rotateDown() { /* ... */ }
        private void rotateDownPrime() { /* ... */ }
        private void rotateDownTwice() { /* ... */ }

        // Wide moves
        private void wideU() { /* ... */ }
        private void wideUPrime() { /* ... */ }
        private void wideUTwice() { /* ... */ }

        // Cube rotations
        private void rotateX() { /* ... */ }
        private void rotateXPrime() { /* ... */ }
        private void rotateXTwice() { /* ... */ }

        // ... include all other moves from your list
    }


