package club.magicfun.aquila.model;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class GroupRolePK implements Serializable {

	private static final long serialVersionUID = -8244815858932695301L;

	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	private Group group;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;

}
