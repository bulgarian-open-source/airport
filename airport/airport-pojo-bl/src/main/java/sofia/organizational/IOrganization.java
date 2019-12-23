package sofia.organizational;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link Organization}.
 *
 * @author Developers
 *
 */
public interface IOrganization extends IEntityDao<Organization> {

    static final IFetchProvider<Organization> FETCH_PROVIDER = EntityUtils.fetch(Organization.class).with(
        // TODO: uncomment the following line and specify the properties, which are required for the UI. Then remove the line after.
        // "key", "desc");
        "Please specify the properties, which are required for the UI");

}
