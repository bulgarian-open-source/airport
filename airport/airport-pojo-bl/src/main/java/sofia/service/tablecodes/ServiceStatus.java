package sofia.service.tablecodes;

import sofia.validators.LongerThan2Validator;
import sofia.validators.NoEmotionsValidator;
import sofia.validators.NoSpacesValidator;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
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
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.BeforeChange;
import ua.com.fielden.platform.entity.annotation.mutator.Handler;
import ua.com.fielden.platform.entity.annotation.mutator.IntParam;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("Service Status")
@CompanionObject(IServiceStatus.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
public class ServiceStatus extends AbstractPersistentEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(ServiceStatus.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
    
    @IsProperty
    @MapTo
    @Title(value = "Name", desc = "Desc")
    @CompositeKeyMember(1)
    @BeforeChange({
        @Handler(NoEmotionsValidator.class), 
        @Handler(value = LongerThan2Validator.class, integer = @IntParam(name = "minLength", value=3))})
    private String name;

    @Observable
    public ServiceStatus setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
    
    @Override
    @Observable
    public ServiceStatus setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }
    


}
