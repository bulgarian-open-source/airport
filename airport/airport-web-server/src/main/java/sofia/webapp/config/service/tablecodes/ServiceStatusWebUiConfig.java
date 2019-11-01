package sofia.webapp.config.service.tablecodes;

import static java.lang.String.format;
import static sofia.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;

import java.util.Optional;

import com.google.inject.Injector;

import sofia.service.tablecodes.ServiceStatus;
import sofia.common.LayoutComposer;
import sofia.common.StandardActions;

import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import sofia.main.menu.service.tablecodes.MiServiceStatus;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
/**
 * {@link ServiceStatus} Web UI configuration.
 *
 * @author Developers
 *
 */
public class ServiceStatusWebUiConfig {

    public final EntityCentre<ServiceStatus> centre;
    public final EntityMaster<ServiceStatus> master;

    public static ServiceStatusWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new ServiceStatusWebUiConfig(injector, builder);
    }

    private ServiceStatusWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link ServiceStatus}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<ServiceStatus> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(1, 2);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(ServiceStatus.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(ServiceStatus.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(ServiceStatus.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(ServiceStatus.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(ServiceStatus.class, standardEditAction);

        final EntityCentreConfig<ServiceStatus> ecc = EntityCentreBuilder.centreFor(ServiceStatus.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(ServiceStatus.class).also()
                .addCrit("desc").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().minWidth(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", ServiceStatus.ENTITY_TITLE))
                    .withAction(standardEditAction).also()
                .addProp("desc").minWidth(100)
                //.addProp("prop").minWidth(100).withActionSupplier(builder.getOpenMasterAction(Entity.class)).also()
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiServiceStatus.class, MiServiceStatus.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link ServiceStatus}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<ServiceStatus> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkGridForMasterFitWidth(2, 1);

        final IMaster<ServiceStatus> masterConfig = new SimpleMasterBuilder<ServiceStatus>().forEntity(ServiceStatus.class)
                .addProp("name").asSinglelineText().also()
                .addProp("desc").asMultilineText().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(ServiceStatus.class, masterConfig, injector);
    }
}