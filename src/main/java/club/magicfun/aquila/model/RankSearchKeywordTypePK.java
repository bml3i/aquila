package club.magicfun.aquila.model;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RankSearchKeywordTypePK implements Serializable {

	private static final long serialVersionUID = -1719280929629996264L;

	@ManyToOne
	@JoinColumn(name = "rank_search_keyword_id", referencedColumnName = "id")
	private RankSearchKeyword rankSearchKeyword;

	@ManyToOne
	@JoinColumn(name = "rank_search_type_id", referencedColumnName = "id")
	private RankSearchType rankSearchType;
}
