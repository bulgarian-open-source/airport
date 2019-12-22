package sofia.organizational;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IOrganization}.
 *
 * @author Developers
 *
 */
@EntityType(Organization.class)
public class OrganizationDao extends CommonEntityDao<Organization> implements IOrganization {

    @Inject
    public OrganizationDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<Organization> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IOrganization.FETCH_PROVIDER. Then remove the line after.
        // return FETCH_PROVIDER;
        throw new UnsupportedOperationException("Please specify the properties, which are required for the UI in IOrganization.FETCH_PROVIDER");
    }
}
