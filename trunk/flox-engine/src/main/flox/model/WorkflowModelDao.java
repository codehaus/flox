package flox.model;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 1:03:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WorkflowModelDao
{
    void save(WorkflowModel model);

    WorkflowModel get(Long id)
            throws NoSuchModelObjectException;

    WorkflowModel get(String processName,
                      Class flowedObjectClass, 
                      Criterion flowedObjectCriterion)
            throws NoSuchModelObjectException;

    List getAll(String processName);
}
