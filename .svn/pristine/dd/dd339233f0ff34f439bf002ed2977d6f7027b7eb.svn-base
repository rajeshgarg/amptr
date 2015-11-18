/**
 * 
 */
package com.nyt.mpt.form;

import java.util.Set;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.domain.PackageSalesCategoryAssoc;
import com.nyt.mpt.util.NumberUtil;

/**
 * This Package form bean is used for Package information
 * 
 * @author surendra.singh
 * 
 */
public class PackageForm extends BaseForm<Package> {

	private long packageId;

	private String packageName;

	private String packageOwner;

	private String ownerId;

	private String validFrom;

	private String validTo;

	private boolean active;

	private String breakable;

	private String breakableStr;

	private String budget;

	private boolean hasDocument;

	private int lineItemCount;

	private String createdAt;

	private String createdBy;

	private String modifiedBy;

	private String modifiedOn;

	private String expired;

	private String comments;

	private String packageSalescategory;

	private String packageSalescategoryName;

	@Override
	public Package populate(final Package bean) {
		bean.setId(this.getPackageId());
		bean.setName(this.packageName);
		final User user = new User();
		user.setUserId(Long.valueOf(this.ownerId));
		bean.setOwner(user);
		bean.setBreakable(Boolean.valueOf(this.breakable));
		bean.setBudget(NumberUtil.doubleValue(this.budget));
		if (StringUtils.isNotBlank(this.validFrom)) {
			bean.setValidFrom(DateUtil.parseToDate(this.validFrom));
		}
		if (StringUtils.isNotBlank(this.validTo)) {
			bean.setValidTo(DateUtil.parseToDate(this.validTo));
		}
		bean.setComments(this.comments);

		if (StringUtils.isNotBlank(this.getPackageSalescategory())) {
			final String[] salesCategoryList = this.getPackageSalescategory().split(ConstantStrings.COMMA);
			final String[] scNamesList = this.getPackageSalescategoryName().split(ConstantStrings.COMMA);
			for (int i = 0; i < salesCategoryList.length; i++) {
				final PackageSalesCategoryAssoc salesCategoryAssoc = new PackageSalesCategoryAssoc();
				salesCategoryAssoc.setSosSalesCategoryId(NumberUtil.longValue(salesCategoryList[i]));
				salesCategoryAssoc.setSosSalesCategoryName(scNamesList[i]);
				bean.addSalesCategoryAssoc(salesCategoryAssoc);
			}
		} else {
			bean.setPackageSalesCategoryAssoc(null);
		}
		return bean;
	}

	@Override
	public void populateForm(final Package bean) {
		int lineitemCount = 0;
		this.packageId = bean.getId();
		this.packageName = bean.getName();
		this.ownerId = Long.toString(bean.getOwner().getUserId());
		this.packageOwner = bean.getOwner().getFullName();
		this.breakable = String.valueOf(bean.isBreakable());
		this.breakableStr = bean.isBreakable() ? Constants.YES : Constants.NO;
		this.budget = NumberUtil.formatDouble(bean.getBudget(), true);
		this.comments = bean.getComments();
		final Set<LineItem> pckgLIset = bean.getPackagelineItemSet();
		for (LineItem lineItem : pckgLIset) {
			if (lineItem.getProposalId() == null && lineItem.isActive()) {
				lineitemCount = lineitemCount + 1;
			}
		}
		final List<PackageSalesCategoryAssoc> salesCategoryList = bean.getPackageSalesCategoryAssoc();
		final StringBuffer salesCategoryIds = new StringBuffer();
		if (salesCategoryList != null && !salesCategoryList.isEmpty()) {
			for (PackageSalesCategoryAssoc salesCategory : salesCategoryList) {
				salesCategoryIds.append(salesCategory.getSosSalesCategoryId()).append(ConstantStrings.COMMA);
			}
			if (salesCategoryIds.toString().contains(ConstantStrings.COMMA)) {
				this.packageSalescategory = salesCategoryIds.substring(0, salesCategoryIds.lastIndexOf(ConstantStrings.COMMA));
			} else {
				this.packageSalescategory = salesCategoryIds.toString();
			}
		} else {
			this.packageSalescategory = ConstantStrings.EMPTY_STRING;
		}
		this.lineItemCount = lineitemCount;

		this.createdAt = DateUtil.getGuiDateString(bean.getCreatedDate());
		this.modifiedOn = DateUtil.getGuiDateString(bean.getModifiedDate());

		this.modifiedBy = bean.getModifiedBy();
		this.createdBy = bean.getCreatedBy();

		if (bean.getValidFrom() != null) {
			this.validFrom = DateUtil.getGuiDateString(bean.getValidFrom());
		}
		if (bean.getValidTo() != null) {
			this.validTo = DateUtil.getGuiDateString(bean.getValidTo());
			this.expired = String.valueOf(bean.getValidTo().before(DateUtil.getCurrentMidnightDate()));
		}
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(final long packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(final String packageName) {
		this.packageName = packageName == null ? packageName : packageName.trim();
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(final String validFrom) {
		this.validFrom = validFrom == null ? validFrom : validFrom.trim();
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(final String validTo) {
		this.validTo = validTo == null ? validTo : validTo.trim();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(final String budget) {
		this.budget = budget == null ? budget : budget.trim();
	}

	public boolean isHasDocument() {
		return hasDocument;
	}

	public void setHasDocument(final boolean hasDocument) {
		this.hasDocument = hasDocument;
	}

	public int getLineItemCount() {
		return lineItemCount;
	}

	public void setLineItemCount(final int lineItemCount) {
		this.lineItemCount = lineItemCount;
	}

	public String getExpired() {
		return Boolean.valueOf(expired) ? Constants.YES : Constants.NO;
	}

	public void setExpired(final String expired) {
		this.expired = expired == null ? expired : expired.trim();
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(final String ownerId) {
		this.ownerId = ownerId == null ? ownerId : ownerId.trim();
	}

	public String getPackageOwner() {
		return packageOwner;
	}

	public void setPackageOwner(final String packageOwner) {
		this.packageOwner = packageOwner == null ? packageOwner : packageOwner.trim();
	}

	public String getBreakable() {
		return breakable;
	}

	public void setBreakable(final String breakable) {
		this.breakable = breakable == null ? breakable : breakable.trim();
	}

	public String getBreakableStr() {
		return breakableStr;
	}

	public void setBreakableStr(final String breakableStr) {
		this.breakableStr = breakableStr == null ? breakableStr : breakableStr.trim();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy == null ? createdBy : createdBy.trim();
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(final String modifiedBy) {
		this.modifiedBy = modifiedBy == null ? modifiedBy : modifiedBy.trim();
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(final String modifiedOn) {
		this.modifiedOn = modifiedOn == null ? modifiedOn : modifiedOn.trim();
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt == null ? createdAt : createdAt.trim();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments == null ? comments : comments.trim();
	}

	public String getPackageSalescategory() {
		return packageSalescategory;
	}

	public void setPackageSalescategory(final String packageSalescategory) {
		this.packageSalescategory = packageSalescategory == null ? packageSalescategory : packageSalescategory.trim();
	}

	public String getPackageSalescategoryName() {
		return packageSalescategoryName;
	}

	public void setPackageSalescategoryName(final String packageSalescategoryName) {
		this.packageSalescategoryName = packageSalescategoryName;
	}
}
