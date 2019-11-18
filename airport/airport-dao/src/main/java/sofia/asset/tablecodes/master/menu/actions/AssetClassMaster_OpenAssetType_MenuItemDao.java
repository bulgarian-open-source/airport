package sofia.asset.tablecodes.master.menu.actions;

import com.google.inject.Inject;

import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IAssetClassMaster_OpenAssetType_MenuItem}.
 *
 * @author Developers
 *
 */
@EntityType(AssetClassMaster_OpenAssetType_MenuItem.class)
public class AssetClassMaster_OpenAssetType_MenuItemDao extends CommonEntityDao<AssetClassMaster_OpenAssetType_MenuItem> implements IAssetClassMaster_OpenAssetType_MenuItem {

    @Inject
    public AssetClassMaster_OpenAssetType_MenuItemDao(final IFilter filter) {
        super(filter);
    }

    @Override
    @SessionRequired
    public AssetClassMaster_OpenAssetType_MenuItem save(AssetClassMaster_OpenAssetType_MenuItem entity) {
        return super.save(entity);
    }

}
