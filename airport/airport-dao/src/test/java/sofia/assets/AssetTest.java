package sofia.assets;


import static sofia.assets.AssetDao.ERR_FAILED_SAVE;
import static sofia.assets.AssetDao.DEFAULT_ASSET_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.cond;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.expr;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAggregates;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAll;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllInclCalc;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllInclCalcAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchIdOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchKeyAndDescOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchKeyAndDescOnlyAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchOnly;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchOnlyAndInstrument;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;
import static ua.com.fielden.platform.utils.EntityUtils.fetch;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.asset.tablecodes.IAssetClass;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
import sofia.validators.NoSpacesValidator;
import sofia.validators.RateRangeValidator;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.dao.QueryExecutionModel;
import ua.com.fielden.platform.dao.exceptions.EntityAlreadyExists;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.IUniversalConstants;

/**
 * This is a test case for {@link Asset}
 * 
 * @author Sofia-team
 *
 */


public class AssetTest extends AbstractDaoTestCase {
    
    
    @Test
    public void asset_has_correct_load_rate() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setDesc("some asset desc").setAssetType(at1);
        
        asset.setLoadingRate("zero");
        assertFalse(asset.isValid().isSuccessful());
        assertEquals(RateRangeValidator.NOT_A_NUMBER, asset.isValid().getMessage());
        
        asset.setLoadingRate("120");
        assertFalse(asset.isValid().isSuccessful());
        assertEquals(RateRangeValidator.INCORRECT_RANGE, asset.isValid().getMessage());
        
        
        asset.setLoadingRate("50");
        assertTrue(asset.isValid().isSuccessful());    
    }
    
    @Test
    public void newly_saved_asset_has_number_generated() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setDesc("some asset desc").setAssetType(at1);
        final Asset savedAsset = co2$.save(asset);
        
        
        assertNotNull(asset.getNumber());
        assertEquals("000001", savedAsset.getNumber());
    }
    
    @Test
    public void existing_assets_keep_their_original_numbers() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final IAsset co$ = co$(Asset.class);
        final Asset asset = co$.new_().setDesc("some description").setAssetType(at1);
        
        final Asset savedAsset = co$.save(asset).setDesc("another description").setAssetType(at1);
        assertTrue(savedAsset.isDirty());
        
        final Asset savedAsset2 = co$.save(savedAsset);
        assertEquals("000001", savedAsset2.getNumber());
    }
    
    @Test
    public void sequentially_created_assets_have_sequential_numbers() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final IAsset co$ = co$(Asset.class);
        final Asset asset1 = co$.save(co$.new_().setDesc("asset 1 description").setAssetType(at1));
        final Asset asset2 = co$.save(co$.new_().setDesc("asset 2 description").setAssetType(at1));
        
        assertEquals("000001", asset1.getNumber());
        assertEquals("000002", asset2.getNumber());
    }
    
    @Test
    public void new_asset_can_be_saved_after_the_first_failed_attempt() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final AssetDao co$ = co$(Asset.class);
        final Asset asset = co$.new_().setDesc("new desc").setAssetType(at1);
        System.out.println(asset.getNumber());
        // the first attempt to save asset should fail
        try {
            co$.saveWithError(asset);
            fail("Should have failed the first saving attempt.");
        } catch (final Result ex) {
            assertEquals(ERR_FAILED_SAVE, ex.getMessage());
        }
        
        System.out.println(asset.isPersisted());
        System.out.println(asset.getNumber());
        
        assertFalse(asset.isPersisted());
        assertEquals(DEFAULT_ASSET_NUMBER, asset.getNumber());
        
        final Asset savedAsset = co$.save(asset);
        assertTrue(savedAsset.isPersisted());
        assertTrue(co$.entityExists(savedAsset));
        assertEquals("000001", savedAsset.getNumber());
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

        // AssetClass population for the test case
        final AssetClass AC1 = new_composite(AssetClass.class, "AC1").setDesc("The first asset class");
        save(new_composite(AssetClass.class, "AC1").setDesc("The first asset class"));
        final IEntityDao<AssetClass> co$ = co$(AssetClass.class);
        final AssetClass ac1 = co$.findByKey("AC1");
        save(new_composite(AssetClass.class, "AC2").setDesc("The first asset class"));
        save(new_composite(AssetType.class, "AT1").setDesc("Some desc").setAssetClass(ac1));
    }

}

