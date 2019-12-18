package sofia.assets.ui_actions.producers;

import com.google.inject.Inject;

import sofia.assets.Asset;
import sofia.assets.ui_actions.OpenAssetMasterAction;
import sofia.common.producers.AbstractProducerForOpenEntityMasterAction;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;

/**
 * A producer for new instances of entity {@link OpenAssetMasterAction}.
 *
 * @author Developers
 *
 */
public class OpenAssetMasterActionProducer extends AbstractProducerForOpenEntityMasterAction<Asset, OpenAssetMasterAction> {

    @Inject
    public OpenAssetMasterActionProducer(final EntityFactory factory, final ICompanionObjectFinder companionFinder) {
        super(factory, Asset.class, OpenAssetMasterAction.class, companionFinder);
    }
}
