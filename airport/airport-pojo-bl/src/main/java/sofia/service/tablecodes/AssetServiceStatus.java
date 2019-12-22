package sofia.service.tablecodes;

import java.util.Date;

import sofia.assets.Asset;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DateOnly;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Required;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("AssetServiceStatus")
@CompanionObject(IAssetServiceStatus.class)
@MapEntityTo
public class AssetServiceStatus extends AbstractPersistentEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetServiceStatus.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
    
    @IsProperty
    @MapTo
    @Title(value = "Asset", desc = "Associated asset")
    @CompositeKeyMember(1)
    private Asset asset;
    
    @IsProperty
    @MapTo
    @Title(value = "Start Date", desc = "The start date of the service")
    @CompositeKeyMember(2)
    @DateOnly
    private Date startDate;
    
    @IsProperty
    @MapTo
    @Required
    @Title(value = "Start Date", desc = "The start date of the ownership")
    private ServiceStatus serviceStatus;
    
    
    
    
    @Observable
    public AssetServiceStatus setAsset(final Asset asset) {
        this.asset = asset;
        return this;
    }

    public Asset getAsset() {
        return asset;
    }
    
    @Observable
    public AssetServiceStatus setStartDate(final Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }
    
    @Observable
    public AssetServiceStatus setServiceStatus(final ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
        return this;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

}
