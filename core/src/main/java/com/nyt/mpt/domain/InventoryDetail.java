package com.nyt.mpt.domain;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to capture avails inventory from Yield-ex
 * 
 * @author manish.kesarwani
 * 
 */
@XStreamAlias("inventoryDetail")
public class InventoryDetail {

	private List<DailyDetail> dailyDetail = new ArrayList<DailyDetail>();

	private ProductDetail productDetail;

	private Summary summary;

	public List<DailyDetail> getDailyDetail() {
		return dailyDetail;
	}

	public void setDailyDetail(List<DailyDetail> dailyDetail) {
		this.dailyDetail = dailyDetail;
	}

	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}
}
