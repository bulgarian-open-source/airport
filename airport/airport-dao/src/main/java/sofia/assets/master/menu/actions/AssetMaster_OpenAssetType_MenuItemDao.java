package sofia.assets.master.menu.actions;

import com.google.inject.Inject;

import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetMaster_OpenAssetType_MenuItem}.
 *
 * @author Developers
 *
 */
@EntityType(AssetMaster_OpenAssetType_MenuItem.class)
public class AssetMaster_OpenAssetType_MenuItemDao extends CommonEntityDao<AssetMaster_OpenAssetType_MenuItem> implements IAssetMaster_OpenAssetType_MenuItem {

    @Inject
    public AssetMaster_OpenAssetType_MenuItemDao(final IFilter filter) {
        super(filter);
    }

    @Override
    @SessionRequired
    public AssetMaster_OpenAssetType_MenuItem save(AssetMaster_OpenAssetType_MenuItem entity) {
        return super.save(entity);
    }

}
