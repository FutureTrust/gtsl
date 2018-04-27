/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.model.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JaxbGregorianCalendarZulu extends XmlAdapter<String, XMLGregorianCalendar> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JaxbGregorianCalendarZulu.class);

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";
	private static final String DATE_FORMAT3 = "yyyy-MM-dd'T'HH:mm:ss";


	@Override
	public XMLGregorianCalendar unmarshal(String v) {
		SimpleDateFormat formatUTC = new SimpleDateFormat(DATE_FORMAT);
		formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat formatUTC2 = new SimpleDateFormat(DATE_FORMAT2);
		formatUTC2.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat formatUTC3 = new SimpleDateFormat(DATE_FORMAT3);
		formatUTC3.setTimeZone(TimeZone.getTimeZone("UTC"));
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		
		Date dt=null;
		try {
			dt = formatUTC.parse(v);
		} catch (ParseException e) {
		}
		
		if (dt==null){
			try {
				dt = formatUTC2.parse(v);
			} catch (ParseException e) {
			}
		}
		
		if (dt==null){
			try {
				dt = formatUTC3.parse(v);
			} catch (ParseException e) {
			}
		}
		
		if (dt==null){
			LOGGER.error("An error occurred when parsing date (unknown date format) : " + v);
		}
		
		gregorianCalendar.setTime(dt);	
		XMLGregorianCalendar calendar = null;
		
		try {
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
		return calendar;

	}

	@Override
	public String marshal(XMLGregorianCalendar v) throws Exception {
		SimpleDateFormat formatUTC = new SimpleDateFormat(DATE_FORMAT);
		formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatUTC.format(v.toGregorianCalendar().getTime());
	}
	
}
