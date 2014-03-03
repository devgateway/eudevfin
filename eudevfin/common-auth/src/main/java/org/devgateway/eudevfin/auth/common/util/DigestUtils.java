/**
 * 
 */
package org.devgateway.eudevfin.auth.common.util;

/**
 * @author mihai
 * 
 */
public final class DigestUtils {

	/**
	 * Standard encryption for passwords in EU-DEVFIN, using
	 * {@link org.apache.commons.codec.digest.DigestUtils#sha256Hex(String)}
	 * 
	 * @param input
	 *            the unencrypted password
	 * @return the encrypted password, using the chosen algorithm
	 */
	public static String passwordEncrypt(String input) {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
	}

}
