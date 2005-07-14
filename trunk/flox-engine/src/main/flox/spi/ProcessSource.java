package flox.spi;

import flox.def.Process;

public interface ProcessSource
{
    Process getProcess(Object context, String name) throws ProcessSourceException;
    Process getProcess(ProcessHandle handle) throws InvalidProcessHandleException, ProcessSourceException;
}
