package assets;

import static org.junit.Assert.*;

import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.assets.Asset;
import sofia.assets.AssetFinDet;
import sofia.assets.IAsset;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class FinDetTest extends AbstractDaoTestCase {

	@Test
	public void fin_det_is_created_and_saved_at_the_same_time_as_asset() {
		final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setAssetType(at1);
        final Asset savedAsset = co2$.save(asset);
        assertTrue(co(Asset.class).entityExists(savedAsset));
        assertTrue(co(AssetFinDet.class).entityExists(savedAsset.getId()));
	}
	
	@Test
	public void no_findet_created_when_existing_asset_saved() {
		final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setAssetType(at1);
        final Asset savedAsset = co2$.save(asset);
        assertEquals(Long.valueOf(0), savedAsset.getVersion());

        final AssetFinDet finDet = co(AssetFinDet.class).findById(savedAsset.getId());
        assertEquals(Long.valueOf(0), finDet.getVersion());

        assertEquals(Long.valueOf(1), save(savedAsset.setDesc("description")).getVersion());
        assertEquals(Long.valueOf(0), co(AssetFinDet.class).findById(finDet.getId()).getVersion());
    }
	
	 @Test
	    public void duplicate_fin_det_for_the_same_asset_is_not_permited() {
		 final IEntityDao<AssetType> co1$ = co$(AssetType.class);
	        final AssetType at1 = co1$.findByKey("AT1");

	        final IAsset co2$ = co$(Asset.class);
	      
	        final Asset asset = co2$.new_().setAssetType(at1);
	        final Asset savedAsset = co2$.save(asset);
	        
	        final AssetFinDet newFinDet = new_(AssetFinDet.class).setKey(savedAsset);
	        try {
	            save(newFinDet);
	            fail("Should have failed due to duplicate instances.");
	        } catch(final EntityAlreadyExists ex) {
	        }
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
