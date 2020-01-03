package sofia.asset.tablecodes;

import java.util.Date;

import sofia.asset.tablecodes.definers.AssetTypeManagementExclusivityDefiner;
import sofia.organizational.BusinessUnit;
import sofia.organizational.Organization;
import sofia.organizational.Role;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DateOnly;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.AfterChange;

/**
 * A base type for Asset and AssetType management.
 *
 * @author Sofia-team
 *
 */

@KeyType(DynamicEntityKey.class)
@KeyTitle("Key")
@CompanionObject(IAssetTypeManagement.class)
@MapEntityTo
public class AbstractManagement extends AbstractPersistentEntity<DynamicEntityKey> {
    
    @IsProperty
    @MapTo
    @Title(value = "Start Date", desc = "The start date of the management")
    @CompositeKeyMember(2)
    @DateOnly
    private Date startDate;
    
    @IsProperty
    @MapTo
    @Title(value = "Role", desc = "Role that manages asset of the specified asset type.")
    @AfterChange(AssetTypeManagementExclusivityDefiner.class)
    private Role role;
    
    @IsProperty
    @MapTo
    @Title(value = "Business Unit", desc = "Business Unit that manages asset of the specified asset type.")
    @AfterChange(AssetTypeManagementExclusivityDefiner.class)
    private BusinessUnit bu;
    
    @IsProperty
    @MapTo
    @Title(value = "Organization", desc = "Organization that manages asset of the specified asset type.")
    @AfterChange(AssetTypeManagementExclusivityDefiner.class)
    private Organization org;
     

    @Observable
    public <E extends AbstractManagement> E setRole(final Role role) {
        this.role = role;
        return (E) this;
    }

    public Role getRole() {
        return role;
    }
    
    
    @Observable
    public <E extends AbstractManagement> E  setBu(final BusinessUnit bu) {
        this.bu = bu;
        return (E) this;
    }

    public BusinessUnit getBu() {
        return bu;
    }
    

    @Observable
    public <E extends AbstractManagement> E  setOrg(final Organization org) {
        this.org = org;
        return (E) this;
    }

    public Organization getOrg() {
        return org;
    }
    
    
    @Observable
    public <E extends AbstractManagement> E  setStartDate(final Date startDate) {
        this.startDate = startDate;
        return (E) this;
    }

    public Date getStartDate() {
        return startDate;
    }
   
}
