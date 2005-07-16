package flox.spi;

public class InvalidProcessHandleException extends ProcessSourceException
{
    /**
     * 
     */
    private static final long serialVersionUID = 3256722892195771449L;
    private ProcessHandle processHandle;
    
    public InvalidProcessHandleException(ProcessSource source, ProcessHandle processHandle)
    {
        super( source );
        this.processHandle = processHandle;
    }
    
    public InvalidProcessHandleException(ProcessSource source, ProcessHandle processHandle, String message)
    {
        super( source, message );
        this.processHandle = processHandle;
    }
    
    public ProcessHandle getProcessHandle()
    {
        return this.processHandle;
    }
}
