package sofia.asset.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetManagement}.
 *
 * @author Developers
 *
 */
@EntityType(AssetManagement.class)
public class AssetManagementDao extends CommonEntityDao<AssetManagement> implements IAssetManagement {

    @Inject
    public AssetManagementDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
    public AssetManagement new_() {
       final AssetManagement management = super.new_();
       management.getProperty("role").setRequired(true);
       management.getProperty("bu").setRequired(true);
       management.getProperty("org").setRequired(true);
       return management;
    } 

    @Override
    protected IFetchProvider<AssetManagement> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}
