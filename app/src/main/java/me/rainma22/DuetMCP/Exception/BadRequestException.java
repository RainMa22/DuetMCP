package me.rainma22.DuetMCP.Exception;


public class BadRequestException extends Exception {

    public BadRequestException(String msg) {
        super(msg);
    }
    
    public BadRequestException(Exception cause) {
        super(cause);
    }
}
