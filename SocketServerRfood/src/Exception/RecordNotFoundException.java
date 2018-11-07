package exception;


public class RecordNotFoundException extends Exception
{
    // atributes
    private String message;
    
    @Override
    public String getMessage()
    {
        return this.message;
    }
    
    public RecordNotFoundException()
    {
        this.message = "Record not found";
    }
    
    public RecordNotFoundException(Object id)
    {
        this.message = "Record not found for id " + id.toString();
    }
}
