package flox.model;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 11:27:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectModel
{
    private Long id;
    private String name;

    public ProjectModel()
    {

    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
