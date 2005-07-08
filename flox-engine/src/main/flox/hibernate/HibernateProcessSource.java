package flox.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import flox.def.Process;
import flox.io.ProcessReader;
import flox.spi.ProcessSource;

public class HibernateProcessSource extends HibernateDaoSupport implements ProcessSource
{
    private ProcessReader processReader;
    
    public HibernateProcessSource()
    {
        super();
    }
    
    public ProcessReader getProcessReader()
    {
        return processReader;
    }
    
    public void setProcessReader(ProcessReader processReader)
    {
        this.processReader = processReader;
    }
    
    public Process getProcess(Object context, String name) throws Exception
    {
        Criteria criteria = getSession().createCriteria( HibernatedProcess.class );
        
        if ( context == null )
        {
            criteria.add( Restrictions.isNull( "context" ) );
        }
        else
        {
            criteria.add( Restrictions.eq( "context", context ) );
        }
        
        criteria.add( Restrictions.eq( "name", name ) );
        
        criteria.setMaxResults( 1 );
        
        List<HibernatedProcess> results = criteria.list();
        
        if ( results.isEmpty() )
        {
            return null;
        }
        
        String definitionXml = results.get( 0 ).getDefinitionXml();
        
        Process process = getProcessReader().read( definitionXml );
        
        return process;
    }
}
