package flox.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 1:04:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StateModelDao
{
    void save(StateModel model);

    StateModel get(Long id)
            throws NoSuchModelObjectException;

    StateModel getCurrentState(WorkflowModel workflow)
            throws NoSuchModelObjectException;

    List<StateModel> getStateSequence(WorkflowModel workflow);
}
