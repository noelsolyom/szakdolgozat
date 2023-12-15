package hu.gde.ortem.user.model;

import java.util.ArrayList;
import java.util.List;

import hu.gde.ortem.common.model.BaseModel;
import hu.gde.ortem.session.model.Session;
import hu.gde.ortem.userrole.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "User")
@Table(name = "user")
public class User extends BaseModel {

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@ManyToOne
	@JoinColumn(name = "user_role_id", nullable = false)
	private UserRole role;

	@OneToMany(mappedBy = "user")
	private List<Session> sessions = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", password=***, role=" + role + "]";
	}

}
