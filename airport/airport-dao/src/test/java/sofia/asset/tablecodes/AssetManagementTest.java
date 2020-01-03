package sofia.asset.tablecodes;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import sofia.assets.Asset;
import sofia.assets.IAsset;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link AssetTypeManagement}
 * 
 * @author Sofia-team
 *
 */


public class AssetManagementTest extends AbstractDaoTestCase {
    
    @Test
    public void management_with_either_role_or_bu_or_org_is_acceptable() {
        final Asset a1 = co(Asset.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000001");
        assertNotNull(a1);
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetManagement o1 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setStartDate(date("2019-12-01 00:00:00"))
                .setRole(r1));
        
        final AssetManagement o2 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setStartDate(date("2019-12-02 00:00:00"))
                .setBu(bu1));
        
        final AssetManagement o3 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setStartDate(date("2019-12-03 00:00:00"))
                .setOrg(org1));
    }
    
    @Test
    public void different_management_for_the_same_asset_type_on_the_same_date_is_not_permitted() {
        final Asset a1 = co(Asset.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000001");
        assertNotNull(a1);
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");

        final AssetManagement o1 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setRole(r1)
                .setStartDate(date("2019-12-01 00:00:00")));
        
        try {
            final AssetManagement o2 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setBu(bu1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate managements");
        }
        catch(final EntityAlreadyExists ex){
        }
        
        try {
            final AssetManagement o3 =  save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setOrg(org1)
                .setStartDate(date("2019-12-01 00:00:00")));
                fail("Error was expected due to duplicate managements");
        }
        catch(final EntityAlreadyExists ex) {
        }
    }
    
    
    @Test
    public void exclusivity_holds_for_management_properties_for_new_non_persistent_instance() {
        final Asset a1 = co(Asset.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000001");
        assertNotNull(a1);
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        final AssetManagement management =  co(AssetManagement.class).new_()
                .setAsset(a1)
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
        final Asset a1 = co(Asset.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000001");
        assertNotNull(a1);
        final Role r1 = co(Role.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
        final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
        final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
        
        save(co(AssetManagement.class).new_()
                .setAsset(a1)
                .setRole(r1)
                .setStartDate(date("2019-11-01 00:00:00")));
        
        final AssetManagement management = co$(AssetManagement.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.fetchModel(), a1, date("2019-11-01 00:00:00"));
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
    
      @Test
      public void currManagement_for_asset_and_asset_type_correspond_to_each_other() {
          final Asset a1 = co(Asset.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000001");
          assertNotNull(a1);
          final Role r1 = co(Role.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Role>fetchFor("role").fetchModel(), "R1");
          final BusinessUnit bu1 = co(BusinessUnit.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<BusinessUnit>fetchFor("bu").fetchModel(), "BU1");
          final Organization org1 = co(Organization.class).findByKeyAndFetch(IAssetManagement.FETCH_PROVIDER.<Organization>fetchFor("org").fetchModel(), "ORG1");
          

          final AssetManagement o0 =  save(co(AssetManagement.class).new_()
                  .setAsset(a1)
                  .setStartDate(date("2019-11-12 00:00:00"))
                  .setOrg(org1));
          
          final AssetManagement o1 =  save(co(AssetManagement.class).new_()
                  .setAsset(a1)
                  .setStartDate(date("2019-12-01 00:00:00"))
                  .setRole(r1));
          
          final AssetManagement o2 =  save(co(AssetManagement.class).new_()
                  .setAsset(a1)
                  .setStartDate(date("2019-12-02 00:00:00"))
                  .setBu(bu1));
          
          final AssetManagement o3 =  save(co(AssetManagement.class).new_()
                  .setAsset(a1)
                  .setStartDate(date("2019-12-13 00:00:00"))
                  .setOrg(org1));
          
          final AssetManagement o4 =  save(co(AssetManagement.class).new_()
                  .setAsset(a1)
                  .setStartDate(date("2020-01-01 00:00:00"))
                  .setBu(bu1));
          
          final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
          constants.setNow(dateTime("2019-11-01 11:30:00"));
          
          final Asset a1WithCurrManagement1 = co(Asset.class).findById(a1.getId(), IAsset.FETCH_PROVIDER.fetchModel());
          assertNull(a1WithCurrManagement1.getCurrManagement());
          assertNull(a1WithCurrManagement1.getAssetType().getCurrManagement());
          
          constants.setNow(dateTime("2019-11-15 11:30:00"));
          final Asset a1WithCurrManagement2 = co(Asset.class).findById(a1.getId(), IAsset.FETCH_PROVIDER.fetchModel());
          assertEquals(o0, a1WithCurrManagement2.getCurrManagement());
          assertNull(a1WithCurrManagement2.getAssetType().getCurrManagement());
          
          constants.setNow(dateTime("2019-12-01 11:30:00"));
          final Asset a1WithCurrManagement3 = co(Asset.class).findById(a1.getId(), IAsset.FETCH_PROVIDER.fetchModel());
          assertEquals(o1, a1WithCurrManagement3.getCurrManagement());
          final AssetTypeManagement assetTypeManagement1 = co(AssetTypeManagement.class)
                  .findByKeyAndFetch(IAsset.FETCH_PROVIDER.<AssetTypeManagement>fetchFor("assetType.currManagement").fetchModel(), 
                  a1WithCurrManagement3.getAssetType(), date("2019-12-01 00:00:00"));
          
          assertEquals(assetTypeManagement1, a1WithCurrManagement3.getAssetType().getCurrManagement());
                    
          constants.setNow(dateTime("2020-02-01 11:30:00"));
          final Asset a1WithCurrManagement4 = co(Asset.class).findById(a1.getId(), IAsset.FETCH_PROVIDER.fetchModel());
          assertEquals(o4, a1WithCurrManagement4.getCurrManagement());
          final AssetTypeManagement assetTypeManagement2 = co(AssetTypeManagement.class)
                  .findByKeyAndFetch(IAsset.FETCH_PROVIDER.<AssetTypeManagement>fetchFor("assetType.currManagement").fetchModel(), 
                  a1WithCurrManagement3.getAssetType(), date("2019-12-03 00:00:00"));
          
          assertEquals(assetTypeManagement2, a1WithCurrManagement4.getAssetType().getCurrManagement());
          
          
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
        constants.setNow(dateTime("2019-12-01 11:30:00"));

        // If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }

        // population for the test case
        
        final AssetClass ac1 = save(new_(AssetClass.class).setName("AC1").setDesc("First Asset Class").setActive(true));
        final AssetType at1 = save(new_(AssetType.class).setName("AT1").setAssetClass(ac1).setDesc("First Asset Type").setActive(true));
        
        
        final Role r1 = save(new_(Role.class).setName("R1").setDesc("First role"));
        final BusinessUnit bu1 = save(new_(BusinessUnit.class).setName("BU1").setDesc("First business unit"));
        final Organization org1 = save(new_(Organization.class).setName("ORG1").setDesc("First organization"));
        
        final IAsset co$ = co$(Asset.class);
        save(co$.new_().setDesc("description").setAssetType(at1).setActive(true));
        
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

}

