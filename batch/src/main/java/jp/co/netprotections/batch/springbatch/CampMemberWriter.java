/**
 * 10:52:54 AM
 * @author h.dat
 */
package jp.co.netprotections.batch.springbatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.service.CampMemberService;
import jp.co.netprotections.batch.service.impl.CampMemberServiceImpl;

/**
 * @author h.dat
 *
 */
public class CampMemberWriter implements ItemWriter<CampMember> {
	// consoleにログを書き込む
	private static final Logger logger = LoggerFactory.getLogger(CampMemberWriter.class);
		
	@Autowired(required = true)
	CampMemberService service;

	@Override
	public void write(List<? extends CampMember> items) {
		//validatorを用意する
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator =factory.getValidator();
		
		try {
			List<CampMember> filterList = new ArrayList<CampMember>();
			for (CampMember campMember : items) {
				//バリデーション実施
				Set<ConstraintViolation<CampMember>> constraintsViolation = validator.validate(campMember);
				if (constraintsViolation.size() == 0) {
					//CampMemberが問題なくて続けてリストを書き込む
					if (service.isValidToBeMember(campMember)) {
						campMember.setEnlistedPropriety(true);
						filterList.add(campMember);
					} else {
						logger.info("Invalid Member: " + campMember.getMemberName());
					}
					
				} else {
					//CampMemberが条件問題があった
					constraintsViolation.stream()
					.map(constraintViolation -> String.format("user %s: %s value '%s' %s",
							campMember.getMemberName(), constraintViolation.getPropertyPath(),
							constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
					.collect(Collectors.toList()).forEach(message -> logger.error(message));
				}
				
			}
			if (filterList.size() > 0) {
				service.addAll(filterList);
			}
		} catch (ConstraintViolationException e) {
			logger.error("Error!: " +e.getLocalizedMessage());
		}
		
		
	}

}
