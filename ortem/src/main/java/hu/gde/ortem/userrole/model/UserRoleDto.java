package hu.gde.ortem.userrole.model;

/*
* @author  Noel Solyom
*/

public class UserRoleDto {

	private Long id;
	private String roleName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "UserRoleDto [id=" + id + ", roleName=" + roleName + "]";
	}

}
