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
        for (ErrorLogCode pageType : ErrorLogCode.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static ErrorLogCode valueOf(int pageType) {
        return (ErrorLogCode) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
