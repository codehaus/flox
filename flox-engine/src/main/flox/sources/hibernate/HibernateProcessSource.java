package flox.sources.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import flox.def.Process;
import flox.io.ProcessReader;
import flox.spi.InvalidProcessHandleException;
import flox.spi.ProcessHandle;
import flox.spi.ProcessSource;
import flox.spi.ProcessSourceException;
import flox.spi.ProcessStorer;
import flox.spi.ProcessStorerException;

public class HibernateProcessSource extends HibernateDaoSupport implements ProcessSource, ProcessStorer
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
    
    public Process getProcess(ProcessHandle handle) throws InvalidProcessHandleException, ProcessSourceException
    {
        String str = handle.getHandle();
        
        int firstPipe = str.indexOf( "|" );
        
        String processName = null;
        
        Criteria criteria = getSession().createCriteria( HibernatedProcess.class );
        
        if ( firstPipe > 0 )
        {
            int secondPipe = str.indexOf( "|", firstPipe+1 );
        
            if ( secondPipe <= 0 )
            {
                throw new InvalidProcessHandleException( this, handle );
            }
            
            String contextClassName   = str.substring( 0, firstPipe );
            String contextObjectIdStr = str.substring( firstPipe+1, secondPipe );
            Long contextObjectId = Long.parseLong( contextObjectIdStr );
            
            criteria.add( Restrictions.sqlRestriction( "{alias}.contextObjectClass=?", contextClassName, new StringType() ) );
            criteria.add( Restrictions.sqlRestriction( "{alias}.contextObjectId=?", contextObjectId, new LongType() ) );
        
            processName = str.substring( secondPipe+1 );
        }
        else
        {
            processName = str;
        }
        
        criteria.add( Restrictions.eq( "name", processName ).ignoreCase() );
        
        criteria.setMaxResults( 1 );
        
        List<HibernatedProcess> results = criteria.list();
        
        if ( results.isEmpty() )
        {
            return null;
        }
        
        String definitionXml = results.get( 0 ).getDefinitionXml();
        
        Process process;
        
        try
        {
            process = getProcessReader().read( definitionXml );
        }
        catch ( Exception e )
        {
            throw new ProcessSourceException( this, e );
        }
        
        process.setProcessHandle( handle );
        
        return process;
    }
    
    public Process getProcess(Object context, String name) throws ProcessSourceException
    {
        ProcessHandle handle = null;
        
        if ( context == null )
        {
            handle = new ProcessHandle( name );
        }
        else
        {
            handle = new ProcessHandle( context.getClass().getName() + "|" 
                    + getSession().getIdentifier( context ) 
                    + "|" + name );
        }
        
        return getProcess( handle );
    }
    
    public void save(Object context, String name, String definitionXml) throws ProcessStorerException
    {
        getSession().update( context );
        
        HibernatedProcess process = new HibernatedProcess();
        
        process.setContext( context );
        process.setName( name );
        process.setDefinitionXml( definitionXml );
        
        getSession().saveOrUpdate( process );
    }
}
