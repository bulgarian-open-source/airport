package sofia.asset.tablecodes.definers;

import sofia.asset.tablecodes.AssetTypeOwnership;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;

public class AssetTypeOwnershipExclusivityDefiner implements IAfterChangeEventHandler<Object> {

    @Override
    public void handle(final MetaProperty<Object> property, final Object value) {
       final AssetTypeOwnership ownership = property.getEntity();
       final boolean allEmpty = ownership.getRole() == null && ownership.getBu() == null && ownership.getOrg() == null;
       
       if (ownership.getRole() == null) {
           ownership.getProperty("role").setRequired(allEmpty);
           }
           if (ownership.getBu() == null) {
               ownership.getProperty("bu").setRequired(allEmpty);
           }
           if (ownership.getOrg() == null) {
           ownership.getProperty("org").setRequired(allEmpty);
           }
           
       if (value != null) {
           if ("role".equals(property.getName())) {
               ownership.getProperty("bu").setRequired(false);
               ownership.setBu(null);
               ownership.getProperty("org").setRequired(false);
               ownership.setOrg(null);
               ownership.getProperty("role").setRequired(true);
               
           } else if ("bu".equals(property.getName())) {
                   ownership.getProperty("role").setRequired(false);
                   ownership.setRole(null);
                   ownership.getProperty("org").setRequired(false);
                   ownership.setOrg(null);
                   ownership.getProperty("bu").setRequired(true);
           } else if ("org".equals(property.getName())){
                   ownership.getProperty("role").setRequired(false);
                   ownership.setRole(null);
                   ownership.getProperty("bu").setRequired(false);
                   ownership.setBu(null);
                   ownership.getProperty("org").setRequired(true);
           }
       }
   }
}