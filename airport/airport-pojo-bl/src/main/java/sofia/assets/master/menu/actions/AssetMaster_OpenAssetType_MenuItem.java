package sofia.assets.master.menu.actions;

import sofia.assets.Asset;
import ua.com.fielden.platform.entity.AbstractFunctionalEntityForCompoundMenuItem;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.EntityTitle;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object to model the detail menu item of the compound master entity object.
 *
 * @author Developers
 *
 */
@KeyType(Asset.class)
@CompanionObject(IAssetMaster_OpenAssetType_MenuItem.class)
@EntityTitle("Asset Master Asset Type Menu Item")
public class AssetMaster_OpenAssetType_MenuItem extends AbstractFunctionalEntityForCompoundMenuItem<Asset> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetMaster_OpenAssetType_MenuItem.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

}
