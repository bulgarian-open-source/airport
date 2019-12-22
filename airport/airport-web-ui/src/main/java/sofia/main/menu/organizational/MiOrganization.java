package sofia.main.menu.organizational;

import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.ui.menu.MiWithConfigurationSupport;
import sofia.organizational.Organization;
/**
 * Main menu item representing an entity centre for {@link Organization}.
 *
 * @author Developers
 *
 */
@EntityType(Organization.class)
public class MiOrganization extends MiWithConfigurationSupport<Organization> {

}
