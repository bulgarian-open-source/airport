package sofia.service.tablecodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.assets.Asset;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class AssetServiceStatusTest extends AbstractDaoTestCase{
    
    @Test
    public void service_status_is_required() {
        final AssetType at1 = co(AssetType.class).findByKey("AT1");
        final ServiceStatus ss1 = co(ServiceStatus.class).findByKey("SS1");
        final Asset savedAsset = save(co(Asset.class).new_().setAssetType(at1).setActive(true).setDesc("First asset"));

               
       final AssetServiceStatus assetServiceStatus = co(AssetServiceStatus.class).new_()
                                                     .setAsset(savedAsset)
                                                     .setStartDate(date("2019-01-01 00:00:00"));
       
       assertFalse(assetServiceStatus.isValid().isSuccessful());
       assertNull(assetServiceStatus.getProperty("serviceStatus").getValue());
       
       assetServiceStatus.setServiceStatus(ss1);
       assertTrue(assetServiceStatus.isValid().isSuccessful());
       assertNotNull(assetServiceStatus.getProperty("serviceStatus").getValue());
       
        
    }
    
    
    @Test
    public void start_date_is_required() {
        final AssetType at1 = co(AssetType.class).findByKey("AT1");
        final ServiceStatus ss1 = co(ServiceStatus.class).findByKey("SS1");
        final Asset savedAsset = save(co(Asset.class).new_().setAssetType(at1).setActive(true).setDesc("First asset"));

               
       final AssetServiceStatus assetServiceStatus = co(AssetServiceStatus.class).new_()
                                                     .setAsset(savedAsset)
                                                     .setServiceStatus(ss1);
   
       assertFalse(assetServiceStatus.isValid().isSuccessful());
       assertNull(assetServiceStatus.getProperty("startDate").getValue());
       
       assetServiceStatus.setStartDate(date("2019-01-01 00:00:00"));
       assertTrue(assetServiceStatus.isValid().isSuccessful());
       assertNotNull(assetServiceStatus.getProperty("startDate").getValue());
        
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
        save(new_composite(AssetClass.class, "AC1").setDesc("The first asset class").setActive(true));
        save(new_composite(AssetType.class, "AT1").setDesc("First asset type").setAssetClass(co$(AssetClass.class).findByKey("AC1")).setActive(true));

        // Service Status population for the test case
        save(new_composite(ServiceStatus.class, "SS1").setDesc("The first service status"));
    }


}
