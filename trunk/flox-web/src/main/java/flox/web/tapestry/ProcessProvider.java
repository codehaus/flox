package flox.web.tapestry;

import flox.NoSuchProcessException;
import flox.def.Process;
import flox.spi.ProcessSourceException;


public interface ProcessProvider
{
    Process getProcess() throws ProcessSourceException, NoSuchProcessException;
}
