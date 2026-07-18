package me.rainma22.DuetMCP.Utils;

/**
 * static class for JSON RPC status codes
 *
 * @see https://www.jsonrpc.org/historical/json-rpc-over-http.html
 */
public class JSONRPCCodes {

    public static final int HTTP_SUCCESS = 200;
    public static int HTTP_ACCEPTED = 202;
    public static final int HTTP_NOTIFICATION_SUCCESS = 204;

    public static final int HTTP_PARSE_ERROR = 500;
    public static final int RPC_PARSE_ERROR = -32700;

    public static final int HTTP_INVALID_REQUEST = 400;
    public static final int RPC_INVALID_REQUEST = -32600;
    
    public static final int HTTP_METHOD_NOT_ALLOWED = 406;

    public static final int HTTP_METHOD_NOT_FOUND = 404;
    public static final int RPC_METHOD_NOT_FOUND = -32601;

    public static final int HTTP_INVALID_PARAMS = 500;
    public static final int RPC_INVALID_PARAMS = -32602;

    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int RPC_INTERNAL_ERROR = -32603;

    public static final int HTTP_SERVER_ERROR = 500;
    public static final int RPC_SERVER_ERROR_GENERAL = -32000;
    public static final int RPC_SERVER_ERROR_START = -32099;
    public static final int RPC_SERVER_ERROR_END = -32000;
}
