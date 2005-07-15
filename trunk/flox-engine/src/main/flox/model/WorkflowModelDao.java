package flox.model;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import flox.def.Process;
import flox.def.State;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 1:03:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WorkflowModelDao extends PageableDao
{
    void save(WorkflowModel model);

    WorkflowModel get(Long id)
            throws NoSuchModelObjectException;

    WorkflowModel get(String processName,
                      Class flowedObjectClass, 
                      Criterion flowedObjectCriterion)
            throws NoSuchModelObjectException;

    WorkflowModel get(Process process, Object flowedObject) 
        throws NoSuchModelObjectException;
    
    //List getAll(String processName, State currentState);
    //List getAll(String processName);
    
    List<WorkflowModel> getAll(Process process);
    List<WorkflowModel> getAll(Process process, State state);
    
    Criteria getCriteria(String processHandle);
    Criteria getCriteria(String processHandle, String state);
}
