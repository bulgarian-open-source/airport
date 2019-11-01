package sofia.validators;

import static ua.com.fielden.platform.error.Result.failure;
import static ua.com.fielden.platform.error.Result.successful;

import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class NoEmotionsValidator implements IBeforeChangeEventHandler<String> {
    
    public static final String ERR_NO_EMOTIONS_ALLOWED = "No question or exclamation marks allowed";


    @Override
    public Result handle(final MetaProperty<String> property, final String newValue, Set<Annotation> mutatorAnnotations) {
        if (newValue.contains("?") | newValue.contains("!")) {
            return failure(ERR_NO_EMOTIONS_ALLOWED);
        }
        
        return successful("All is good.");
    }

}
