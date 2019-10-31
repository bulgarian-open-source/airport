package sofia.service.tablecodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.IAssetClass;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import sofia.validators.NoEmotionsValidator;
import sofia.validators.NoSpacesValidator;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class ServiceStatusTest extends AbstractDaoTestCase{
    
    @Test
    public void there_are_two_instances_in_test_population() {
        assertTrue(co(ServiceStatus.class).count(select(ServiceStatus.class).model()) == 2);
    }
    
    @Test
    public void there_is_only_one_instance_ending_with_2() {
        assertTrue(co(ServiceStatus.class).count(select(ServiceStatus.class).where().prop("name").like().val("%2").model()) == 1);
    }
    
    @Test
    public void exclamation_question_mark() {
        
        final ServiceStatus ss1 = co$(ServiceStatus.class).findByKeyAndFetch(IServiceStatus.FETCH_PROVIDER.fetchModel(), "SS1");
        assertTrue(ss1.isValid().isSuccessful());    
        
        ss1.setName("name?");
        assertFalse(ss1.isValid().isSuccessful());
        assertEquals(NoEmotionsValidator.ERR_NO_EMOTIONS_ALLOWED, ss1.isValid().getMessage());
        
        ss1.setName("name");
        assertTrue(ss1.isValid().isSuccessful());
        
        ss1.setName("name!");
        assertFalse(ss1.isValid().isSuccessful());
        assertEquals(NoEmotionsValidator.ERR_NO_EMOTIONS_ALLOWED, ss1.isValid().getMessage());

        
        
    }
    
    @Test
    public void service_status_name_cannot_be_shorte_than_3_characters() {
        final ServiceStatus ss1 = co$(ServiceStatus.class).findByKeyAndFetch(IServiceStatus.FETCH_PROVIDER.fetchModel(), "SS1");
        assertTrue(ss1.isValid().isSuccessful());
        
        ss1.setName("SS");
        assertFalse(ss1.isValid().isSuccessful());
        assertFalse(ss1.getProperty("name").isValid());
    }
    
    
    @Test
    public void persistent_predicates_on_abstract_entities() {
        final ServiceStatus ss1 = co(ServiceStatus.class).findByKey("SS1");
        assertNotNull(ss1);
        assertEquals("SS1", ss1.getKey().toString());
        
        assertTrue(ss1.isPersistent());
        assertTrue(ss1.isPersisted());
                
        final IServiceStatus coServiceStatus = co(ServiceStatus.class);
        final ServiceStatus ss3 = (ServiceStatus) coServiceStatus.new_().setName("SS3");
        ss3.setDesc("some value");

        assertTrue(ss3.isPersistent());
        assertFalse(ss3.isPersisted());
        
        final ServiceStatus newSS3 = co(ServiceStatus.class).save(ss3);
        assertTrue(newSS3.isPersisted());
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
        save(new_composite(ServiceStatus.class, "SS1").setDesc("The first service status"));
        save(new_composite(ServiceStatus.class, "SS2").setDesc("The second service status"));
    }


}
