package sofia.assets;

import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;

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
    
    public static final String DEFAULT_ASSET_NUMBER = "NEXT NUMBER WILL BE GENERATED UPON SAVE.";

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
    public Asset save(Asset asset) {
        // TODO Auto-generated method stub
        if (!asset.isPersisted()) {
            final IKeyNumber coKeyNumber = co(KeyNumber.class);
            final Integer nextNumber = coKeyNumber.nextNumber("ASSET_NUMBER");
            asset.setNumber(nextNumber.toString());
            }
        return super.save(asset);
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