package sofia.webapp.config.asset.tablecodes;

import static java.lang.String.format;
import static sofia.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;

import java.util.Optional;

import com.google.inject.Injector;

import sofia.asset.tablecodes.AssetOwnership;
import sofia.asset.tablecodes.AssetType;
import sofia.asset.tablecodes.AssetTypeOwnership;
import sofia.assets.Asset;
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
import sofia.main.menu.asset.tablecodes.MiAssetOwnership;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
/**
 * {@link AssetOwnership} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetOwnershipWebUiConfig {

    public final EntityCentre<AssetOwnership> centre;
    public final EntityMaster<AssetOwnership> master;

    public static AssetOwnershipWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetOwnershipWebUiConfig(injector, builder);
    }

    private AssetOwnershipWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetOwnership}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetOwnership> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(2, 3);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetOwnership.class, standardEditAction);

        final EntityCentreConfig<AssetOwnership> ecc = EntityCentreBuilder.centreFor(AssetOwnership.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("asset").asMulti().autocompleter(Asset.class).also()
                .addCrit("startDate").asRange().date().also()
                .addCrit("role").asMulti().autocompleter(Role.class).also()
                .addCrit("bu").asMulti().autocompleter(BusinessUnit.class).also()
                .addCrit("org").asMulti().autocompleter(Organization.class)
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("asset").order(1).asc().minWidth(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetOwnership.ENTITY_TITLE))
                    .withActionSupplier(builder.getOpenMasterAction(Asset.class)).also()
                .addProp("startDate").order(2).desc().width(150).also()
                .addProp("role").minWidth(100).also()
                .addProp("bu").minWidth(100).also()
                .addProp("org").minWidth(100)
                //.addProp("prop").minWidth(100).withActionSupplier(builder.getOpenMasterAction(Entity.class)).also()
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetOwnership.class, MiAssetOwnership.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link AssetOwnership}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetOwnership> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkGridForMasterFitWidth(5, 1);

        final IMaster<AssetOwnership> masterConfig = new SimpleMasterBuilder<AssetOwnership>().forEntity(AssetOwnership.class)
                .addProp("asset").asAutocompleter().also()
                .addProp("startDate").asDatePicker().also()
                .addProp("role").asAutocompleter().also()
                .addProp("bu").asAutocompleter().also()
                .addProp("org").asAutocompleter().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(AssetOwnership.class, masterConfig, injector);
    }
}