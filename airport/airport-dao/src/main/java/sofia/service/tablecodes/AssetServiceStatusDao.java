package sofia.service.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetServiceStatus}.
 *
 * @author Developers
 *
 */
@EntityType(AssetServiceStatus.class)
public class AssetServiceStatusDao extends CommonEntityDao<AssetServiceStatus> implements IAssetServiceStatus {

    @Inject
    public AssetServiceStatusDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<AssetServiceStatus> createFetchProvider() {
        return FETCH_PROVIDER;
        }
}
