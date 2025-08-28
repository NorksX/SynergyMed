package mk.ukim.finki.synergymed.exceptions;

public class HealthProfileDoesNotExistException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Client does not have a health profile.";
    }
}
