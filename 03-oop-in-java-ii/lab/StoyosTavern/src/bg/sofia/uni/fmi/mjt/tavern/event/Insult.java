package bg.sofia.uni.fmi.mjt.tavern.event;

public record Insult(String from, String to) implements Event{
    /**
     * One guest insults another.
     *
     * @throws IllegalArgumentException if any of the names is null or blank
     * @throws IllegalArgumentException if from and to are the same name (names are compared exactly, case-sensitive)
     */

   public Insult {
       Event.validateDifferentString(from, to);
   }
}
