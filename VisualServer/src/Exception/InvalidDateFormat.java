package Exception;

public class InvalidDateFormat extends Exception
{
    // atributes
    private String message;
    
    @Override
    public String getMessage()
    {
        return this.message;
    }
    
    public InvalidDateFormat()
    {
        this.message = "Invalid format date";
    }
}
