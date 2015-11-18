package com.nyt.mpt.service.impl;

import com.nyt.mpt.dao.IDatabaseHeartBeatSchedularDAO;
import com.nyt.mpt.service.IDatabaseHeartBeatService;

public class DatabaseHeartBeatService implements IDatabaseHeartBeatService{
	
	private IDatabaseHeartBeatSchedularDAO databaseHeartBeatSchedularDAO;

	@Override
	public boolean executeHealthStatus() {
		return databaseHeartBeatSchedularDAO.healthCheck();
	}
	
	public void setDatabaseHeartBeatSchedularDAO(IDatabaseHeartBeatSchedularDAO databaseHeartBeatSchedularDAO) {
		this.databaseHeartBeatSchedularDAO = databaseHeartBeatSchedularDAO;
	}
}
