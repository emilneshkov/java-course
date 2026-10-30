package bg.sofia.uni.fmi.mjt.parking.vehicle;

public class LicensePlate {
    private  final String plate;

    public LicensePlate(String plate) {
        this.plate = plate;
    }

    public static LicensePlate parse(String text){
        if(text == null ){
            return null;
        }

        String normalizedText = normalize(text);
        if(!isValidPlate(normalizedText)){
            return null;
        }
        return new LicensePlate(normalizedText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LicensePlate other) {
            return plate.equals(other.plate);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return plate.hashCode();
    }

    @Override
    public String toString() {
        return plate;
    }

    private static String normalize(String text){
        String normalized = "";
        String cyrillic = "АВЕКМНОРСТУХ";
        String latin    = "ABEKMHOPCTYX";

        text=text.toUpperCase();

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);

            if (letter == ' ' || letter == '-') {
                continue;
            }

            int index = cyrillic.indexOf(letter);

            if (index != -1) {
                letter = latin.charAt(index);
            }

            normalized += letter;
        }

        return normalized;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(char c) {
        String allowedLetters = "ABEKMHOPCTYX";
        return allowedLetters.indexOf(c) != -1;
    }

    private static boolean isValidPlate(String text){
        if (text == null || text.length() < 6 || text.length() > 8) {
            return false;
        }

        int index = 0;
        if (!isLetter(text.charAt(index++))) {
            return false;
        }

        if (isLetter(text.charAt(index))) {
            index++;
        }

        for (int i = 0; i < 4; i++) {
            if (!isNumber(text.charAt(index++))) {
                return false;
            }
        }

        int remaining = text.length() - index;

        if (remaining < 1 || remaining > 2) {
            return false;
        }

        while (index < text.length()) {
            if (!isLetter(text.charAt(index++))) {
                return false;
            }
        }

        return true;
    }

}
