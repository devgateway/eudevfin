package org.devgateway.eudevfin.reports.core.utils;

import net.sf.jasperreports.functions.AbstractFunctionSupport;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@FunctionCategories({FunctionsCategory.class})
public final class JasperFunctions extends AbstractFunctionSupport
{
	private static final Log log = LogFactory.getLog(JasperFunctions.class);
	
	@Function("CHECKNULL")
	@FunctionParameters({
			@FunctionParameter("number")})
	public static int CHECKNULL(Number number){
		if(number==null) {
			return 0;
		}
		else{
			if(number instanceof Integer){
				return ((Integer)number).intValue();
			}
			else if(number instanceof Double){
				return ((Double)number).intValue();
			}
			else if(number instanceof Float){
				return ((Float)number).intValue();
			}
			else if(number instanceof Long){
				return ((Long)number).intValue();
			}
			else{
				return 0;
			}
		}
	}
}
