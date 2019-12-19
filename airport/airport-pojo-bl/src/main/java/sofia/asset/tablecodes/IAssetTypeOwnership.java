package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetTypeOwnership}.
 *
 * @author Developers
 *
 */
public interface IAssetTypeOwnership extends IEntityDao<AssetTypeOwnership> {

    static final IFetchProvider<AssetTypeOwnership> FETCH_PROVIDER = EntityUtils.fetch(AssetTypeOwnership.class)
            .with("assetType", "role", "bu", "org", "startDate");
}
