package sofia.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import sofia.personnel.Person;
import sofia.projects.Project;
import ua.com.fielden.platform.basic.config.IApplicationDomainProvider;
import ua.com.fielden.platform.domain.PlatformDomainTypes;
import ua.com.fielden.platform.entity.AbstractEntity;
import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.service.tablecodes.ServiceStatus;
import sofia.assets.Asset;
import sofia.asset.tablecodes.ui_actions.OpenAssetClassMasterAction;
import sofia.asset.tablecodes.master.menu.actions.AssetClassMaster_OpenMain_MenuItem;
import sofia.asset.tablecodes.master.menu.actions.AssetClassMaster_OpenAssetType_MenuItem;
import sofia.service.tablecodes.AssetServiceStatus;
import sofia.service.tablecodes.ConditionRating;
import sofia.assets.AssetFinDet;
import sofia.organizational.Role;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.asset.tablecodes.AssetTypeOwnership;
import sofia.asset.tablecodes.AssetTypeManagement;
import sofia.asset.tablecodes.AssetTypeOperatorship;
import sofia.asset.tablecodes.AssetOwnership;
import sofia.asset.tablecodes.AssetTypeOperatorship;
import sofia.asset.tablecodes.AssetManagement;

/**
 * A class to register domain entities.
 * 
 * @author TG Team
 * 
 */
public class ApplicationDomain implements IApplicationDomainProvider {
    private static final Set<Class<? extends AbstractEntity<?>>> entityTypes = new LinkedHashSet<>();
    private static final Set<Class<? extends AbstractEntity<?>>> domainTypes = new LinkedHashSet<>();

    static {
        entityTypes.addAll(PlatformDomainTypes.types);
        add(Person.class);
        add(AssetClass.class);
        add(AssetType.class);
        add(ServiceStatus.class);
        add(Asset.class);
        add(OpenAssetClassMasterAction.class);
        add(AssetClassMaster_OpenMain_MenuItem.class);
        add(AssetClassMaster_OpenAssetType_MenuItem.class);
        add(Project.class);
        add(AssetServiceStatus.class);
        add(ConditionRating.class);
        add(AssetFinDet.class);
        add(Role.class);
        add(BusinessUnit.class);
        add(Organization.class);
        add(AssetTypeOwnership.class);
        add(AssetTypeManagement.class);
        add(AssetTypeOperatorship.class);
        add(AssetOperatorship.class);
        add(AssetOwnership.class);
        add(AssetManagement.class);
    }

    private static void add(final Class<? extends AbstractEntity<?>> domainType) {
        entityTypes.add(domainType);
        domainTypes.add(domainType);
    }

    @Override
    public List<Class<? extends AbstractEntity<?>>> entityTypes() {
        return Collections.unmodifiableList(entityTypes.stream().collect(Collectors.toList()));
    }

    public List<Class<? extends AbstractEntity<?>>> domainTypes() {
        return Collections.unmodifiableList(domainTypes.stream().collect(Collectors.toList()));
    }
}
