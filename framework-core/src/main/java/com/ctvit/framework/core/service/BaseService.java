package com.ctvit.framework.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ctvit.framework.core.dao.GenericDao;
import com.ctvit.framework.core.dao.SlaveDao;

@Service
public class BaseService {

	@Autowired
	@Qualifier("baseDao")
	protected GenericDao baseDao;

	@Autowired
	@Qualifier("slaveDao")
	protected SlaveDao slaveDao;

}
