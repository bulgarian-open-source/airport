package sofia.asset.tablecodes;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.expr;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

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
import ua.com.fielden.platform.entity.annotation.Title;
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
@KeyTitle("Asset Type")
@CompanionObject(IAssetType.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
// TODO: May need this later if some entities need to be automatically cascade-deactivated when this entity is deactivated
// @DeactivatableDependencies({ Dependency1.class, Dependency2.class, Dependency3.class })
public class AssetType extends ActivatableAbstractEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetType.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
  
    @IsProperty
    @MapTo
    @Title(value = "name", desc = "Asset Type name")
    @CompositeKeyMember(1)
    private String name;

    @IsProperty
    @MapTo
    @Title(value = "Asset Class", desc = "An asset class for this type.")
    private AssetClass assetClass;
    
    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Curr Management", desc = "Current management for this type.")
    @Subtitles({@PathTitle(path="role", title="Management Role"),
                @PathTitle(path="bu", title="Management Business Unit"),
                @PathTitle(path="org", title="Management Organization"),
                @PathTitle(path="startDate", title="Management Start Date")})
    private AssetTypeManagement currManagement;
    
    private static final EntityResultQueryModel<AssetTypeManagement> managementSubQuery = select(AssetTypeManagement.class).where()
                                                                                .prop("assetType").eq().extProp("assetType").and()
                                                                                .prop("startDate").le().now().and()
                                                                                .prop("startDate").gt().extProp("startDate").model();
    
    protected static final ExpressionModel currManagement_ = expr().model(select(AssetTypeManagement.class)
                                                                .where().prop("assetType").eq().extProp("id").and()
                                                                .prop("startDate").le().now().and()
                                                                .notExists(managementSubQuery).model()).model();

    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Curr Ownership", desc = "Desc")
    @Subtitles({@PathTitle(path="role", title="Ownership Role"),
                @PathTitle(path="bu", title="Ownership Business Unit"),
                @PathTitle(path="org", title="Ownership Organization"),
                @PathTitle(path="startDate", title="Ownership Start Date")})
    private AssetTypeOwnership currOwnership;
    
    private static final EntityResultQueryModel<AssetTypeOwnership> ownershipSubQuery = select(AssetTypeOwnership.class).where()
                                                                                .prop("assetType").eq().extProp("assetType").and()
                                                                                .prop("startDate").le().now().and()
                                                                                .prop("startDate").gt().extProp("startDate").model();
            
    protected static final ExpressionModel currOwnership_ = expr().model(select(AssetTypeOwnership.class)
                                                            .where().prop("assetType").eq().extProp("id").and()
                                                            .prop("startDate").le().now().and()
                                                            .notExists(ownershipSubQuery).model()).model();


    
    @Observable
    protected AssetType setCurrManagement(final AssetTypeManagement currManagement) {
        this.currManagement = currManagement;
        return this;
    }

    public AssetTypeManagement getCurrManagement() {
        return currManagement;
    }
    
    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Curr Operatorship", desc = "Desc")
    @Subtitles({@PathTitle(path="role", title="Operatorship Role"),
                @PathTitle(path="bu", title="Operatorship Business Unit"),
                @PathTitle(path="org", title="Operatorship Organization"),
                @PathTitle(path="startDate", title="Operatorship Start Date")})
    private AssetTypeOperatorship currOperatorship;
    
    private static final EntityResultQueryModel<AssetTypeOperatorship> operatorshipSubQuery= select(AssetTypeOperatorship.class).where()
                                                                                .prop("assetType").eq().extProp("assetType").and()
                                                                                .prop("startDate").le().now().and()
                                                                                .prop("startDate").gt().extProp("startDate").model();
            
    protected static final ExpressionModel currOperatorship_ = expr().model(select(AssetTypeOperatorship.class)
                                                            .where().prop("assetType").eq().extProp("id").and()
                                                            .prop("startDate").le().now().and()
                                                            .notExists(operatorshipSubQuery).model()).model();
    


    @Observable
    public AssetType setAssetClass(final AssetClass assetClass) {
        this.assetClass = assetClass;
        return this;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    
    @Observable
    public AssetType setName(final String name) {
        this.name = name;
        return this;
    }
    

    public String getName() {
        return name;
    }
    
    @Override
    @Observable
    public AssetType setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }
    
    @Override
    @Observable
    public AssetType setActive(boolean active) {
        super.setActive(active);
        return this;
    }
    
    @Observable
    protected AssetType setCurrOwnership(final AssetTypeOwnership currOwnership) {
        this.currOwnership = currOwnership;
        return this;
    }

    public AssetTypeOwnership getCurrOwnership() {
        return currOwnership;
    }
    
    @Observable
    protected AssetType setCurrOperatorship(final AssetTypeOperatorship currOperatorship) {
        this.currOperatorship = currOperatorship;
        return this;
    }

    public AssetTypeOperatorship getCurrOperatorship() {
        return currOperatorship;
    }
    

}
