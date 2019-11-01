package sofia.service.tablecodes;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IServiceStatus}.
 *
 * @author Developers
 *
 */
@EntityType(ServiceStatus.class)
public class ServiceStatusDao extends CommonEntityDao<ServiceStatus> implements IServiceStatus {

    @Inject
    public ServiceStatusDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<ServiceStatus> createFetchProvider() {
        // TODO: uncomment the following line and specify the properties, which are required for the UI in IServiceStatus.FETCH_PROVIDER. Then remove the line after.
        return FETCH_PROVIDER;
        
    }
}
