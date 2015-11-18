/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * This <code>ReservationStatus</code> enus contains all the status for the Reservation
 * 
 * @author amandeep.singh
 */
public enum ReservationStatus {
	/**
	 * Represent status of reservation is "Renew"
	 */
	RE_NEW {

		@Override
		public String getDisplayName() {
			return "RENEWED";
		}
	},
	/**
	 * Represent status of reservation is "Created"
	 */
	HOLD {

		@Override
		public String getDisplayName() {
			return "CREATED";
		}
	},
	/**
	 * Represent status of reservation is "Expired"
	 */
	RELEASED {

		@Override
		public String getDisplayName() {
			return "EXPIRED";
		}
	},
	/**
	 * Represent status of reservation is "Deleted"
	 */
	DELETED {

		@Override
		public String getDisplayName() {
			return "DELETED";
		}
	};

	public abstract String getDisplayName();

	public static ReservationStatus findByName(String reservationStatus) {
		if (StringUtils.isNotBlank(reservationStatus)) {
			for (ReservationStatus reservstat : ReservationStatus.values()) {
				if (reservationStatus.equals(reservstat.name())) {
					return reservstat;
				}
			}
		}
		throw new IllegalArgumentException("No Status enum found for given Status name: " + reservationStatus);
	}

	public static Map<String, String> getReservationStatusMap() {
		final Map<String, String> reservationStatusMap = new TreeMap<String, String>();
		for (ReservationStatus reservationStatus : ReservationStatus.values()) {
			reservationStatusMap.put(reservationStatus.name(), reservationStatus.getDisplayName());
		}
		return reservationStatusMap;
	}
}
