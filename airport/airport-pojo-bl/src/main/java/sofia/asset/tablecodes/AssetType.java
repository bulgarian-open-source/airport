package sofia.asset.tablecodes;

//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.cond;
//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAggregated;
//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAll;
//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllAndInstrument;
//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllIncCalc;
//import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetchAllIncCalcAndInstrument;

import ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils;

import ua.com.fielden.platform.utils.EntityUtils;


import ua.com.fielden.platform.dao.QueryExecutionModel;
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
import ua.com.fielden.platform.entity.query.EntityAggregates;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.AggregatedResultQueryModel;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.ExpressionModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
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
    
   
   

    @Observable
    public AssetType setAssetClass(final AssetClass assetClass) {
        this.assetClass = assetClass;
        return this;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }
    
    
    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Curr Ownership", desc = "desc")
    private AssetTypeOwnership currOwnership;
   
    private static final EntityResultQueryModel<AssetTypeOwnership> subQuery = EntityQueryUtils.select(AssetTypeOwnership.class).where()
    																			.prop("assetType)").eq().extProp("assetType").and()
																	    		.prop("startDate").gt().extProp("startDate").model();
    protected static final ExpressionModel currOwnership_ = EntityQueryUtils.expr().model(EntityQueryUtils.select(AssetTypeOwnership.class)
    		.where().prop("assetType").eq().extProp("id").and().notExists(subQuery).model()).model();
    
   

    @Observable
    public AssetType setCurrOwnership(final AssetTypeOwnership currOwnership) {
        this.currOwnership = currOwnership;
        return this;
    }

	public AssetTypeOwnership getCurrOwnership() {
        return currOwnership;
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

    

}
