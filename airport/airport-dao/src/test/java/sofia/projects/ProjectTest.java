package sofia.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class ProjectTest extends AbstractDaoTestCase {
    @Test
    public void project_start_is_before_finish() {
        final Project project = (Project) co(Project.class).new_().setName("TestProject").setDesc("Some project");
        project.setStartDate(date("2019-12-19 00:00:00")).setFinishDate(date("2019-12-10 00:00:00"));
        		
        assertFalse(project.isValid().isSuccessful());
        assertTrue(project.getProperty("startDate").isValid());
        assertFalse(project.getProperty("finishDate").isValid());
        
        project.setStartDate(date("2019-12-01 00:00:00"));
        assertTrue(project.isValid().isSuccessful());
        assertTrue(project.getProperty("startDate").isValid());
        assertTrue(project.getProperty("finishDate").isValid());
        
        assertEquals(project.getStartDate(), date("2019-12-01 00:00:00"));
        assertEquals(project.getFinishDate(), date("2019-12-10 00:00:00"));
        
        final Project savedProject = save(project);
        assertTrue(savedProject.isPersisted());
        
        
    }
    
    @Test
    public void start_date_is_required() {
    	 final Project project = (Project) co(Project.class).new_().setName("TestProject").setDesc("Some project");
    	 assertFalse(project.isValid().isSuccessful());
    	 
    	 project.setStartDate(date("2019-01-01 00:00:00"));
    	 assertTrue(project.isValid().isSuccessful());
    }
    
    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    @Override
    public boolean useSavedDataPopulationScript() {
        return false;
    }

    @Override
    protected void populateDomain() {
        // Need to invoke super to create a test user that is responsible for data population 
        super.populateDomain();

        // Here is how the Test Case universal constants can be set.
        // In this case the notion of now is overridden, which makes it possible to have an invariant system-time.
        // However, the now value should be after AbstractDaoTestCase.prePopulateNow in order not to introduce any date-related conflicts.
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(dateTime("2019-10-01 11:30:00"));

        // If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }

        // AssetClass population for the test case
        final AssetClass AC1 = new_composite(AssetClass.class, "AC1").setDesc("The first asset class");
        save(new_composite(AssetClass.class, "AC1").setDesc("The first asset class"));
        final IEntityDao<AssetClass> co$ = co$(AssetClass.class);
        final AssetClass ac1 = co$.findByKey("AC1");
        save(new_composite(AssetClass.class, "AC2").setDesc("The first asset class"));
        save(new_composite(AssetType.class, "AT1").setDesc("Some desc").setAssetClass(ac1));
    }

}

