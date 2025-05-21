 package org.firstinspires.ftc.teamcode;


import java.util.HashMap;
import java.util.Locale;


public class CubeStringConverter {


    // Map color names as output by your scanner to cube letters
    private static final HashMap<String, Character> colorMap = new HashMap<>();
    static {
        colorMap.put("WHITE", 'U');
        colorMap.put("YELLOW", 'D');
        colorMap.put("RED", 'R');
        colorMap.put("ORANGE", 'L');
        colorMap.put("GREEN", 'F');
        colorMap.put("BLUE", 'B');
    }


    /** Converts 6x9 color names from scanner to 54-letter solver string */
    public static String cubeStateToSolverString(String[][] cubeState, String[] faceNames) {
        // Jaap/Kociemba expect order: U R F D L B, each 9 stickers
        // Your order is: {"FRONT",  "LEFT", "BACK", "RIGHT", "DOWN","UP"};
        int[] faceOrder = {
                findIndex(faceNames, "UP"),
                findIndex(faceNames, "RIGHT"),
                findIndex(faceNames, "FRONT"),
                findIndex(faceNames, "DOWN"),
                findIndex(faceNames, "LEFT"),
                findIndex(faceNames, "BACK")
        };


        StringBuilder sb = new StringBuilder();
        for (int face : faceOrder)
            for (int i = 0; i < 9; i++)
                sb.append(colorToLetter(cubeState[face][i]));
        return sb.toString();
    }


    private static int findIndex(String[] faces, String name) {
        for (int i = 0; i < faces.length; i++)
            if (faces[i].equalsIgnoreCase(name)) return i;
        return -1;
    }


    private static char colorToLetter(String color) {
        if (color == null) return '?';
        Character code = colorMap.get(color.toUpperCase(Locale.ROOT));
        if (code != null) return code;
        return '?';
    }

}
