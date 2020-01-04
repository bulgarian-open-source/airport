package sofia.asset.tablecodes.definers;

import java.util.Set;

import sofia.asset.tablecodes.AssetOwnership;
import sofia.asset.tablecodes.AssetTypeOwnership;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeOwnershipExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> ownershipPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        if (!(property.getEntity() instanceof AssetOwnership) && !(property.getEntity() instanceof AssetTypeOwnership)) {
            throw Result.failure("Stringly entities of type AssetOwnership or AssetTypeOwnership are expected");
        }
        
       final AbstractEntity<?> ownership = property.getEntity();
       final boolean allEmpty = ownership.get("role") == null && ownership.get("bu") == null && ownership.get("org") == null;
       
       ownershipPropNames.stream()
               .map(name -> ownership.getProperty(name))
               .filter(p -> p.getValue() == null)
               .forEach(p -> p.setRequired(allEmpty));
           
       if (value != null) {
           ownershipPropNames.stream()
                    .filter(name -> !name.equalsIgnoreCase(property.getName()))
                    .map(name -> ownership.getProperty(name))
                    .forEach(p -> {p.setRequired(false);p.setValue(null);});
           property.setRequired(true);
       }
}}