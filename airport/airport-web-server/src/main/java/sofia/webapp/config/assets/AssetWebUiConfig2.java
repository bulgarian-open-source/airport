package sofia.webapp.config.assets;

import static ua.com.fielden.platform.web.PrefDim.mkDim;
import static sofia.common.StandardScrollingConfigs.standardEmbeddedScrollingConfig;
import static sofia.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;
import static sofia.common.StandardActions.actionAddDesc;
import static sofia.common.StandardActions.actionEditDesc;
import static java.lang.String.format;
import static ua.com.fielden.platform.dao.AbstractOpenCompoundMasterDao.enhanceEmbededCentreQuery;
import static ua.com.fielden.platform.entity_centre.review.DynamicQueryBuilder.createConditionProperty;

import java.util.Optional;

import com.google.inject.Injector;

import sofia.assets.Asset;
import sofia.asset.tablecodes.AssetType;
import sofia.main.menu.asset.tablecodes.MiAssetMaster_AssetType;
import sofia.assets.master.menu.actions.AssetMaster_OpenAssetType_MenuItem;
import sofia.assets.ui_actions.OpenAssetMasterAction;
import sofia.assets.ui_actions.producers.OpenAssetMasterActionProducer;
import sofia.assets.master.menu.actions.AssetMaster_OpenMain_MenuItem;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.compound.Compound;
import ua.com.fielden.platform.web.view.master.api.compound.impl.CompoundMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.PrefDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
import sofia.common.LayoutComposer;
import sofia.common.StandardActions;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import static ua.com.fielden.platform.web.centre.api.context.impl.EntityCentreContextSelector.context;

import ua.com.fielden.platform.web.centre.CentreContext;
import ua.com.fielden.platform.web.centre.IQueryEnhancer;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.ICompleted;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.IWhere0;
import sofia.main.menu.assets.MiAsset;
import ua.com.fielden.platform.web.view.master.EntityMaster;
/**
 * {@link Asset} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetWebUiConfig2 {

    private final Injector injector;

    public final EntityCentre<Asset> centre;
    public final EntityMaster<Asset> master;
    public final EntityMaster<OpenAssetMasterAction> compoundMaster;
    public final EntityActionConfig editAssetAction;
    private final EntityActionConfig newAssetAction;

    public static AssetWebUiConfig2 register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetWebUiConfig2(injector, builder);
    }

    private AssetWebUiConfig2(final Injector injector, final IWebUiBuilder builder) {
        this.injector = injector;

        final PrefDim dims = mkDim(960, 640, Unit.PX);
        editAssetAction = Compound.openEdit(OpenAssetMasterAction.class, Asset.ENTITY_TITLE, actionEditDesc(Asset.ENTITY_TITLE), dims);
        newAssetAction = Compound.openNew(OpenAssetMasterAction.class, "add-circle-outline", Asset.ENTITY_TITLE, actionAddDesc(Asset.ENTITY_TITLE), dims);
        builder.registerOpenMasterAction(Asset.class, editAssetAction);

        centre = createCentre(builder);
        builder.register(centre);

        master = createMaster();
        builder.register(master);

        compoundMaster = CompoundMasterBuilder.<Asset, OpenAssetMasterAction>create(injector, builder)
            .forEntity(OpenAssetMasterAction.class)
            .withProducer(OpenAssetMasterActionProducer.class)
            .addMenuItem(AssetMaster_OpenMain_MenuItem.class)
                .icon("icons:picture-in-picture")
                .shortDesc(OpenAssetMasterAction.MAIN)
                .longDesc(Asset.ENTITY_TITLE + " main")
                .withView(master)
            .also()
            .addMenuItem(AssetMaster_OpenAssetType_MenuItem.class)
                .icon("icons:view-module")
                .shortDesc(OpenAssetMasterAction.ASSETTYPES)
                .longDesc(Asset.ENTITY_TITLE + " " + OpenAssetMasterAction.ASSETTYPES)
                .withView(createAssetTypeCentre())
            .done();
        builder.register(compoundMaster);
    }

    /**
     * Creates entity centre for {@link Asset}.
     *
     * @return
     */
    private EntityCentre<Asset> createAssetCentre(final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(1, 2);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(Asset.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(Asset.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();

        final EntityCentreConfig<Asset> ecc = EntityCentreBuilder.centreFor(Asset.class)
                //.runAutomatically()
                .addFrontAction(newAssetAction)
                .addTopAction(newAssetAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(Asset.class).also()
                .addCrit("desc").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().minWidth(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", Asset.ENTITY_TITLE))
                    .withAction(editAssetAction).also()
                .addProp("desc").minWidth(300)
                .addPrimaryAction(editAssetAction)
                .build();

        return new EntityCentre<>(MiAsset.class, MiAsset.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link Asset}.
     *
     * @return
     */
    private EntityMaster<Asset> createAssetMaster() {
        final String layout = LayoutComposer.mkGridForMasterFitWidth(1, 2);

        final IMaster<Asset> masterConfig = new SimpleMasterBuilder<Asset>().forEntity(Asset.class)
                .addProp("key").asSinglelineText().also()
                .addProp("desc").asMultilineText().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .done();

        return new EntityMaster<Asset>(Asset.class, masterConfig, injector);
    }

    private EntityCentre<AssetType> createAssetTypeCentre() {
        final Class<AssetType> root = AssetType.class;
        final String layout = LayoutComposer.mkVarGridForCentre(2, 1);

        final EntityActionConfig standardNewAction = StandardActions.NEW_WITH_MASTER_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_EMBEDDED_CENTRE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();

        final EntityCentreConfig<AssetType> ecc = EntityCentreBuilder.centreFor(root)
                .runAutomatically()
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("crit1").asMulti()/*.autocompleter(Crit1.class)*/.text().also()
                .addCrit("crit2").asMulti()/*.autocompleter(Crit2.class)*/.text().also()
                .addCrit("crit3").asRange().integer()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardEmbeddedScrollingConfig(0))
                .addProp("prop1").order(1).asc().minWidth(80)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetType.ENTITY_TITLE)).also()
                .addProp("prop2").minWidth(80).also()
                .addProp("prop3").minWidth(80)
                .addPrimaryAction(standardEditAction)
                .setQueryEnhancer(AssetMaster_AssetTypeCentre_QueryEnhancer.class, context().withMasterEntity().build())
                .build();

        return new EntityCentre<>(MiAssetMaster_AssetType.class, MiAssetMaster_AssetType.class.getSimpleName(), ecc, injector, null);
    }

    private static class AssetMaster_AssetTypeCentre_QueryEnhancer implements IQueryEnhancer<AssetType> {
        @Override
        public ICompleted<AssetType> enhanceQuery(final IWhere0<AssetType> where, final Optional<CentreContext<AssetType, ?>> context) {
            return enhanceEmbededCentreQuery(where, createConditionProperty("asset"), context.get().getMasterEntity().getKey());
        }
    }

}