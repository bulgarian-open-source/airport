package sofia.asset.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetTypeOwnership}.
 *
 * @author Developers
 *
 */
@EntityType(AssetTypeOwnership.class)
public class AssetTypeOwnershipDao extends CommonEntityDao<AssetTypeOwnership> implements IAssetTypeOwnership {

    @Inject
    public AssetTypeOwnershipDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
    public AssetTypeOwnership new_() {
        final AssetTypeOwnership ownership = super.new_();
        ownership.getProperty("role").setRequired(true);
        ownership.getProperty("bu").setRequired(true);
        ownership.getProperty("org").setRequired(true);
        return ownership;
        }

    @Override
    protected IFetchProvider<AssetTypeOwnership> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetTypeOwnership.FETCH_PROVIDER. Then remove the line after.
         return FETCH_PROVIDER;
    }
}
