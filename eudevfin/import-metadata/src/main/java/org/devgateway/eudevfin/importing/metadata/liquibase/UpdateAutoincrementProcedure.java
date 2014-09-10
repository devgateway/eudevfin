/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.liquibase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author Alex
 * 
 */
public class UpdateAutoincrementProcedure {
	private static Logger logger	= Logger.getLogger(UpdateAutoincrementProcedure.class);
	public static void updateAutoincrement(final String tableName, final String colName) throws SQLException {
		try (Connection conn = DriverManager
				.getConnection("jdbc:default:connection")) {
			int updateValue	= 0;
			
			final String sqlString	= String.format("select max(%s)+1 from APP.%s", colName, tableName);
			final PreparedStatement ps1 = conn
					.prepareStatement(sqlString);
			
			final ResultSet rs1	= ps1.executeQuery();
			if ( rs1.next() ) {
				updateValue	= rs1.getInt(1);
				if ( updateValue == 0){ 
					updateValue =1;
				}
			}
			rs1.close();
			
			if ( updateValue > 0 ) {
				final PreparedStatement ps2	= conn.prepareStatement(
						String.format("ALTER TABLE APP.%s ALTER COLUMN %s RESTART WITH %d", 
								tableName, colName, updateValue));
				ps2.executeUpdate();
			}
			else {
				logger.error( String.format("Cannot update derby autoincrement for table %s and column %s to value %d ",
					tableName, colName, updateValue) );
			}
			
		} catch (final SQLException e) {
			logger.error(String.format("Procedure for updating derby autoincrement couldn't run for table %s and column %s",
					tableName, colName));
			throw e;
		}
	}
}
