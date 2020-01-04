package sofia.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link Asset}.
 *
 * @author Developers
 *
 */
public interface IAsset extends IEntityDao<Asset> {

    static final IFetchProvider<Asset> FETCH_PROVIDER = EntityUtils.fetch(Asset.class)
            .with("number", "desc", "assetType", "finDet", "regulatory", "keyService")
            .with("assetType.currOwnership.role", "assetType.currOwnership.bu", "assetType.currOwnership.org", "assetType.currOwnership.startDate")
            .with("currOwnership.role", "currOwnership.bu", "currOwnership.org", "currOwnership.startDate")
            .with("assetType.currManagement.role", "assetType.currManagement.bu", "assetType.currManagement.org", "assetType.currManagement.startDate")
            .with("currManagement.role", "currManagement.bu", "currManagement.org", "currManagement.startDate")
            .with("assetType.currOperatorship.role", "assetType.currOperatorship.bu", "assetType.currOperatorship.org", "assetType.currOperatorship.startDate")
            .with("currOperatorship.role", "currOperatorship.bu", "currOperatorship.org", "currOperatorship.startDate");

}
