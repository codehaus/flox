package flox.spi;

import flox.FloxException;


public class ProcessStorerException extends FloxException
{

    public ProcessStorerException(String message)
    {
        super( message );
    }
    
    public ProcessStorerException(Throwable cause)
    {
        super( cause );
    }

}
