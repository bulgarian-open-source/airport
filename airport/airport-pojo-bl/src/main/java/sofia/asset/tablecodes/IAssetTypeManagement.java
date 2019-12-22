package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetTypeManagement}.
 *
 * @author Developers
 *
 */
public interface IAssetTypeManagement extends IEntityDao<AssetTypeManagement> {

    static final IFetchProvider<AssetTypeManagement> FETCH_PROVIDER = EntityUtils.fetch(AssetTypeManagement.class)
            .with("assetType", "role", "bu", "org", "startDate");

}
