package sofia.assets;

import java.math.BigDecimal;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.assets.validators.FinDetAcquireDateWithinProjectPeriod;
import sofia.assets.validators.LoadingRateInzerohundredRangeValidator;
import ua.com.fielden.platform.entity.ActivatableAbstractEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DescRequired;
import ua.com.fielden.platform.entity.annotation.DescTitle;
import ua.com.fielden.platform.entity.annotation.DisplayDescription;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Readonly;
import ua.com.fielden.platform.entity.annotation.Required;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.BeforeChange;
import ua.com.fielden.platform.entity.annotation.mutator.Handler;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("Asset Number")
@CompanionObject(IAsset.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
// TODO: May need this later if some entities need to be automatically cascade-deactivated when this entity is deactivated
// @DeactivatableDependencies({ Dependency1.class, Dependency2.class, Dependency3.class })
public class Asset extends ActivatableAbstractEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(Asset.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
	
    @IsProperty
    @MapTo
    @Title(value = "number", desc = "A unique asset number, auto-generated.")
    @CompositeKeyMember(1)
    @Readonly
    private String number;
    
    @IsProperty
    @MapTo
    @Required
    @Title(value = "assetType", desc = "An asset type for this asset.")
    private AssetType assetType;
    
    @IsProperty
    @Title(value = "Fin Det", desc = "Financial details for this asset")
    private AssetFinDet finDet;

    @IsProperty
    @MapTo
    @Title(value = "loadingRate", desc = "Loading/usage rate for the Asset.")
    private BigDecimal loadingRate;
    
    
    
    @Observable
    public Asset setFinDet(final AssetFinDet finDet) {
        this.finDet = finDet;
        return this;
    }

    public AssetFinDet getFinDet() {
        return finDet;
    }

    
    @Observable
    public Asset setLoadingRate(final BigDecimal loadingRate) throws Exception {
        this.loadingRate = loadingRate;
        return this;
    }
    
    @Observable
    public BigDecimal getLoadingRate() {
        return loadingRate;
    }

    @Observable
    public Asset setAssetType(final AssetType assetType) {
        this.assetType = assetType;
        return this;
    }
    
    @Observable
    public AssetType getAssetType() {
        return assetType;
    }

   

    @Observable
    public Asset setNumber(final String number) {
        this.number = number;
        return this;
    }
    

    public String getNumber() {
        return number;
    }
    
    @Override
    @Observable
    public Asset setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }
    
    @Override
    @Observable
    public Asset setActive(boolean active) {
        super.setActive(active);
        return this;
    }

    

}
