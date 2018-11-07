package Models;

import java.util.HashMap;
import java.util.Map;

public enum ErrorLogCode 
{
    InvalidJSONFormat(0), 
    ValueOutOfRange(1), 
    RecordNotFound(2);
    
    private int value;
    private static Map map = new HashMap<>();

    private ErrorLogCode(int value) {
        this.value = value;
    }

    static {
        for (ErrorLogCode errorLogCode : ErrorLogCode.values()) {
            map.put(errorLogCode.value, errorLogCode);
        }
    }

    public static ErrorLogCode valueOf(int errorLogCode) {
        return (ErrorLogCode) map.get(errorLogCode);
    }

    public int getValue() {
        return value;
    }
    
}
