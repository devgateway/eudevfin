package org.devgateway.eudevfin.financial.backup.test;

import java.io.File;

import org.devgateway.eudevfin.financial.backup.DatabaseBackupService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonContext.xml"})
@TransactionConfiguration(defaultRollback=false, transactionManager="transactionManager")
public class DatabaseBackupTest {

	@Autowired
	private DatabaseBackupService databaseBackupService;

	/**
	 * Tests if the backup archive is created successfully. Does NOT test if the service is ran at the
	 *  given time (the cron test)
	 */
	@Test
	public void testDatabaseBackup() {
		databaseBackupService.backupDatabase();
		File backupFile=new File(databaseBackupService.getLastBackupURL()+DatabaseBackupService.ARCHIVE_SUFFIX);

		Assert.assertTrue(backupFile.exists());
	}
	
	@After
	public void cleanup() {
		File backupFile=new File(databaseBackupService.getLastBackupURL()+DatabaseBackupService.ARCHIVE_SUFFIX);
		if(backupFile.exists()) backupFile.delete();
	}

}
