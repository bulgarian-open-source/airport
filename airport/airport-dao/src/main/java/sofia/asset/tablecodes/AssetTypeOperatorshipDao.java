package sofia.asset.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetTypeOperatorship}.
 *
 * @author Developers
 *
 */
@EntityType(AssetTypeOperatorship.class)
public class AssetTypeOperatorshipDao extends CommonEntityDao<AssetTypeOperatorship> implements IAssetTypeOperatorship {

    @Inject
    public AssetTypeOperatorshipDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
    public AssetTypeOperatorship new_() {
        final AssetTypeOperatorship operatorship = super.new_();
        operatorship.getProperty("role").setRequired(true);
        operatorship.getProperty("bu").setRequired(true);
        operatorship.getProperty("org").setRequired(true);
        return operatorship;
        }

    @Override
    protected IFetchProvider<AssetTypeOperatorship> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IAssetTypeOwnership.FETCH_PROVIDER. Then remove the line after.
         return FETCH_PROVIDER;
    }
}
