package sofia.service.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetServiceStatus}.
 *
 * @author Developers
 *
 */
public interface IAssetServiceStatus extends IEntityDao<AssetServiceStatus> {

    static final IFetchProvider<AssetServiceStatus> FETCH_PROVIDER = EntityUtils.fetch(AssetServiceStatus.class)
            .with("asset", "startDate", "serviceStatus");

}
