package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetTypeOperatorship}.
 *
 * @author Developers
 *
 */
public interface IAssetTypeOperatorship extends IEntityDao<AssetTypeOperatorship> {

    static final IFetchProvider<AssetTypeOperatorship> FETCH_PROVIDER = EntityUtils.fetch(AssetTypeOperatorship.class)
            .with("assetType", "role", "bu", "org", "startDate");
}
