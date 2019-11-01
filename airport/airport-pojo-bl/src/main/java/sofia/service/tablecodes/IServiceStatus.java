package sofia.service.tablecodes;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link ServiceStatus}.
 *
 * @author Developers
 *
 */
public interface IServiceStatus extends IEntityDao<ServiceStatus> {

    static final IFetchProvider<ServiceStatus> FETCH_PROVIDER = EntityUtils.fetch(ServiceStatus.class)
                                                                           .with("name", "desc");
}
