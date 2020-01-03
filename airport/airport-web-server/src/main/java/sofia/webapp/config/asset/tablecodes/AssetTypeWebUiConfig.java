package sofia.webapp.config.asset.tablecodes;

import static java.lang.String.format;
import static sofia.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;

import java.util.Optional;

import com.google.inject.Injector;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.AssetType;
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
import sofia.main.menu.asset.tablecodes.MiAssetType;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
/**
 * {@link AssetType} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetTypeWebUiConfig {

    public final EntityCentre<AssetType> centre;
    public final EntityMaster<AssetType> master;

    public static AssetTypeWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetTypeWebUiConfig(injector, builder);
    }

    private AssetTypeWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetType}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetType> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkVarGridForCentre(2, 2, 3, 1, 3, 1);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetType.class, standardEditAction);

        final EntityCentreConfig<AssetType> ecc = EntityCentreBuilder.centreFor(AssetType.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(AssetType.class).also()
                .addCrit("desc").asMulti().text().also()
                .addCrit("assetClass").asMulti().autocompleter(AssetClass.class).also()
                .addCrit("active").asMulti().bool().also()
                .addCrit("currOwnership.role").asMulti().autocompleter(Role.class).also()
                .addCrit("currOwnership.bu").asMulti().autocompleter(BusinessUnit.class).also()
                .addCrit("currOwnership.org").asMulti().autocompleter(Organization.class).also()
                .addCrit("currOwnership.startDate").asRange().date().also()
                .addCrit("currOperatorship.role").asMulti().autocompleter(Role.class).also()
                .addCrit("currOperatorship.bu").asMulti().autocompleter(BusinessUnit.class).also()
                .addCrit("currOperatorship.org").asMulti().autocompleter(Organization.class).also()
                .addCrit("currOperatorship.startDate").asRange().date()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().minWidth(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetType.ENTITY_TITLE))
                    .withAction(standardEditAction).also()
                .addProp("desc").minWidth(100).also()
                .addProp("assetClass").minWidth(100).withActionSupplier(builder.getOpenMasterAction(AssetClass.class)).also()
                .addProp("active").minWidth(80).also()
                .addProp("currOwnership.role").minWidth(40).also()
                .addProp("currOwnership.bu").minWidth(40).also()
                .addProp("currOwnership.org").minWidth(40).also()
                .addProp("currOwnership.startDate").minWidth(75).also()
                .addProp("currOperatorship.role").minWidth(40).also()
                .addProp("currOperatorship.bu").minWidth(40).also()
                .addProp("currOperatorship.org").minWidth(40).also()
                .addProp("currOperatorship.startDate").minWidth(75)
                //.addProp("prop").minWidth(100).withActionSupplier(builder.getOpenMasterAction(Entity.class)).also()
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetType.class, MiAssetType.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link AssetType}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetType> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkVarGridForMasterFitWidth(1, 1, 2, 4, 4);

        final IMaster<AssetType> masterConfig = new SimpleMasterBuilder<AssetType>().forEntity(AssetType.class)
                .addProp("name").asSinglelineText().also()
                .addProp("desc").asMultilineText().also()
                .addProp("assetClass").asAutocompleter().also()
                .addProp("active").asCheckbox().also()
                .addProp("currOwnership.role").asAutocompleter().also()
                .addProp("currOwnership.bu").asAutocompleter().also()
                .addProp("currOwnership.org").asAutocompleter().also()
                .addProp("currOwnership.startDate").asDatePicker().also()
                .addProp("currOperatorship.role").asAutocompleter().also()
                .addProp("currOperatorship.bu").asAutocompleter().also()
                .addProp("currOperatorship.org").asAutocompleter().also()
                .addProp("currOperatorship.startDate").asDatePicker().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_THREE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(AssetType.class, masterConfig, injector);
    }
}