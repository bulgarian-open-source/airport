package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetOperatorship}.
 *
 * @author Developers
 *
 */
public interface IAssetOperatorship extends IEntityDao<AssetOperatorship> {

    static final IFetchProvider<AssetOperatorship> FETCH_PROVIDER = EntityUtils.fetch(AssetOperatorship.class)
            .with("asset", "role", "bu", "org", "startDate");

}
