package flox.web.tapestry.model;

import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.ITableColumn;

import flox.Workflow;
import flox.WorkflowEngine;
import flox.def.Process;
import flox.model.WorkflowModel;
import flox.model.WorkflowModelDao;


public class WorkflowTableModel extends BaseTableModel
{
    private WorkflowEngine workflowEngine;
    private String processName;
    private Process process;
    
    public WorkflowTableModel(WorkflowEngine workflowEngine, WorkflowModelDao dao, String processName, Process process)
    {
        super( dao, dao.getCriteria( processName ) );
        this.workflowEngine = workflowEngine;
        this.processName    = processName;
        this.process        = process;
    }
    
    public WorkflowTableModel(WorkflowEngine workflowEngine, WorkflowModelDao dao, String processName, Process process, String state)
    {
        super( dao, dao.getCriteria( processName, state ) );
        this.workflowEngine = workflowEngine;
        this.processName    = processName;
        this.process        = process;
    }
    
    public Iterator getCurrentPageRows(int first, int pageSize, ITableColumn objSortColumn, boolean sortOrder)
    {
        final Iterator iterator = super.getCurrentPageRows( first, pageSize, objSortColumn, sortOrder );
        
        return new Iterator()   {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            public Object next() {
                return new Workflow( workflowEngine, process, (WorkflowModel) iterator.next() );
            }
            public void remove() {
                iterator.remove();
            }
        };
    }
}
