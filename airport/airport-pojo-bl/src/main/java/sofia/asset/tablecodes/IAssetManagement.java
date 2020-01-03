package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetManagement}.
 *
 * @author Developers
 *
 */
public interface IAssetManagement extends IEntityDao<AssetManagement> {

    static final IFetchProvider<AssetManagement> FETCH_PROVIDER = EntityUtils.fetch(AssetManagement.class)
                                                                    .with("asset", "role", "bu", "org", "startDate");

}
