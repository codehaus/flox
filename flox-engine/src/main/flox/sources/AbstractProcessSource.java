package flox.sources;

import flox.io.ProcessReader;
import flox.spi.ProcessSource;

public abstract class AbstractProcessSource implements ProcessSource
{
    private ProcessReader processReader;

    public AbstractProcessSource()
    {
        super();
    }
    
    public ProcessReader getProcessReader()
    {
        return processReader;
    }
    
    public void setProcessReader(ProcessReader processReader)
    {
        this.processReader = processReader;
    }
}
