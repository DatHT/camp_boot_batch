/**
 * 3:44:37 PM
 * @author h.dat
 */
package jp.co.netprotections.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author h.dat
 * CAMPメンバー　エンティティ
 *
 */
@Entity
@Table(name = "CampMembers")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampMember {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	/** 隊員氏名 */
	@Column
	@NotNull
	private String memberName;

	/** イベント企画力 */
	@Column
	@Min(0)
	@Max(5)
	@NotNull
	private int eventPlanning;

	/** 思考力 */
	@Column
	@Min(0)
	@Max(5)
	@NotNull
	private int cogitation;

	/** 調整力 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int coodination;

	/** プログラム製造力 */
	@Column
	@Min(0)
	@Max(5)
	@NotNull
	private int programmingAbility;

	/** 基盤理解 */
	@Column
	@Min(0)
	@Max(5)
	@NotNull
	private int infrastructureKnowledge;

	/** 隊員氏名 */
	@Column
	private boolean enlistedPropriety;
}
