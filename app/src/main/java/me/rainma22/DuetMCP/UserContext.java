package me.rainma22.DuetMCP;

import java.util.UUID;

/**
 *
 */
public class UserContext {
    public boolean supportsRoots = false;
    public boolean supportsSampling = false;
    public boolean supportsElicitation = false;
    public String version = "";
    public UUID sessionId = null;
    public UserContext(){
        
    }
    
}
