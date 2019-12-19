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

public class AssetTest extends AbstractDaoTestCase {
    
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
    
    @Test
    public void fin_det_is_created_and_saved_at_the_same_time_as_asset() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setAssetType(at1).setDesc("lgajabglb");
        final Asset savedAsset = co2$.save(asset);
        assertTrue(co(Asset.class).entityExists(savedAsset));
        assertTrue(co(AssetFinDet.class).entityExists(savedAsset.getId()));
    }
    
    @Test
    public void no_findet_created_when_existing_asset_saved() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");

        final IAsset co2$ = co$(Asset.class);
      
        final Asset asset = co2$.new_().setAssetType(at1).setDesc("kjhgieahg");
        final Asset savedAsset = co2$.save(asset);
        assertEquals(Long.valueOf(0), savedAsset.getVersion());

        final AssetFinDet finDet = co(AssetFinDet.class).findById(savedAsset.getId());
        assertEquals(Long.valueOf(0), finDet.getVersion());

        assertEquals(Long.valueOf(1), save(savedAsset.setDesc("description").setAssetType(at1)).getVersion());
        assertEquals(Long.valueOf(0), co(AssetFinDet.class).findById(finDet.getId()).getVersion());
    }
    
   @Test
   public void duplicate_fin_det_for_the_same_asset_is_not_permited() {
         final IEntityDao<AssetType> co1$ = co$(AssetType.class);
         final AssetType at1 = co1$.findByKey("AT1");

         final IAsset co2$ = co$(Asset.class);
          
         final Asset asset = co2$.new_().setAssetType(at1).setDesc("AS");
         final Asset savedAsset = co2$.save(asset);
            
         final AssetFinDet newFinDet = new_(AssetFinDet.class).setKey(savedAsset);
         try {
              save(newFinDet);
              fail("Should have failed due to duplicate instances.");
            } catch(final EntityAlreadyExists ex) {
            }
        }
    
    @Test
    public void can_find_asset_by_fin_det_information() {
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final Asset asset1 = save(new_(Asset.class).setDesc("demo asset 1").setAssetType(at1));
        final Asset asset2 = save(new_(Asset.class).setDesc("demo asset 2").setAssetType(at1));
        final Asset asset3 = save(new_(Asset.class).setDesc("demo asset 3").setAssetType(at1));
        
        final AssetFinDet finDet1 = co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet1.setInitCost(Money.of("120.00")).setAcquireDate(date("2019-12-07 00:00:00")));
        final AssetFinDet finDet2 = co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet2.setInitCost(Money.of("100.00")).setAcquireDate(date("2019-11-07 00:00:00")));
        final AssetFinDet finDet3 = co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel());
        save(finDet3.setInitCost(Money.of("10.00")).setAcquireDate(date("2018-11-07 00:00:00")));
        

        final QueryExecutionModel<Asset, EntityResultQueryModel<Asset>> qFindWithLessOrEqual100 = 
                from(select(Asset.class).where().prop("finDet.initCost").le().val(Money.of("100.00")).model())
                .with(fetch(Asset.class).with("number", "desc", "finDet.initCost", "finDet.acquireDate").fetchModel())
                .with(orderBy().prop("finDet.initCost").asc().model()).model();

        final List<Asset> assets = co(Asset.class).getAllEntities(qFindWithLessOrEqual100);
        assertEquals(2, assets.size());
        
        assertEquals(Money.of("10.00"), assets.get(0).getFinDet().getInitCost());
        assertEquals("000003", assets.get(0).getNumber());
        assertEquals(Money.of("100.00"), assets.get(1).getFinDet().getInitCost());
        assertEquals("000002", assets.get(1).getNumber());
        
        assets.forEach(System.out::println);
        
        final QueryExecutionModel<Asset, EntityResultQueryModel<Asset>> qFindWithGreater100 = 
                from(select(Asset.class).where().prop("finDet.initCost").gt().val(Money.of("100.00")).model())
                .with(fetch(Asset.class).with("number", "desc", "finDet.initCost", "finDet.acquireDate").fetchModel())
                .with(orderBy().prop("finDet.initCost").asc().model()).model();

        final List<Asset> assetsMoreExpensiveThan100 = co(Asset.class).getAllEntities(qFindWithGreater100);
        assertEquals(1, assetsMoreExpensiveThan100.size());
        
        assertEquals(Money.of("120.00"), assetsMoreExpensiveThan100.get(0).getFinDet().getInitCost());
        assertEquals("000001", assetsMoreExpensiveThan100.get(0).getNumber());

        

        
        
        

        

        

        
        
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

