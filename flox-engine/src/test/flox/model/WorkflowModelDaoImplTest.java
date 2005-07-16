package flox.model;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

public class WorkflowModelDaoImplTest 
        extends DaoTestCase
{
    public WorkflowModelDaoImplTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(WorkflowModelDaoImplTest.class);
    }

    public void testDao() throws Exception
    {
        assertNotNull( "dao is available",
                       getBean( "workflowModelDao" ) );
    }

    public void testSave() throws Exception
    {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setProcessHandle( "test process" );

        WorkflowModelDao workflowDao = (WorkflowModelDao) getBean( "workflowModelDao" );

        workflowDao.save( workflow );

        Long id = workflow.getId();

        WorkflowModel fetchedWorkflow = workflowDao.get( id );

        assertEquals( "same id",
                      workflow.getId(),
                      fetchedWorkflow.getId() );

        assertEquals( "same process",
                      workflow.getProcessHandle(),
                      fetchedWorkflow.getProcessHandle() );
    }

    public void testGetByFlowedObjectCriteria() throws Exception
    {
        String name = "project-" + new Date();

        ProjectModel project = new ProjectModel();
        project.setName( name );

        ProjectModelDao projectDao = (ProjectModelDao) getBean( "projectModelDao" );
        projectDao.save( project );

        WorkflowModel workflow = new WorkflowModel();
        workflow.setProcessHandle( "test process" );
        workflow.setFlowedObject( project );

        WorkflowModelDao workflowDao = (WorkflowModelDao) getBean( "workflowModelDao" );
        workflowDao.save( workflow );

        WorkflowModel fetchedWorkflow = workflowDao.get( workflow.getId() );

        assertEquals( "flowed object class is the same",
                      ProjectModel.class,
                      fetchedWorkflow.getFlowedObject().getClass() );

        assertEquals( "flowed object id is the same",
                      project.getId(),
                      ((ProjectModel)fetchedWorkflow.getFlowedObject()).getId() );

        assertEquals( "name is the same",
                      name,
                      ((ProjectModel)fetchedWorkflow.getFlowedObject()).getName() );

        Criterion criterion = Expression.eq(  "name",
                                              name );

        fetchedWorkflow = workflowDao.get( "test process",
                                           ProjectModel.class,
                                           criterion );
    }
}
