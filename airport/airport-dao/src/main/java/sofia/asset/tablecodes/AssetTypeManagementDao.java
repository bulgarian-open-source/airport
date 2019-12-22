package sofia.asset.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetTypeManagement}.
 *
 * @author Developers
 *
 */
@EntityType(AssetTypeManagement.class)
public class AssetTypeManagementDao extends CommonEntityDao<AssetTypeManagement> implements IAssetTypeManagement {

    @Inject
    public AssetTypeManagementDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
    public AssetTypeManagement new_() {
        final AssetTypeManagement management = super.new_();
        management.getProperty("role").setRequired(true);
        management.getProperty("bu").setRequired(true);
        management.getProperty("org").setRequired(true);
        return management;
        }

    @Override
    protected IFetchProvider<AssetTypeManagement> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetTypeManagement.FETCH_PROVIDER. Then remove the line after.
       return FETCH_PROVIDER;
    }
}
