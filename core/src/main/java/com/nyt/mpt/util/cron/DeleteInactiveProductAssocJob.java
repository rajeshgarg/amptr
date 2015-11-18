package com.nyt.mpt.util.cron;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * Delete all creative and attribute association which are associated with inctive product
 * @author rakesh.tewari
 */
public class DeleteInactiveProductAssocJob extends AbstractCronJob {

	private IProductService productService;
	private static final Logger LOGGER = Logger.getLogger(DeleteInactiveProductAssocJob.class);

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!isJobActiveOnServer(CronJobNameEnum.DELETE_INACTIVE_PRODUCT_ASSOC_JOB)) {
			return;
		}
		super.setUserForCronJobs();
		final List<Long> productIDList = productService.getAllInactiveProducts();
		LOGGER.info("List of Deleted or inactive product in sos - " + StringUtils.join(productIDList, ConstantStrings.COMMA));
		if (productIDList != null && !productIDList.isEmpty()) {
			LOGGER.info("Delete product creative assoc for given product ids");
			productService.deleteProductCreativeAssoc(productIDList);
			LOGGER.info("Delete product attribute assoc for given product ids");
			productService.deleteProductAttributeAssoc(productIDList);
		}
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

}
