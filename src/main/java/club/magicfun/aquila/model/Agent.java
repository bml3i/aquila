package club.magicfun.aquila.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "findActiveAgents", query = "select a from Agent a where a.activeFlag = 1"),
	@NamedQuery(name = "findInactiveAgents", query = "select a from Agent a where a.activeFlag = 0") 
})
@Table(name = "agents")
public class Agent {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "port_num")
	private String portNumber;

	@Column(name = "description")
	private String description;
	
	@Column(name = "active_flg")
	private Boolean activeFlag;
	
	@Column(name = "retry_cnt")
	private Integer retryCount;
	
	@Column(name = "delay")
	private Double delay; 
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	
	@Column(name = "update_datetime")
	private Date updateDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	
	// get full ip address & port
	public String getIPAndPort() {
		return this.ipAddress + ":" + this.portNumber; 
	}
	
}
