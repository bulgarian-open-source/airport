package sofia.asset.tablecodes.definers;

import java.util.Set;

import sofia.asset.tablecodes.AssetTypeOperatorship;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeOperatorshipExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> ownershipPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        
       final AssetTypeOperatorship operatorship = property.getEntity();
       final boolean allEmpty = operatorship.getRole() == null && operatorship.getBu() == null && operatorship.getOrg() == null;
       
       ownershipPropNames.stream()
               .map(name -> operatorship.getProperty(name))
               .filter(p -> p.getValue() == null)
               .forEach(p -> p.setRequired(allEmpty));
           
       if (value != null) {
           ownershipPropNames.stream()
                    .filter(name -> !name.equalsIgnoreCase(property.getName()))
                    .map(name -> operatorship.getProperty(name))
                    .forEach(p -> {p.setRequired(false);p.setValue(null);});
           property.setRequired(true);
       }
   }
}