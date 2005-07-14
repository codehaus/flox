package flox.spi;

import flox.FloxException;


public class InvalidProcessHandleException extends ProcessSourceException
{
    private ProcessHandle processHandle;
    
    public InvalidProcessHandleException(ProcessSource source, ProcessHandle handle)
    {
        super( source );
        this.processHandle = processHandle;
    }
    
    public InvalidProcessHandleException(ProcessSource source, ProcessHandle handle, String message)
    {
        super( source, message );
        this.processHandle = processHandle;
    }
    
    public ProcessHandle getProcessHandle()
    {
        return this.processHandle;
    }
}
