package com.nyt.mpt.form;

/**
 * This class is used for agency info
 * 
 * @author amandeep.singh
 * 
 */
public class AgencyForm {

	private long agencyId;
	private String agencyName;

	public long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(final long agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(final String agencyName) {
		this.agencyName = agencyName != null ? agencyName.trim() : agencyName;
	}

	public AddressForm getAddressForm() {
		return addressForm;
	}

	public void setAddressForm(final AddressForm addressForm) {
		this.addressForm = addressForm;
	}

	private AddressForm addressForm;
}
