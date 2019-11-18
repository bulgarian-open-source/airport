package sofia.service.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link ConditionRating}.
 *
 * @author Developers
 *
 */
public interface IConditionRating extends IEntityDao<ConditionRating> {

    static final IFetchProvider<ConditionRating> FETCH_PROVIDER = EntityUtils.fetch(ConditionRating.class)
            .with("name", "desc");

}
