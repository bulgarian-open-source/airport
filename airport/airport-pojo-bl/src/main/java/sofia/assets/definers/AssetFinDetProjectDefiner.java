package sofia.assets.definers;

import sofia.assets.AssetFinDet;
import sofia.projects.Project;
import ua.com.fielden.platform.entity.meta.IAfterChangeEventHandler;
import ua.com.fielden.platform.entity.meta.MetaProperty;

public class AssetFinDetProjectDefiner implements IAfterChangeEventHandler<Project> {

	@Override
	public void handle(MetaProperty<Project> property, final Project entityPropertyValue) {
		final AssetFinDet finDet = property.getEntity();
		if(!finDet.isInitialising()) {
			if(finDet.getProject() != null && finDet.getAcquireDate() == null) {
				finDet.setAcquireDate(finDet.getProject().getStartDate());
			}
		}
		
	}

}
