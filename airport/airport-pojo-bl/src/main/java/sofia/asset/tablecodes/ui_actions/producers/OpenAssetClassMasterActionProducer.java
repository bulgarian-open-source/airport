package sofia.asset.tablecodes.ui_actions.producers;

import com.google.inject.Inject;

import sofia.asset.tablecodes.AssetClass;
import sofia.asset.tablecodes.ui_actions.OpenAssetClassMasterAction;
import ua.com.fielden.platform.security.Authorise;
import sofia.common.producers.AbstractProducerForOpenEntityMasterAction;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;

/**
 * A producer for new instances of entity {@link OpenAssetClassMasterAction}.
 *
 * @author Developers
 *
 */
public class OpenAssetClassMasterActionProducer extends AbstractProducerForOpenEntityMasterAction<AssetClass, OpenAssetClassMasterAction> {

    @Inject
    public OpenAssetClassMasterActionProducer(final EntityFactory factory, final ICompanionObjectFinder companionFinder) {
        super(factory, AssetClass.class, OpenAssetClassMasterAction.class, companionFinder);
    }

    @Override
    protected OpenAssetClassMasterAction provideDefaultValues(OpenAssetClassMasterAction openAction) {
        return super.provideDefaultValues(openAction);
    }
}
