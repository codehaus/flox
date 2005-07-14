package flox.sources.hibernate;

public class HibernatedProcess
{
    private Object context;
    private String name;
    private String definitionXml;

    public HibernatedProcess()
    {
        super();
    }
    
    public Object getContext()
    {
        return context;
    }
    
    public void setContext( Object context )
    {
        this.context = context;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDefinitionXml()
    {
        return definitionXml;
    }
    
    public void setDefinitionXml( String definitionXml )
    {
        this.definitionXml = definitionXml;
    }
}
