package club.magicfun.aquila.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
	private Set<GroupRole> groupRoles;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
	@OrderBy("id ASC")
	@JoinTable(name = "group_role", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	public Set<GroupRole> getGroupRoles() {
		return groupRoles;
	}

	public void setGroupRoles(Set<GroupRole> groupRoles) {
		this.groupRoles = groupRoles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Integer> getRoleIds() {
		List<Integer> roleIds = new ArrayList<Integer>();

		for (Role role : this.getRoles()) {
			roleIds.add(role.getId());
		}

		return roleIds;
	}

}
