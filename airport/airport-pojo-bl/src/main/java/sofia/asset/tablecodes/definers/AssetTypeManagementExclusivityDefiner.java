package sofia.asset.tablecodes.definers;

import java.util.Set;
import sofia.asset.tablecodes.AssetTypeManagement;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeManagementExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> managementPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        
       final AssetTypeManagement management = property.getEntity();
       final boolean allEmpty = management.getRole() == null && management.getBu() == null && management.getOrg() == null;
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