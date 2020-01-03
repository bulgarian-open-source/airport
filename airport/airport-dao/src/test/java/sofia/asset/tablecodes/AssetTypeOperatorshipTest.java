package sofia.asset.tablecodes;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetTypeOperatorship}
 * 
 * @author Sofia-team
 *
 */


public class AssetTypeOperatorshipTest extends AbstractDaoTestCase {
    
    @Test
    public void operatorship_with_either_role_or_bu_or_org_is_acceptable() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetTypeOperatorship o1 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-01 00:00:00"))
                .setRole(r1));
        
        final AssetTypeOperatorship o2 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-02 00:00:00"))
                .setBu(bu1));
        
        final AssetTypeOperatorship o3 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-03 00:00:00"))
                .setOrg(org1));
    }
    
    @Test
    public void different_operatorship_for_the_same_asset_type_on_the_same_date_is_not_permitted() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");

        final AssetTypeOperatorship o1 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setRole(r1)
                .setStartDate(date("2019-12-01 00:00:00")));
        
        try {
            final AssetTypeOperatorship o2 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setBu(bu1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate operatorships");
        }
        catch(final EntityAlreadyExists ex){
        }
        
        try {
            final AssetTypeOperatorship o3 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setOrg(org1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate operatorships");
        }
        catch(final EntityAlreadyExists ex) {
        }
    }
    
    
    @Test
    public void exclusivity_holds_for_operatorship_properties_for_new_non_persistent_instance() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetTypeOperatorship operatorship =  co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-11-01 00:00:00"));
                
        assertFalse(operatorship.isValid().isSuccessful());
        assertNull(operatorship.getRole());
        assertTrue(operatorship.getProperty("role").isRequired());
        assertNull(operatorship.getBu());
        assertTrue(operatorship.getProperty("bu").isRequired());
        assertNull(operatorship.getOrg());
        assertTrue(operatorship.getProperty("org").isRequired());

        operatorship.setRole(r1);
        assertTrue(operatorship.isValid().isSuccessful());
        assertNotNull(operatorship.getRole());
        assertTrue(operatorship.getProperty("role").isRequired());
        assertNull(operatorship.getBu());
        assertFalse(operatorship.getProperty("bu").isRequired());
        assertNull(operatorship.getOrg());
        assertFalse(operatorship.getProperty("org").isRequired());
        
        operatorship.setBu(bu1);
        assertTrue(operatorship.isValid().isSuccessful());
        assertNull(operatorship.getRole());
        assertFalse(operatorship.getProperty("role").isRequired());
        assertNotNull(operatorship.getBu());
        assertTrue(operatorship.getProperty("bu").isRequired());
        assertNull(operatorship.getOrg());
        assertFalse(operatorship.getProperty("org").isRequired());
        
        operatorship.setOrg(org1);
        assertTrue(operatorship.isValid().isSuccessful());
        assertNull(operatorship.getRole());
        assertFalse(operatorship.getProperty("role").isRequired());
        assertNull(operatorship.getBu());
        assertFalse(operatorship.getProperty("bu").isRequired());
        assertNotNull(operatorship.getOrg());
        assertTrue(operatorship.getProperty("org").isRequired());        
    }
    
    
    @Test
    public void exclusivity_holds_for_operatorship_properties_for_persisted_instance() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setRole(r1)
                .setStartDate(date("2019-11-01 00:00:00")));
        
        final AssetTypeOperatorship operatorship = co$(AssetTypeOperatorship.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.fetchModel(), at1, date("2019-11-01 00:00:00"));
        assertNotNull(operatorship);
                
        assertTrue(operatorship.isValid().isSuccessful());
        assertNotNull(operatorship.getRole());
        assertTrue(operatorship.getProperty("role").isRequired());
        assertNull(operatorship.getBu());
        assertFalse(operatorship.getProperty("bu").isRequired());
        assertNull(operatorship.getOrg());
        assertFalse(operatorship.getProperty("org").isRequired());
        
        operatorship.setBu(bu1);
        assertTrue(operatorship.isValid().isSuccessful());
        assertNull(operatorship.getRole());
        assertFalse(operatorship.getProperty("role").isRequired());
        assertNotNull(operatorship.getBu());
        assertTrue(operatorship.getProperty("bu").isRequired());
        assertNull(operatorship.getOrg());
        assertFalse(operatorship.getProperty("org").isRequired());
        
        operatorship.setOrg(org1);
        assertTrue(operatorship.isValid().isSuccessful());
        assertNull(operatorship.getRole());
        assertFalse(operatorship.getProperty("role").isRequired());
        assertNull(operatorship.getBu());
        assertFalse(operatorship.getProperty("bu").isRequired());
        assertNotNull(operatorship.getOrg());
        assertTrue(operatorship.getProperty("org").isRequired());   
        
        assertNotNull(save(operatorship).getOrg());
    }
    
    @Test
    public void currOperatorship_for_asset_type_finds_the_latest_operatorship() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeOperatorship.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        

        final AssetTypeOperatorship o0 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-11-12 00:00:00"))
                .setOrg(org1));
        
        final AssetTypeOperatorship o1 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-01 00:00:00"))
                .setRole(r1));
        
        final AssetTypeOperatorship o2 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-02 00:00:00"))
                .setBu(bu1));
        
        final AssetTypeOperatorship o3 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-13 00:00:00"))
                .setOrg(org1));
        
        final AssetTypeOperatorship o4 =  save(co(AssetTypeOperatorship.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2020-01-01 00:00:00"))
                .setBu(bu1));
        
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(dateTime("2019-11-01 11:30:00"));
        
        final AssetType at1WithCurrOperatorship1 = co(AssetType.class).findById(at1.getId(), EntityUtils.fetch(AssetType.class).with("currOperatorship").fetchModel());
        assertNull(at1WithCurrOperatorship1.getCurrOperatorship());
        
        constants.setNow(dateTime("2019-11-15 11:30:00"));
        final AssetType at1WithCurrOperatorship2 = co(AssetType.class).findById(at1.getId(), EntityUtils.fetch(AssetType.class).with("currOperatorship").fetchModel());
        assertEquals(o0, at1WithCurrOperatorship2.getCurrOperatorship());
        
        constants.setNow(dateTime("2019-12-01 11:30:00"));
        final AssetType at1WithCurrOperatorship3 = co(AssetType.class).findById(at1.getId(), EntityUtils.fetch(AssetType.class).with("currOperatorship").fetchModel());
        assertEquals(o1, at1WithCurrOperatorship3.getCurrOperatorship());
        
        constants.setNow(dateTime("2020-02-01 11:30:00"));
        final AssetType at1WithCurrOperatorship4 = co(AssetType.class).findById(at1.getId(), EntityUtils.fetch(AssetType.class).with("currOperatorship").fetchModel());
        assertEquals(o4, at1WithCurrOperatorship4.getCurrOperatorship());   
    }
    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return true;
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
        constants.setNow(dateTime("2019-12-01 11:30:00"));

        // If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }

        // population for the test case
        
        final AssetClass ac1 = save(new_(AssetClass.class).setName("AC1").setDesc("First Asset Class").setActive(true));
        save(new_(AssetType.class).setName("AT1").setAssetClass(ac1).setDesc("First Asset Type").setActive(true));
        
        
        save(new_(Role.class).setName("R1").setDesc("First role"));
        save(new_(BusinessUnit.class).setName("BU1").setDesc("First business unit"));
        save(new_(Organization.class).setName("ORG1").setDesc("First organization"));

    }

}

