package mk.ukim.finki.synergymed.exceptions;

public class PasswordsMismatchException extends RuntimeException{
    public PasswordsMismatchException(String message){
        super(message);
    }
}
