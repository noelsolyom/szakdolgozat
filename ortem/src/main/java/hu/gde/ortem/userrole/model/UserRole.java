package hu.gde.ortem.userrole.model;

import hu.gde.ortem.common.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "UserRole")
@Table(name = "user_role")
public class UserRole extends BaseModel {

	@Enumerated(EnumType.STRING)
	@Column(nullable = true, length = 10)
	private Role roleName;

	public Role getRoleName() {
		return roleName;
	}

	public void setRoleName(Role roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "UserRole [roleName=" + roleName + "]";
	}

}
