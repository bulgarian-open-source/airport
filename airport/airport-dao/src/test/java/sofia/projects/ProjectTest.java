package sofia.projects;


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
import sofia.assets.Asset;
import sofia.assets.AssetDao;
import sofia.assets.AssetFinDet;
import sofia.assets.IAssetFinDet;
import sofia.projects.validators.ProjectStartAndFinishDatesValidator;
import sofia.test_config.AbstractDaoTestCase;
import sofia.test_config.UniversalConstantsForTesting;
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

public class ProjectTest extends AbstractDaoTestCase {
    
    @Test
    public void project_start_and_finish_do_not_permit_inverted_periods() {
        final Project project = co(Project.class).new_().setName("Project 1").setDesc("Project 1 description");
        project.setStartDate(date("2019-12-08 00:00:00")).setFinishDate(date("2019-11-08 00:00:00"));
        
        assertFalse(project.isValid().isSuccessful());
        assertTrue(project.getProperty("startDate").isValid());
        assertFalse(project.getProperty("finishDate").isValid());
        assertNull(project.getFinishDate());
        
        project.setStartDate(date("2019-10-08 00:00:00"));
        assertTrue(project.getProperty("startDate").isValid());
        assertTrue(project.getProperty("finishDate").isValid());
        assertEquals(date("2019-10-08 00:00:00"), project.getStartDate());
        assertEquals(date("2019-11-08 00:00:00"), project.getFinishDate());
        assertTrue(project.isValid().isSuccessful());
        
        final Project savedProject = save(project);
        assertTrue(co(Project.class).entityExists(savedProject));
    }
    
    @Test
    public void start_date_is_required_for_the_project() {
        final Project project = co(Project.class).new_().setName("Project 1").setDesc("Project 1 description");
        final Result validationResult = project.isValid();
        assertFalse(validationResult.isSuccessful());
        assertEquals("Required property [Start Date] is not specified for entity [Project].", validationResult.getMessage());
        
        project.setStartDate(date("2019-10-08 00:00:00"));
        assertTrue(project.isValid().isSuccessful());
        
    }
    
    @Test
    public void start_date_cannot_be_after_acquired_date_for_associated_assets() {
        final Project project = save(new_(Project.class).setName("PROJECT 1").setStartDate(date("2019-10-01 00:00:00")).setDesc("Project 1 description"));
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final AssetDao co$ = co$(Asset.class);
        final Asset asset1 = save(co$.new_().setDesc("first desc").setAssetType(at1));
        final Asset asset2 = save(co$.new_().setDesc("second desc").setAssetType(at1));
        final Asset asset3 = save(co$.new_().setDesc("third desc").setAssetType(at1));
        
        save(co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-10-02 00:00:00")).setProject(project));
        save(co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-11-02 00:00:00")).setProject(project));
        save(co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2020-01-02 00:00:00")).setProject(project));

        project.setStartDate(date("2019-11-01 00:00:00"));
        assertFalse(project.isValid().isSuccessful());
        assertEquals(ProjectStartAndFinishDatesValidator.ERR_OUTSIDE_NEW_PERIOD_DUE_TO_START_DATE, project.isValid().getMessage());
    }
    
    @Test
    public void finish_date_cannot_be_before_acquire_date_for_associated_assets() {
        final Project project = save(new_(Project.class).setName("PROJECT 1")
        		.setStartDate(date("2019-10-01 00:00:00"))
        		.setFinishDate(date("2020-10-01 00:00:00"))
        		.setDesc("Project 1 description"));
        final IEntityDao<AssetType> co1$ = co$(AssetType.class);
        final AssetType at1 = co1$.findByKey("AT1");
        
        final AssetDao co$ = co$(Asset.class);
        final Asset asset1 = save(co$.new_().setDesc("first desc").setAssetType(at1));
        final Asset asset2 = save(co$.new_().setDesc("second desc").setAssetType(at1));
        final Asset asset3 = save(co$.new_().setDesc("third desc").setAssetType(at1));
        
        save(co$(AssetFinDet.class).findById(asset1.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-10-02 00:00:00")).setProject(project));
        save(co$(AssetFinDet.class).findById(asset2.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2019-11-02 00:00:00")).setProject(project));
        save(co$(AssetFinDet.class).findById(asset3.getId(), IAssetFinDet.FETCH_PROVIDER.fetchModel()).setAcquireDate(date("2020-01-02 00:00:00")).setProject(project));

        project.setFinishDate(date("2020-01-01 00:00:00"));
        assertFalse(project.isValid().isSuccessful());
        assertEquals(ProjectStartAndFinishDatesValidator.ERR_OUTSIDE_NEW_PERIOD_DUE_TO_FINISH_DATE, project.isValid().getMessage());
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
        
        final AssetClass AC1 = new_composite(AssetClass.class, "AC1").setDesc("The first asset class");
        save(new_composite(AssetClass.class, "AC1").setDesc("The first asset class"));
        final IEntityDao<AssetClass> co$ = co$(AssetClass.class);
        final AssetClass ac1 = co$.findByKey("AC1");
        save(new_composite(AssetClass.class, "AC2").setDesc("The first asset class"));
        save(new_composite(AssetType.class, "AT1").setDesc("Some desc").setAssetClass(ac1));

    }

}

