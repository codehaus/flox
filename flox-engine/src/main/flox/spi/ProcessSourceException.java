package flox.spi;

import flox.FloxException;

public class ProcessSourceException extends FloxException
{
    private ProcessSource processSource;

    public ProcessSourceException(ProcessSource processSource)
    {
        this.processSource = processSource;
    }
    
    public ProcessSourceException(ProcessSource processSource, Throwable cause)
    {
        super( cause );
        this.processSource = processSource;
    }
    
    public ProcessSourceException(ProcessSource processSource, String message)
    {
        super( message );
        this.processSource = processSource;
    }
}
