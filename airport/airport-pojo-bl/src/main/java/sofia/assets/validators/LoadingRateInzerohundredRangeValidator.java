package sofia.assets.validators;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;
import static ua.com.fielden.platform.error.Result.failure;
import static ua.com.fielden.platform.error.Result.successful;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Set;

import sofia.assets.Asset;
import sofia.projects.Project;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractBeforeChangeEventHandler;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.error.Result;

public class LoadingRateInzerohundredRangeValidator extends AbstractBeforeChangeEventHandler<BigDecimal> {

	@Override
	public Result handle(final MetaProperty<BigDecimal> property, final BigDecimal newValue, final Set<Annotation> mutatorAnnotations) {
		final Asset asset = property.getEntity();
		
//		if (asset.getLoadingRate().compareTo(BigDecimal.valueOf(100)) == 1 || asset.getLoadingRate().compareTo(BigDecimal.valueOf(0)) == -1) {
//            return Result.failure("Loading rate outside 0% - 100% range.");
//        }
		if (newValue == null) {
            return successful(newValue);
        }
		final EntityResultQueryModel<Asset> query = select(Asset.class)
                .where().prop("loadingRate").le().val(BigDecimal.valueOf(0)).or()
                .prop("loadingRate").ge().val(BigDecimal.valueOf(100)).model();
		
		return co(Asset.class).exists(query) ? successful(newValue) : failure("Loading rate outside 0% - 100% range.");
    }
}
