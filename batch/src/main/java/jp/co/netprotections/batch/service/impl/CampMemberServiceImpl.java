/**
 * 5:22:37 PM
 * @author h.dat
 */
package jp.co.netprotections.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.netprotections.batch.CampMemberCheckBatch;
import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.repository.CampMemberRepository;
import jp.co.netprotections.batch.service.CampMemberService;

/**
 * @author h.dat CampMemberServiceの実装
 *
 */
@Service
public class CampMemberServiceImpl implements CampMemberService {

	// consoleにログを書き込む
	private static final Logger logger = LoggerFactory.getLogger(CampMemberServiceImpl.class);

	@Autowired
	private CampMemberRepository repository;

	@Override
	public void add(CampMember campMember) {
		try {
			repository.save(campMember);
		} catch (ConstraintViolationException e) {
			logger.error("Error: " + e.getLocalizedMessage());
		}

	}

	@Override
	public void addAll(List<CampMember> campMembers) {
		try {
			repository.saveAll(campMembers);
		} catch (ConstraintViolationException e) {
			logger.error("Error: " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<CampMember> listAll() {
		try {
			return repository.findAll();
		} catch (ConstraintViolationException e) {
			logger.error("Error: " + e.getLocalizedMessage());
			return new ArrayList<CampMember>();
		}
	}

	@Override
	public void deleteAll() {
		try {
			repository.deleteAll();
		} catch (ConstraintViolationException e) {
			logger.error("Error: " + e.getLocalizedMessage());
		}
	}

	@Override
	public boolean isValidToBeMember(CampMember newMember) {
		boolean isValid = true;
		if (newMember.getMemberName() != null && !newMember.getMemberName().isEmpty()) {
			if (newMember.getEventPlanning() < 1) {
				isValid = false;
			} else {
				if (newMember.getCogitation() < 1) {
					isValid = false;
				}
				if (newMember.getCoodination() < 1) {
					isValid = false;
				}
				if ((newMember.getEventPlanning() + newMember.getCogitation() + newMember.getCoodination()
						+ newMember.getProgrammingAbility() + newMember.getInfrastructureKnowledge()) < 10) {
					isValid = false;
				}
			}
		} else {
			isValid = false;
		}
		return isValid;
	}

}
