package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetOwnership}.
 *
 * @author Developers
 *
 */
public interface IAssetOwnership extends IEntityDao<AssetOwnership> {

    static final IFetchProvider<AssetOwnership> FETCH_PROVIDER = EntityUtils.fetch(AssetOwnership.class)
            .with("asset", "role", "bu", "org", "startDate");

}
