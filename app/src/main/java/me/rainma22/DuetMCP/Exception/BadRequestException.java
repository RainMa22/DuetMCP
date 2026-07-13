package me.rainma22.DuetMCP.Exception;


public class BadRequestException extends DuetMCPException {

    public BadRequestException(String msg) {
        super(msg);
    }
    
    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
