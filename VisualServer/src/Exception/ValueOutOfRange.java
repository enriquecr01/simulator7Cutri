package Exception;

public class ValueOutOfRange extends Exception
{
    // atributes
    private String message;
    
    @Override
    public String getMessage()
    {
        return this.message;
    }
    
    public ValueOutOfRange(double value)
    {
        this.message = "The value " + value + " out of range";
    }
}
