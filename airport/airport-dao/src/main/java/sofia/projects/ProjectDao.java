package sofia.projects;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;
/**
 * DAO implementation for companion object {@link IProject}.
 *
 * @author Developers
 *
 */
@EntityType(Project.class)
public class ProjectDao extends CommonEntityDao<Project> implements IProject {

    @Inject
    public ProjectDao(final IFilter filter) {
        super(filter);
    }

    @Override
    protected IFetchProvider<Project> createFetchProvider() {
       return FETCH_PROVIDER;
    }
}
