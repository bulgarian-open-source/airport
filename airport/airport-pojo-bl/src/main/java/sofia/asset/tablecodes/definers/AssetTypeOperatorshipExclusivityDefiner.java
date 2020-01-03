package sofia.asset.tablecodes.definers;

import java.util.Set;

import sofia.asset.tablecodes.AssetOperatorship;
import sofia.asset.tablecodes.AssetTypeOperatorship;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.utils.CollectionUtil;

public class AssetTypeOperatorshipExclusivityDefiner implements IAfterChangeEventHandler<Object> {
    
    private final static Set<String> operatorshipPropNames = CollectionUtil.setOf("role", "bu", "org");

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
        if (!(property.getEntity() instanceof AssetOperatorship) && !(property.getEntity() instanceof AssetTypeOperatorship)) {
            throw Result.failure("Stringly entities of type AssetOperatorship or AssetTypeOperatorship are expected");
        }
        
       final AbstractEntity<?> operatorship = property.getEntity();
       final boolean allEmpty = operatorship.get("role") == null && operatorship.get("bu") == null && operatorship.get("org") == null;
       
       operatorshipPropNames.stream()
               .map(name -> operatorship.getProperty(name))
               .filter(p -> p.getValue() == null)
               .forEach(p -> p.setRequired(allEmpty));
           
       if (value != null) {
           operatorshipPropNames.stream()
                    .filter(name -> !name.equalsIgnoreCase(property.getName()))
                    .map(name -> operatorship.getProperty(name))
                    .forEach(p -> {p.setRequired(false);p.setValue(null);});
           property.setRequired(true);
       }
}}