package flox.model;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 11:28:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectModelDao
{
    void save(ProjectModel project);

    ProjectModel get(Long id)
            throws NoSuchModelObjectException;
}
