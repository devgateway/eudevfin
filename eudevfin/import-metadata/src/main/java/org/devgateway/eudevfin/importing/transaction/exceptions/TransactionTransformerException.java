/**
 *
 */
package org.devgateway.eudevfin.importing.transaction.exceptions;

/**
 * @author alexandru-m-g
 *
 */
public class TransactionTransformerException extends RuntimeException {

	private final int colNumber ;

	public TransactionTransformerException(final int colNumber) {
		super();
		this.colNumber = colNumber;
	}



	public TransactionTransformerException(final Throwable cause, final int colNumber) {
		super(cause);
		this.colNumber = colNumber;
	}



	public int getColNumber() {
		return this.colNumber;
	}

}
