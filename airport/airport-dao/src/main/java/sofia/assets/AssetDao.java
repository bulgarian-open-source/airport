package sofia.assets;

import ua.com.fielden.platform.error.Result;
import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.keygen.IKeyNumber;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAsset}.
 *
 * @author Developers
 *
 */
@EntityType(Asset.class)
public class AssetDao extends CommonEntityDao<Asset> implements IAsset {
	public static final String ERR_FAILED_SAVE = "Deliberate save exception.";

    public static final String DEFAULT_ASSET_NUMBER = "NEXT NUMBER WILL BE GENERATED UPON SAVE.";
    private boolean throwExceptionForTestingPurposes = false;

    @Inject
    public AssetDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
        public Asset new_() {
            // TODO Auto-generated method stub
            final Asset asset =  super.new_();
            asset.setNumber(DEFAULT_ASSET_NUMBER);
            return asset;
        }
    
    @Override
    @SessionRequired
    public Asset save(final Asset asset) {
        final boolean wasPersisted = asset.isPersisted();
        try {
            if (!wasPersisted) {
                final IKeyNumber coKeyNumber = co(KeyNumber.class);
                final Integer nextNumber = coKeyNumber.nextNumber("ASSET_NUMBER");
                asset.setNumber(nextNumber.toString());
            }

            // save asset
            final Asset savedAsset = super.save(asset);
            if (!wasPersisted) {
                final AssetFinDet finDet = co(AssetFinDet.class).new_().setKey(savedAsset);
                co$(AssetFinDet.class).save(finDet);
            }

            // simulating a situation with an exception for testing purposes
            if (throwExceptionForTestingPurposes) {
                throw Result.failure(ERR_FAILED_SAVE);
            }

            // if no exception occurred then simply return the saved instance
            return savedAsset;
        } catch (final Exception ex) {
            // if there was an exception when saving a new asset we need reset the value of its number to the default value
            if (!wasPersisted) {
                asset.setNumber(DEFAULT_ASSET_NUMBER);
            }
            // and re-throw the exception
            throw ex;
        }
    }

    @Override
    @SessionRequired
    public int batchDelete(final Collection<Long> entitiesIds) {
        return defaultBatchDelete(entitiesIds);
    }

    @Override
    @SessionRequired
    public int batchDelete(final List<Asset> entities) {
        return defaultBatchDelete(entities);
    }

    @Override
    protected IFetchProvider<Asset> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAsset.FETCH_PROVIDER. Then remove the line after.
      return FETCH_PROVIDER;
    }}