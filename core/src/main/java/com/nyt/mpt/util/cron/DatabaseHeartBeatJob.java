/**
 * 
 */
package com.nyt.mpt.util.cron;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.dao.impl.DatabaseHeartBeatSchedularDAO;
import com.nyt.mpt.dao.impl.ODSDatabaseHeartBeatSchedularDAO;
import com.nyt.mpt.dao.impl.SOSDatabaseHeartBeatSchedularDAO;

/**
 * Database heart beat scheduler to check database connection alive in Production
 * 
 * @author pranay.prabhat
 *
 */
public class DatabaseHeartBeatJob extends AbstractCronJob {

	private DatabaseHeartBeatSchedularDAO databaseHeartBeatSchedularDAO;
	
	private ODSDatabaseHeartBeatSchedularDAO oDSDatabaseHeartBeatSchedularDAO;
	
	private SOSDatabaseHeartBeatSchedularDAO sosDatabaseHeartBeatSchedularDAO;

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		databaseHeartBeatSchedularDAO.fetchAMPTRoles();
		oDSDatabaseHeartBeatSchedularDAO.fetchSalesCategoryFromODS();
		sosDatabaseHeartBeatSchedularDAO.fetchCurrencyFromSOS();
	}

	public void setDatabaseHeartBeatSchedularDAO(DatabaseHeartBeatSchedularDAO databaseHeartBeatSchedularDAO) {
		this.databaseHeartBeatSchedularDAO = databaseHeartBeatSchedularDAO;
	}
	
	public void setODSDatabaseHeartBeatSchedularDAO(ODSDatabaseHeartBeatSchedularDAO oDSDatabaseHeartBeatSchedularDAO) {
		this.oDSDatabaseHeartBeatSchedularDAO = oDSDatabaseHeartBeatSchedularDAO;
	}

	public void setSosDatabaseHeartBeatSchedularDAO(SOSDatabaseHeartBeatSchedularDAO sosDatabaseHeartBeatSchedularDAO) {
		this.sosDatabaseHeartBeatSchedularDAO = sosDatabaseHeartBeatSchedularDAO;
	}
}
