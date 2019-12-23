package sofia.validators;

import static ua.com.fielden.platform.error.Result.failure;
import static ua.com.fielden.platform.error.Result.successful;


import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class RateRangeValidator implements IBeforeChangeEventHandler<String> {
    
    public static final String NOT_A_NUMBER = "Not a number";
    public static final String INCORRECT_RANGE = "Load ratio must be from 0% to 100%";

    @Override
    public Result handle(final MetaProperty<String> property, String newValue, Set<Annotation> mutatorAnnotations) {
        if (!newValue.substring(newValue.length() - 1, newValue.length()).equals("%")) {
            newValue = newValue.concat("%");
        }
        try {
            Integer load = Integer.parseInt(newValue.substring(0, newValue.length() - 1));
            if (load < 0 || load > 100) {
                return failure(INCORRECT_RANGE);
            }
        } catch (NumberFormatException nfe)
        {
            return failure(NOT_A_NUMBER);
        }
      
        
        return successful("All is good.");
    }

}
