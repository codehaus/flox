package flox.web.tapestry;

import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.def.Process;
import flox.spi.ProcessSourceException;


public interface WorkflowEngineProvider
{
    WorkflowEngine getWorkflowEngine();
}
