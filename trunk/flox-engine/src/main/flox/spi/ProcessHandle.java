package flox.spi;

public final class ProcessHandle
{
    private String handle;
    
    public ProcessHandle(String handle)
    {
        this.handle = handle;
    }
    
    public String getHandle()
    {
        return this.handle;
    }
}
