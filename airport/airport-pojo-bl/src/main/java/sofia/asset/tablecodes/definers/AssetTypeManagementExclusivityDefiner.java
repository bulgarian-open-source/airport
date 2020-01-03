package sofia.asset.tablecodes.definers;

import java.util.Set;

import sofia.asset.tablecodes.AssetManagement;
import sofia.asset.tablecodes.AssetTypeManagement;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeManagementExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> managementPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        
        if (!(property.getEntity() instanceof AssetManagement) && !(property.getEntity() instanceof AssetTypeManagement)) {
            throw Result.failure("Stringly entities of type AssetOwnership or AssetTypeOwnership are expected");
        }
       final AbstractEntity<?> management = property.getEntity();
       final boolean allEmpty = management.get("role") == null && management.get("bu") == null && management.get("org") == null;
        
       managementPropNames.stream()
               .map(name -> management.getProperty(name))
               .filter(p -> p.getValue() == null)
               .forEach(p -> p.setRequired(allEmpty));
           
       if (value != null) {
           managementPropNames.stream()
                    .filter(name -> !name.equalsIgnoreCase(property.getName()))
                    .map(name -> management.getProperty(name))
                    .forEach(p -> {p.setRequired(false);p.setValue(null);});
           property.setRequired(true);
       }
   }
}