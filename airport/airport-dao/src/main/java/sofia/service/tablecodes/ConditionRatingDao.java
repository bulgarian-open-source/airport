package sofia.service.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IConditionRating}.
 *
 * @author Developers
 *
 */
@EntityType(ConditionRating.class)
public class ConditionRatingDao extends CommonEntityDao<ConditionRating> implements IConditionRating {

    @Inject
    public ConditionRatingDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<ConditionRating> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IConditionRating.FETCH_PROVIDER. Then remove the line after.
        return FETCH_PROVIDER;
    }
}
