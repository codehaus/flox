package flox.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import flox.FloxTestCase;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public abstract class DaoTestCase
        extends FloxTestCase
{
    private PlatformTransactionManager transactionManager;
    private TransactionStatus transaction;

    public DaoTestCase(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();

        TransactionDefinition transactionDef = new DefaultTransactionDefinition( TransactionDefinition.PROPAGATION_REQUIRED );

        this.transactionManager = (PlatformTransactionManager) getBean( "transactionManager" );
        this.transaction = this.transactionManager.getTransaction( transactionDef );
    }

    public void tearDown() throws Exception
    {
        this.transactionManager.commit( this.transaction );

        super.tearDown();
    }

}

