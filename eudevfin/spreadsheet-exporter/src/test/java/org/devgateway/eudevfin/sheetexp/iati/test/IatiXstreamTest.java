package org.devgateway.eudevfin.sheetexp.iati.test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivities;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.ReportingOrg;
import org.devgateway.eudevfin.sheetexp.iati.domain.Sector;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

public class IatiXstreamTest {

	@Test
	public void testTransform() {
		
		final IatiActivity act1	= new IatiActivity();
		act1.setTitle("Title1");
		act1.setDescription("Description 1");
		
		final ReportingOrg ro	= new ReportingOrg();
		ro.setRef("test-ref");
		ro.setType("test-type");
		ro.setValue("test-value");
		act1.setReportingOrg(ro);
		
		final Sector sector1 = new Sector();
		sector1.setCode("test-s1-code");
		sector1.setVocabulary("test-vocab");
		sector1.setValue("sector1");
		final ArrayList<Sector> sectors = new ArrayList<>();
		sectors.add(sector1);
		act1.setSectors(sectors);
		
		final IatiActivity act2	= new IatiActivity();
		act2.setTitle("Title1");
		act2.setDescription("Description 1");
		
		final IatiActivities activities = new IatiActivities();
		activities.setActivities( new ArrayList<IatiActivity>() );
		activities.getActivities().add(act1);
		activities.getActivities().add(act2);
		
		activities.setGeneratedDate ( GregorianCalendar.getInstance().getTime() );
		
		final XStream xStream = new XStream();
		
		xStream.processAnnotations(IatiActivity.class);
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", new String[] {}));

		
		try (final ObjectOutputStream oos = xStream.createObjectOutputStream(System.out)) {
			oos.writeObject(activities);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
