/**
 * 5:05:42 PM
 * @author h.dat
 */
package jp.co.netprotections.batch.service;

import java.util.List;

import jp.co.netprotections.batch.entity.CampMember;

/**
 * @author h.dat
 * CAMP memberサービス、ビジネスロジック処理、DB repositoryの呼び出す
 */
public interface CampMemberService {
	
	/**
	 * DBにメンバーを追加する
	 * @param campMember
	 * @return void
	 */
	public void add(CampMember campMember);

	/**
	 * DBにメンバー一覧を追加する
	 * @param campMembers
	 * @return void
	 */
	public void addAll(List<CampMember> campMembers);

	/**
	 * DBからメンバー一覧を取得する
	 * @return
	 * @return void
	 */
	public List<CampMember> listAll();

	/**
	 * DBのメンバーデータ全て削除する
	 * @return void
	 */
	public void deleteAll();

	/**
	 * 人の入隊基準を満たせるかどうかチェックする
	 * @param newMember
	 * @return boolean
	 */
	public boolean isValidToBeMember(CampMember newMember);
}
