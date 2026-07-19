package me.rainma22.DuetMCP.event;

import java.text.MessageFormat;

/**
 *
 */
public record Event(String id, String msg) {
    public Event(String id, String msg){
        this.id = id;
        this.msg = msg;
    }
    
    private static final MessageFormat FORMAT
            = new MessageFormat("id: {0}\n"
                    + "data: {1}\n"
            //                    + "retry: 1"
            );

    public String toString() {
        return FORMAT.format(new Object[]{id, msg});
    }
}
