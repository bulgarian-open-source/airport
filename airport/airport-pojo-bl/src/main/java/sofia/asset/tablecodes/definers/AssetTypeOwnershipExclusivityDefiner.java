package sofia.asset.tablecodes.definers;

import java.util.Set;

import sofia.asset.tablecodes.AssetTypeOwnership;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeOwnershipExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> ownershipPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        
       final AssetTypeOwnership ownership = property.getEntity();
       final boolean allEmpty = ownership.getRole() == null && ownership.getBu() == null && ownership.getOrg() == null;
       
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