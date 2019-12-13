package sofia.service.tablecodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class ConditionRatingTest extends AbstractDaoTestCase{
    
    @Test
    public void persistent_predicates_on_abstract_entities() {
        final ConditionRating cr1 = co(ConditionRating.class).findByKey("10");
        assertNotNull(cr1);
        assertEquals("10", cr1.getKey().toString());
        
        assertTrue(cr1.isPersistent());
        assertTrue(cr1.isPersisted());
                
        final IConditionRating coConditionRating = co(ConditionRating.class);
        final ConditionRating cr3 = (ConditionRating) coConditionRating.new_().setRating("7");
        cr3.setDesc("some value");

        assertTrue(cr3.isPersistent());
        assertFalse(cr3.isPersisted());
        
        final ConditionRating newCR3 = co(ConditionRating.class).save(cr3);
        assertTrue(newCR3.isPersisted());
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
        save(new_composite(ConditionRating.class, "10").setDesc("The first condition rating"));
        save(new_composite(ConditionRating.class, "9").setDesc("The second condition rating"));
    }

}
