package flox.spi;

import flox.FloxException;


public class ProcessStorerException extends FloxException
{
    private static final long serialVersionUID = 3257001055786448179L;

    public ProcessStorerException(String message)
    {
        super( message );
    }
    
    public ProcessStorerException(Throwable cause)
    {
        super( cause );
    }

}
