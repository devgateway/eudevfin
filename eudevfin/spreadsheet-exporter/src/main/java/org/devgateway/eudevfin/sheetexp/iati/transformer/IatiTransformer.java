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
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivities;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * @author alexandru-m-g
 *
 */
@Component
@Lazy(value = false)
public class IatiTransformer {
	@ServiceActivator(inputChannel = "iatiListExporterChannel")
	public Boolean createIatiExport(final List<EntityWrapperInterface<CustomFinancialTransaction>> list, 
			@Header("outputStream") final OutputStream out, 
			@Header("exportName")final String exportName) {
		
		final IatiTransformerEngine engine = new IatiTransformerEngine(list);
		
		final IatiActivities iatiActivities = engine.transform().getIatiActivities();
		
		final XStream xStream = new XStream();
		
		xStream.processAnnotations(IatiActivities.class);
		xStream.registerConverter(new DateConverter("yyyy-MM-dd", new String[] {}));

		
		try (final ObjectOutputStream oos = xStream.createObjectOutputStream(out, "iati-activities")) {
			for (final IatiActivity iatiActivity: iatiActivities.getActivities()) {
				oos.writeObject(iatiActivity);
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
}
