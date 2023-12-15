package hu.gde.ortem.session.model;

import hu.gde.ortem.common.model.BaseModel;
import hu.gde.ortem.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
* @author  Noel Solyom
*/

@Entity(name = "Session")
@Table(name = "session")
public class Session extends BaseModel {

	@Column(nullable = false, length = 255)
	private String sid;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Session [sid=" + sid + ", user=" + user + "]";
	}

}
