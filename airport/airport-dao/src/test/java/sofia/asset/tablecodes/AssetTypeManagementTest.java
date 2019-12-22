package sofia.asset.tablecodes;


import static sofia.assets.AssetDao.ERR_FAILED_SAVE;
import static sofia.assets.AssetDao.DEFAULT_ASSET_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.asset.tablecodes.IAssetClass;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import sofia.validators.NoSpacesValidator;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetTypeManagement}
 * 
 * @author Sofia-team
 *
 */


public class AssetTypeManagementTest extends AbstractDaoTestCase {
    
    @Test
    public void management_with_either_role_or_bu_or_org_is_acceptable() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetTypeManagement o1 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-01 00:00:00"))
                .setRole(r1));
        
        final AssetTypeManagement o2 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-02 00:00:00"))
                .setBu(bu1));
        
        final AssetTypeManagement o3 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-12-03 00:00:00"))
                .setOrg(org1));
    }
    
    @Test
    public void different_managers_for_the_same_asset_type_on_the_same_date_is_not_permitted() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");

        final AssetTypeManagement o1 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setRole(r1)
                .setStartDate(date("2019-12-01 00:00:00")));
        
        try {
            final AssetTypeManagement o2 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setBu(bu1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate managements");
        }
        catch(final EntityAlreadyExists ex){
        }
        
        try {
            final AssetTypeManagement o3 =  save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setOrg(org1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate managements");
        }
        catch(final EntityAlreadyExists ex) {
        }
    }
    
    
    @Test
    public void exclusivity_holds_for_management_properties_for_new_non_persistent_instance() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetTypeManagement management =  co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setStartDate(date("2019-11-01 00:00:00"));
        
        
        assertFalse(management.isValid().isSuccessful());
        assertNull(management.getRole());
        assertTrue(management.getProperty("role").isRequired());
        assertNull(management.getBu());
        assertTrue(management.getProperty("bu").isRequired());
        assertNull(management.getOrg());
        assertTrue(management.getProperty("org").isRequired());

        management.setRole(r1);
        assertTrue(management.isValid().isSuccessful());
        assertNotNull(management.getRole());
        assertTrue(management.getProperty("role").isRequired());
        assertNull(management.getBu());
        assertFalse(management.getProperty("bu").isRequired());
        assertNull(management.getOrg());
        assertFalse(management.getProperty("org").isRequired());
        
        management.setBu(bu1);
        assertTrue(management.isValid().isSuccessful());
        assertNull(management.getRole());
        assertFalse(management.getProperty("role").isRequired());
        assertNotNull(management.getBu());
        assertTrue(management.getProperty("bu").isRequired());
        assertNull(management.getOrg());
        assertFalse(management.getProperty("org").isRequired());
        
        management.setOrg(org1);
        assertTrue(management.isValid().isSuccessful());
        assertNull(management.getRole());
        assertFalse(management.getProperty("role").isRequired());
        assertNull(management.getBu());
        assertFalse(management.getProperty("bu").isRequired());
        assertNotNull(management.getOrg());
        assertTrue(management.getProperty("org").isRequired());        
    }
    
    
    @Test
    public void exclusivity_holds_for_management_properties_for_persisted_instance() {
        final AssetType at1 = co(AssetType.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        save(co(AssetTypeManagement.class).new_()
                .setAssetType(at1)
                .setRole(r1)
                .setStartDate(date("2019-11-01 00:00:00")));
        
        final AssetTypeManagement management = co$(AssetTypeManagement.class).findByKeyAndFetch(IAssetTypeManagement.FETCH_PROVIDER.fetchModel(), at1, date("2019-11-01 00:00:00"));
        assertNotNull(management);
                
        assertTrue(management.isValid().isSuccessful());
        assertNotNull(management.getRole());
        assertTrue(management.getProperty("role").isRequired());
        assertNull(management.getBu());
        assertFalse(management.getProperty("bu").isRequired());
        assertNull(management.getOrg());
        assertFalse(management.getProperty("org").isRequired());
        
        management.setBu(bu1);
        assertTrue(management.isValid().isSuccessful());
        assertNull(management.getRole());
        assertFalse(management.getProperty("role").isRequired());
        assertNotNull(management.getBu());
        assertTrue(management.getProperty("bu").isRequired());
        assertNull(management.getOrg());
        assertFalse(management.getProperty("org").isRequired());
        
        management.setOrg(org1);
        assertTrue(management.isValid().isSuccessful());
        assertNull(management.getRole());
        assertFalse(management.getProperty("role").isRequired());
        assertNull(management.getBu());
        assertFalse(management.getProperty("bu").isRequired());
        assertNotNull(management.getOrg());
        assertTrue(management.getProperty("org").isRequired());   
        
        assertNotNull(save(management).getOrg());
    }
    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    @Override
    public boolean useSavedDataPopulationScript() {
        return true;
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

        // population for the test case
        
        final AssetClass ac1 = save(new_(AssetClass.class).setName("AC1").setDesc("First Asset Class").setActive(true));
        save(new_(AssetType.class).setName("AT1").setAssetClass(ac1).setDesc("First Asset Type").setActive(true));
        
        
        save(new_(Role.class).setName("R1").setDesc("First role"));
        save(new_(BusinessUnit.class).setName("BU1").setDesc("First business unit"));
        save(new_(Organization.class).setName("ORG1").setDesc("First organization"));

    }

}

