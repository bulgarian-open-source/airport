package sofia.asset.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetClass}.
 *
 * @author Developers
 *
 */
public interface IAssetClass extends IEntityDao<AssetClass> {

    static final IFetchProvider<AssetClass> FETCH_PROVIDER = EntityUtils.fetch(AssetClass.class).with(
        "name", "desc");
//        "Please specify the properties, which are required for the UI");

}
