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
    private Process process;
    
    public WorkflowTableModel(WorkflowEngine workflowEngine, WorkflowModelDao dao, Process process)
    {
        super( dao, dao.getCriteria( process.getProcessHandle().getHandle() ) );
        this.workflowEngine = workflowEngine;
        this.process        = process;
    }
    
    public WorkflowTableModel(WorkflowEngine workflowEngine, WorkflowModelDao dao, Process process, String state)
    {
        super( dao, dao.getCriteria( process.getProcessHandle().getHandle(), state ) );
        this.workflowEngine = workflowEngine;
        this.process        = process;
    }
    
    public Iterator<Workflow> getCurrentPageRows(int first, int pageSize, ITableColumn objSortColumn, boolean sortOrder)
    {
        final Iterator<WorkflowModel> iterator = (Iterator<WorkflowModel>) super.getCurrentPageRows( first, pageSize, objSortColumn, sortOrder );
        
        return new Iterator<Workflow>()   {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            public Workflow next() {
                return new Workflow( workflowEngine, process, iterator.next() );
            }
            public void remove() {
                iterator.remove();
            }
        };
    }
}
