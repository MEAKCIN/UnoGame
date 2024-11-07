package exceptions;

public class WrongCardPlayed extends Exception{
    public WrongCardPlayed(String message) {
        super("This card cannot played:"+message);
    }
}
