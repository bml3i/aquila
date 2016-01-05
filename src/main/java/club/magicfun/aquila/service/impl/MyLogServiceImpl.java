package club.magicfun.aquila.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.model.MyLog;
import club.magicfun.aquila.repository.MyLogRepository;
import club.magicfun.aquila.service.MyLogService;

@Service
@Transactional
public class MyLogServiceImpl implements MyLogService {

	@Autowired
	private MyLogRepository myLogRepository;

	@Override
	public MyLog persist(MyLog myLog) {
		
		if (myLog.getCreateDatetime() == null) {
			myLog.setCreateDatetime(new Date());
		}
		
		return myLogRepository.save(myLog);
	}

}
