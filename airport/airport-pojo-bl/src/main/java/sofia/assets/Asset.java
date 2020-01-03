package sofia.assets;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.expr;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetOwnership;
import sofia.asset.tablecodes.AssetType;
import sofia.asset.tablecodes.AssetTypeOwnership;
import sofia.service.tablecodes.AssetServiceStatus;
import sofia.assets.validators.FinDetAcquireDateWithinProjectPeriod;
import sofia.validators.RateRangeValidator;
import ua.com.fielden.platform.entity.ActivatableAbstractEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.Calculated;
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
import ua.com.fielden.platform.entity.annotation.titles.PathTitle;
import ua.com.fielden.platform.entity.annotation.titles.Subtitles;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.ExpressionModel;
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
    @Title(value = "Type", desc = "An asset type for this asset.")
    @Subtitles({@PathTitle(path="currOwnership.role", title="Type Ownership Role"),
                @PathTitle(path="currOwnership.bu", title="Type Ownership Business Unit"),
                @PathTitle(path="currOwnership.org", title="Type Ownership Organization"),
                @PathTitle(path="currOwnership.startDate", title="Type Ownership Start Date")})
    private AssetType assetType;
    
    @IsProperty
    @MapTo
    @Title(value = "regulatory", desc = "A flag for Assets that are legally regulated.")
    private boolean regulatory;

    @IsProperty
    @MapTo
    @Title(value = "keyService", desc = "A flag for Assets that are 'key service' assets.")
    private boolean keyService;

    @IsProperty
    @Title(value = "Fin Det", desc = "Financial details for this asset")
    private AssetFinDet finDet;

    @IsProperty
    @MapTo
    @BeforeChange(@Handler(RateRangeValidator.class))
    @Title(value = "loadingRate", desc = "Loading/usage rate for the Asset.")
    private String loadingRate;
    
    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Curr Ownership", desc = "Desc")
    @Subtitles({@PathTitle(path="role", title="Ownership Role"),
                @PathTitle(path="bu", title="Ownership Business Unit"),
                @PathTitle(path="org", title="Ownership Organization"),
                @PathTitle(path="startDate", title="Ownership Start Date")})
    private AssetOwnership currOwnership;
    
    private static final EntityResultQueryModel<AssetOwnership> subQuery = select(AssetOwnership.class).where()
                                                                                .prop("asset").eq().extProp("asset").and()
                                                                                .prop("startDate").le().now().and()
                                                                                .prop("startDate").gt().extProp("startDate").model();
            
    protected static final ExpressionModel currOwnership_ = expr().model(select(AssetOwnership.class)
                                                            .where().prop("asset").eq().extProp("id").and()
                                                            .prop("startDate").le().now().and()
                                                            .notExists(subQuery).model()).model();

    @Observable
    protected Asset setCurrOwnership(final AssetOwnership currOwnership) {
        this.currOwnership = currOwnership;
        return this;
    }

    public AssetOwnership getCurrOwnership() {
        return currOwnership;
    }


    @Observable
    public Asset setLoadingRate(final String loadingRate) {
        if (!loadingRate.substring(loadingRate.length() - 1, loadingRate.length()).equals("%")) {
            this.loadingRate = loadingRate.concat("%");}
        else {
            this.loadingRate = loadingRate;
        }

        return this;
    }

    @Observable
    public String getLoadingRate() {
        return loadingRate.substring(0, loadingRate.length() - 1);
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
    public Asset setRegulatory(final boolean regulatory) {
        this.regulatory= regulatory;
        return this;
    }

    @Observable
    public boolean getRegulatory() {
        return regulatory;
    }

    @Observable
    public Asset setKeyService(final boolean keyService) {
        this.keyService = keyService;
        return this;
    }

    @Observable
    public boolean getKeyService() {
        return keyService;
    }

    @Observable
    public Asset setFinDet(final AssetFinDet finDet) {
        this.finDet = finDet;
        return this;
    }

    public AssetFinDet getFinDet() {
        return finDet;
    }
}
