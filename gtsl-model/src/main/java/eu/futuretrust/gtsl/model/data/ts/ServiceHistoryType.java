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

package eu.futuretrust.gtsl.model.data.ts;

import eu.futuretrust.gtsl.jaxb.tsl.ServiceHistoryTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact.Legal;
import eu.futuretrust.gtsl.model.constraints.payload.Severity.Error;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5.10 Service history
 *
 * <xsd:complexType name="ServiceHistoryType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.6 Service history instance --> <xsd:element ref="tsl:ServiceHistoryInstance"
 * minOccurs="0" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="ServiceHistoryInstance" type="tsl:ServiceHistoryInstanceType"/>
 */
public class ServiceHistoryType extends ListModel<ServiceHistoryInstanceType> {

	public ServiceHistoryType() {
	}

	public ServiceHistoryType(
			List<ServiceHistoryInstanceType> values) {
		super(values);
	}

	public ServiceHistoryType(ServiceHistoryTypeJAXB serviceHistory) {
		super(CollectionUtils.isNotEmpty(serviceHistory.getServiceHistoryInstance()) ? serviceHistory
				.getServiceHistoryInstance().stream().map(ServiceHistoryInstanceType::new)
				.collect(Collectors.toList()) : Collections.emptyList());
	}

	@Override
	//@NotEmpty(message = "{NotEmpty.serviceHistory.values}", payload = {Severity.Info.class, Impact.Legal.class})
	public List<ServiceHistoryInstanceType> getValues() {
		return super.getValues();
	}

	// from TL-Manager
	public ServiceHistoryTypeJAXB asJAXB(
			@NotEmpty(message = "{NotEmpty.serviceDigitalIdentity.values}", payload = {Error.class,
					Legal.class}) List<DigitalIdentityType> digitalIdentityList) {
		if (CollectionUtils.isNotEmpty(values)) {
			ServiceHistoryTypeJAXB serviceHistoryJAXB = new ServiceHistoryTypeJAXB();
			for (ServiceHistoryInstanceType serviceHistory : this.values) {
				if (digitalIdentityList != null) {
					if (digitalIdentityList.get(0) != null
							&& digitalIdentityList.get(0).getCertificateList() != null
							&& !digitalIdentityList.get(0).getCertificateList().isEmpty()) {
						serviceHistoryJAXB.getServiceHistoryInstance()
								.add(serviceHistory.asJAXB(digitalIdentityList.get(0).getCertificateList().get(0)));
					} else {
						serviceHistoryJAXB.getServiceHistoryInstance().add(serviceHistory.asJAXB(null));
					}
				} else {
					serviceHistoryJAXB.getServiceHistoryInstance().add(serviceHistory.asJAXB(null));
				}
			}
			return serviceHistoryJAXB;
		}
		return null;
	}
}
