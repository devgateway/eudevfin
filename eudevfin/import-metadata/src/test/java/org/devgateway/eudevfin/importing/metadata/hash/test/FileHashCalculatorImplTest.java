/**
 * 
 */
package org.devgateway.eudevfin.importing.metadata.hash.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.devgateway.eudevfin.financial.dao.AreaDaoImpl;
import org.devgateway.eudevfin.financial.repository.AreaTranslationRepository;
import org.devgateway.eudevfin.importing.metadata.dao.IImportedFileDAO;
import org.devgateway.eudevfin.importing.metadata.entity.ImportedFile;
import org.devgateway.eudevfin.importing.metadata.hash.FileHashHelperImpl;
import org.devgateway.eudevfin.importing.metadata.hash.IFileHashHelper;
import org.devgateway.eudevfin.metadata.common.domain.Area;
import org.devgateway.eudevfin.metadata.common.domain.translation.AreaTranslation;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/commonContext.xml",
		"classpath:/META-INF/financialContext.xml",
		"classpath:META-INF/commonFinancialContext.xml","classpath:testImportMetadataContext.xml" })
@Component 
public class FileHashCalculatorImplTest {
	
	private static final String ABC_STREAM_HASH = "900150983cd24fb0d6963f7d28e17f72";
	private static final String FILENAME_TEST = "filename-test";
	@Autowired
	private IImportedFileDAO importedFileDao;
	
	@Autowired
	private AreaDaoImpl areaDaoImpl;
	
	@Autowired
	private AreaTranslationRepository areaTranslationRepository;

	/**
	 * Test method for {@link org.devgateway.eudevfin.importing.metadata.hash.FileHashHelperImpl#computeHash()}.
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testComputeHash() throws UnsupportedEncodingException {
		final String testStr1	= "abc";
		final String testStr2	= testStr1 + "d"; 
		
		final InputStream is1	= new ByteArrayInputStream(testStr1.getBytes("UTF-8") );
		final InputStream is2	= new ByteArrayInputStream(testStr2.getBytes("UTF-8") );
		
		final FileHashHelperImpl calculator1	= new FileHashHelperImpl();
		calculator1.setup("", is1, this.importedFileDao);
		final FileHashHelperImpl calculator2	= new FileHashHelperImpl();
		calculator2.setup("", is2, this.importedFileDao);
		
		final String hash1	= calculator1.computeHash();
		final String hash2	= calculator2.computeHash();
		
		assertEquals(ABC_STREAM_HASH, hash1);
		
		assertNotEquals(hash1, hash2);
		
	}
	
	@Test
	@Ignore
	public void testUnique() {
		
	}
	
	@Test
	@Transactional
	public void testDetectFileModified() throws UnsupportedEncodingException {
		final String testStr1	= "abc";
		
		final InputStream is1	= new ByteArrayInputStream(testStr1.getBytes("UTF-8") );
		
		final IFileHashHelper hashHelper1	= new FileHashHelperImpl();
		hashHelper1.setup(FILENAME_TEST, is1, this.importedFileDao);
		final String hash1	= hashHelper1.computeHash();
		
		assertFalse( hashHelper1.checkAlreadyLoaded() );
		
		hashHelper1.markAsLoaded();
		
		final ImportedFile loadedFile1	= this.importedFileDao.findImportedFileByFilename(FILENAME_TEST);
		
		assertNotNull( loadedFile1 );
		assertEquals(ABC_STREAM_HASH, loadedFile1.getHashcode());
		
		assertTrue( hashHelper1.checkAlreadyLoaded() );
		
		final String testStr2				= testStr1 + "d";
		final InputStream is2	= new ByteArrayInputStream(testStr2.getBytes("UTF-8") );
		final IFileHashHelper hashHelper2	= new FileHashHelperImpl();
		hashHelper2.setup(FILENAME_TEST, is2, this.importedFileDao);
		
		final String hash2	= hashHelper2.computeHash();
		assertFalse( hashHelper2.checkAlreadyLoaded() );
		hashHelper2.markAsLoaded();
		
		final ImportedFile loadedFile2		= this.importedFileDao.findImportedFileByFilename(FILENAME_TEST);
		assertNotNull( loadedFile2 );
		assertNotEquals(ABC_STREAM_HASH, loadedFile2.getHashcode());
		
	}
	
	@Test
	@Transactional
	public void testAreaClassChange() {
		Area a	= new Area();
		a.setCode("code");
		a.setIncomeGroup(null);
		a.setName("name");
		
		a	= this.areaDaoImpl.save(a).getEntity();
		final AreaTranslation at	= a.getTranslations().get("en");
		final List<AreaTranslation> trns1	= this.areaTranslationRepository.findAll();
		
		assertEquals(Area.class, a.getClass());
		
		final Area c	= new Area();
		c.setCode("code2");
		c.setIncomeGroup(null);
		c.setName("name2");
		c.setId(a.getId());
		
		final Area a2	= this.areaDaoImpl.save(c).getEntity();
		
		final List<AreaTranslation> trns2	= this.areaTranslationRepository.findAll();
		
		final List<Area> all	= this.areaDaoImpl.findAllAsList();
		
		assertEquals(1, all.size());
		
		
	}
	

}
