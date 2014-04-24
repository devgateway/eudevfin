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
	private static final int DIVIDER = 1000000;
	
	@Function("CHECKNULL")
	@FunctionParameters({
			@FunctionParameter("number")})
	public static Number CHECKNULL(Number number){
		if(number==null) {
			return 0;
		}
		else{
			if(number instanceof Integer){
				return ((Integer)number).intValue()/DIVIDER;
			}
			else if(number instanceof Double){
				return ((Double)number).doubleValue()/DIVIDER;
			}
			else if(number instanceof Float){
				return ((Float)number).intValue()/DIVIDER;
			}
			else if(number instanceof Long){
				return ((Long)number).intValue()/DIVIDER;
			}
			else{
				return 0;
			}
		}
	}
}
