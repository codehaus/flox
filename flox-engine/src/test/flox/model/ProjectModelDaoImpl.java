package flox.model;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 11:26:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectModelDaoImpl
        extends DaoBase implements ProjectModelDao
{
    public ProjectModelDaoImpl()
    {

    }

    public void save(ProjectModel project)
    {
        super.save( project );
    }

    public ProjectModel get(Long id)
            throws NoSuchModelObjectException
    {
        return (ProjectModel) get( ProjectModel.class,
                                   id );
    }
}
