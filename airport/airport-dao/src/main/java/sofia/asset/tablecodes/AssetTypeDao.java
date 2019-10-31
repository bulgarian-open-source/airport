package sofia.asset.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetType}.
 *
 * @author Developers
 *
 */
@EntityType(AssetType.class)
public class AssetTypeDao extends CommonEntityDao<AssetType> implements IAssetType {

    @Inject
    public AssetTypeDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetType> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetType.FETCH_PROVIDER. Then remove the line after.
        return FETCH_PROVIDER;
        //throw new UnsupportedOperationException("Please specify the properties, which are required for the UI in IAssetType.FETCH_PROVIDER");
    }
}
