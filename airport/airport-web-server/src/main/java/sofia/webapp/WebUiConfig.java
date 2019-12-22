package sofia.webapp;

import org.apache.commons.lang.StringUtils;

import sofia.config.personnel.PersonWebUiConfig;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import sofia.projects.Project;
import sofia.service.tablecodes.AssetServiceStatus;
import sofia.service.tablecodes.ConditionRating;
import sofia.service.tablecodes.ServiceStatus;
import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
import sofia.asset.tablecodes.AssetTypeOperatorship;
import sofia.asset.tablecodes.AssetTypeManagement;
import sofia.asset.tablecodes.AssetTypeOwnership;
import sofia.assets.Asset;
import sofia.assets.AssetFinDet;
import sofia.webapp.config.asset.tablecodes.AssetClassWebUiConfig;
import sofia.webapp.config.asset.tablecodes.AssetTypeOperatorshipWebUiConfig;
import sofia.webapp.config.asset.tablecodes.AssetTypeManagementWebUiConfig;
import sofia.webapp.config.asset.tablecodes.AssetTypeOwnershipWebUiConfig;
import sofia.webapp.config.asset.tablecodes.AssetTypeWebUiConfig;
import sofia.webapp.config.assets.AssetFinDetWebUiConfig;
import sofia.webapp.config.assets.AssetWebUiConfig;
import sofia.webapp.config.organizational.BusinessUnitWebUiConfig;
import sofia.webapp.config.organizational.OrganizationWebUiConfig;
import sofia.webapp.config.organizational.RoleWebUiConfig;
import sofia.webapp.config.projects.ProjectWebUiConfig;
import sofia.webapp.config.service.tablecodes.AssetServiceStatusWebUiConfig;
import sofia.webapp.config.service.tablecodes.ConditionRatingWebUiConfig;
import sofia.webapp.config.service.tablecodes.ServiceStatusWebUiConfig;
import ua.com.fielden.platform.basic.config.Workflows;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.resources.webui.AbstractWebUiConfig;
import ua.com.fielden.platform.web.resources.webui.UserRoleWebUiConfig;
import ua.com.fielden.platform.web.resources.webui.UserWebUiConfig;

/**
 * App-specific {@link IWebApp} implementation.
 *
 * @author Generated
 *
 */
public class WebUiConfig extends AbstractWebUiConfig {

    private final String domainName;
    private final String path;
    private final int port;

    public WebUiConfig(final String domainName, final int port, final Workflows workflow, final String path) {
        super("Sofia Airport Asset Management (development)", workflow, new String[] { "sofia/" });
        if (StringUtils.isEmpty(domainName) || StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Both the domain name and application binding path should be specified.");
        }
        this.domainName = domainName;
        this.port = port;
        this.path = path;
    }


    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getPort() {
        return port;
    }

    /**
     * Configures the {@link WebUiConfig} with custom centres and masters.
     */
    @Override
    public void initConfiguration() {
        super.initConfiguration();

        final IWebUiBuilder builder = configApp();
        builder.setDateFormat("DD/MM/YYYY").setTimeFormat("HH:mm").setTimeWithMillisFormat("HH:mm:ss")
        // Minimum tablet width defines the boundary below which mobile layout takes place.
        // For example for Xiaomi Redmi 4x has official screen size of 1280x640,
        // still its viewport sizes is twice lesser: 640 in landscape orientation and 360 in portrait orientation.
        // When calculating reasonable transition tablet->mobile we need to consider "real" viewport sizes instead of physical pixel sizes (http://viewportsizes.com).
        .setMinTabletWidth(600);

        // Personnel
        final PersonWebUiConfig personWebUiConfig = PersonWebUiConfig.register(injector(), builder);
        final UserWebUiConfig userWebUiConfig = new UserWebUiConfig(injector());
        final UserRoleWebUiConfig userRoleWebUiConfig = new UserRoleWebUiConfig(injector());
        
        // Asset table codes
        final AssetClassWebUiConfig assetClassWebUiConfig = AssetClassWebUiConfig.register(injector(), builder);
        final AssetTypeWebUiConfig assetTypeWebUiConfig = AssetTypeWebUiConfig.register(injector(), builder);
        final AssetTypeOwnershipWebUiConfig assetTypeOwnershipWebUiConfig = AssetTypeOwnershipWebUiConfig.register(injector(), builder);
        final AssetTypeOperatorshipWebUiConfig assetTypeOperatorshipWebUiConfig = AssetTypeOperatorshipWebUiConfig.register(injector(), builder);
        final AssetTypeManagementWebUiConfig assetTypeManagementWebUiConfig = AssetTypeManagementWebUiConfig.register(injector(), builder);
      
        // Service Status table codes
        final ServiceStatusWebUiConfig serviceStatusWebUiConfig = ServiceStatusWebUiConfig.register(injector(), builder);
        final ConditionRatingWebUiConfig conditionRatingWebUiConfig = ConditionRatingWebUiConfig.register(injector(), builder);
      
        // Asset instance
        final AssetWebUiConfig assetWebUiConfig = AssetWebUiConfig.register(injector(), builder);
        final AssetFinDetWebUiConfig assetFinDetWebUiConfig = AssetFinDetWebUiConfig.register(injector(), builder);
        
        // Organizational
        final RoleWebUiConfig roleWebUiConfig = RoleWebUiConfig.register(injector(), builder);
        final BusinessUnitWebUiConfig buWebUiConfig = BusinessUnitWebUiConfig.register(injector(), builder);
        final OrganizationWebUiConfig orgWebUiConfig = OrganizationWebUiConfig.register(injector(), builder);
        final AssetServiceStatusWebUiConfig assetServiceStatusWebUiConfig = AssetServiceStatusWebUiConfig.register(injector(), builder);
        final AssetWebUiConfig assetWebUiConfig = AssetWebUiConfig.register(injector(), builder);
        final AssetFinDetWebUiConfig assetFinDetWebUiConfig = AssetFinDetWebUiConfig.register(injector(), builder);
        
        
        // Project related UI
        final ProjectWebUiConfig projectWebUiConfig = ProjectWebUiConfig.register(injector(), builder);


        
        
        
        
        // Configure application web resources such as masters and centres
        configApp()
        .addMaster(userWebUiConfig.master)
        .addMaster(userWebUiConfig.rolesUpdater)
        .addMaster(userRoleWebUiConfig.master)
        .addMaster(userRoleWebUiConfig.tokensUpdater)
        // user/personnel module
        .addCentre(userWebUiConfig.centre)
        .addCentre(userRoleWebUiConfig.centre);

        // Configure application menu
        configDesktopMainMenu().
        addModule("Asset Acquisition").
        description("Asset acquisition module").
        icon("mainMenu:equipment").
        detailIcon("mainMenu:equipment").
        bgColor("#FFE680").
        captionBgColor("#FFD42A").menu()
            .addMenuItem(Asset.ENTITY_TITLE).description(String.format("%s Centre", Asset.ENTITY_TITLE)).centre(assetWebUiConfig.centre).done()
            .addMenuItem(AssetFinDet.ENTITY_TITLE).description(String.format("%s Centre", AssetFinDet.ENTITY_TITLE)).centre(assetFinDetWebUiConfig.centre).done()
            .addMenuItem(Project.ENTITY_TITLE).description(String.format("%s Centre", Project.ENTITY_TITLE)).centre(projectWebUiConfig.centre).done()
            .done().done().
        addModule("Users / Personnel").
            description("Provides functionality for managing application security and personnel data.").
            icon("mainMenu:help").
            detailIcon("anotherMainMenu:about").
            bgColor("#FFE680").
            captionBgColor("#FFD42A").menu()
            .addMenuItem("Personnel").description("Personnel related data")
                .addMenuItem("Personnel").description("Personnel Centre").centre(personWebUiConfig.centre).done()
            .done()
            .addMenuItem("Users").description("Users related data")
                .addMenuItem("Users").description("User centre").centre(userWebUiConfig.centre).done()
                .addMenuItem("User Roles").description("User roles centre").centre(userRoleWebUiConfig.centre).done()
            .done().
        done().done().
        addModule("Table Codes").
            description("Table Codes Description").
            icon("mainMenu:tablecodes").
            detailIcon("mainMenu:tablecodes").
            bgColor("#FFE680").
            captionBgColor("#FFD42A").menu()
            .addMenuItem("Asset Table Codes").description("Various master data for assets.")
                .addMenuItem(AssetClass.ENTITY_TITLE).description(String.format("%s Centre", AssetClass.ENTITY_TITLE)).centre(assetClassWebUiConfig.centre).done()
                .addMenuItem(AssetType.ENTITY_TITLE).description(String.format("%s Centre", AssetType.ENTITY_TITLE)).centre(assetTypeWebUiConfig.centre).done()
                .addMenuItem(AssetTypeOwnership.ENTITY_TITLE).description(String.format("%s Centre", AssetTypeOwnership.ENTITY_TITLE)).centre(assetTypeOwnershipWebUiConfig.centre).done()
                .addMenuItem(AssetTypeOperatorship.ENTITY_TITLE).description(String.format("%s Centre", AssetTypeOperatorship.ENTITY_TITLE)).centre(assetTypeOperatorshipWebUiConfig.centre).done()
                .addMenuItem(AssetTypeManagement.ENTITY_TITLE).description(String.format("%s Centre", AssetTypeManagement.ENTITY_TITLE)).centre(assetTypeManagementWebUiConfig.centre).done()
                .addMenuItem(AssetServiceStatus.ENTITY_TITLE).description(String.format("%s Centre", AssetServiceStatus.ENTITY_TITLE)).centre(assetServiceStatusWebUiConfig.centre).done()
            .done()
            .addMenuItem("Asset Service Codes").description("Various master data for assets service.")
            .addMenuItem(ServiceStatus.ENTITY_TITLE).description(String.format("%s Centre", ServiceStatus.ENTITY_TITLE)).centre(serviceStatusWebUiConfig.centre).done()
            .addMenuItem(ConditionRating.ENTITY_TITLE).description(String.format("%s Centre", ConditionRating.ENTITY_TITLE)).centre(conditionRatingWebUiConfig.centre).done()
        .done().
        done().done()
        .addModule("Organizational").
        description("Organizational entities").
        icon("mainMenu:tablecodes").
        detailIcon("mainMenu:tablecodes").
        bgColor("#FFE689").
        captionBgColor("#FFD42A").menu()
            .addMenuItem(Role.ENTITY_TITLE).description(String.format("%s Centre", Role.ENTITY_TITLE)).centre(roleWebUiConfig.centre).done()
            .addMenuItem(BusinessUnit.ENTITY_TITLE).description(String.format("%s Centre", BusinessUnit.ENTITY_TITLE)).centre(buWebUiConfig.centre).done()
            .addMenuItem(Organization.ENTITY_TITLE).description(String.format("%s Centre", Organization.ENTITY_TITLE)).centre(orgWebUiConfig.centre).done()    
    .done().done()
    .setLayoutFor(Device.DESKTOP, null, "[[[], []], [[], []]]")
    .setLayoutFor(Device.TABLET, null,  "[[[{\"rowspan\":2}], []], [[]]]")
    .setLayoutFor(Device.MOBILE, null, "[ [[]],[[]], [[]], [[]] ]")
    .minCellWidth(100).minCellHeight(148).done();
}

            

}