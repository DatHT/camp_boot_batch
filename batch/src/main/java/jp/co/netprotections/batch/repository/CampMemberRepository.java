/**
 * 3:50:19 PM
 * @author h.dat
 */
package jp.co.netprotections.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.netprotections.batch.entity.CampMember;

/**
 * @author h.dat
 * CAMPメンバーのリポジトリ（DAO）: BDにCRUD
 *
 */
@Repository
public interface CampMemberRepository extends JpaRepository<CampMember, Integer>{

}
