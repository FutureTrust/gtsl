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

package eu.futuretrust.gtsl.business.services.additional.impl;

import eu.futuretrust.gtsl.business.services.additional.TslAdditionalInformationService;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import eu.futuretrust.gtsl.model.data.extension.AdditionalServiceInformationType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.utils.RulesPropertiesUtils;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TslAdditionalInformationServiceImpl implements TslAdditionalInformationService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TslAdditionalInformationServiceImpl.class);

  @Override
  public void appendAdditionalInformation(RulesProperties properties, TrustStatusListType tsl) {
    if (tsl != null && tsl.getTrustServiceProviderList() != null && CollectionUtils
        .isNotEmpty(tsl.getTrustServiceProviderList().getValues())) {
      tsl.getTrustServiceProviderList().getValues()
          .forEach(tsp -> appendAdditionalInformation(properties, tsp));
    }
  }

  @Override
  public void appendAdditionalInformation(RulesProperties properties, TSPType tsp) {
    if (tsp != null && tsp.getTspServices() != null && CollectionUtils
        .isNotEmpty(tsp.getTspServices().getValues())) {
      tsp.setActive(isTspActive(properties, tsp));
      tsp.setServiceTypes(new LinkedHashSet<>());
      tsp.getTspServices().getValues().forEach(service -> {
        appendAdditionalInformation(properties, service);
        tsp.getServiceTypes().addAll(service.getServiceTypes());
      });
      tsp.setServiceTypes(sortServiceTypes(tsp.getServiceTypes()));
    }
  }

  @Override
  public void appendAdditionalInformation(RulesProperties properties, TSPServiceType service) {
    service.setServiceTypes(getServiceTypes(properties, service));
  }

  @Override
  public Set<ServiceType> getServiceTypes(RulesProperties properties, TSPServiceType service) {
    Set<ServiceType> qServiceTypes = new LinkedHashSet<>();

    if (service != null && service.getServiceInformation() != null
        && service.getServiceInformation().getServiceTypeIdentifier() != null
        && org.apache.commons.lang.StringUtils
        .isNotEmpty(service.getServiceInformation().getServiceTypeIdentifier().getValue())) {

      String serviceType = service.getServiceInformation().getServiceTypeIdentifier().getValue();
      List<AdditionalServiceInformationType> asiList = ModelUtils
          .getValidAdditionalServiceInformation(
              service.getServiceInformation().getServiceInformationExtensions());

      if (typeMatchQCert(properties, serviceType)) {
        addQCertService(qServiceTypes, properties, asiList);
      } else if (typeMatchQVal(properties, serviceType)) {
        addQValService(qServiceTypes, properties, asiList);
      } else if (typeMatchQPres(properties, serviceType)) {
        addQPresService(qServiceTypes, properties, asiList);
      } else if (typeMatchQTimestamp(properties, serviceType)) {
        qServiceTypes.add(ServiceType.Q_TIMESTAMP);
      } else if (typeMatchQERDS(properties, serviceType)) {
        qServiceTypes.add(ServiceType.Q_ERDS);
      } else if (typeMatchQSCDManagement(properties, serviceType)) {
        qServiceTypes.add(ServiceType.Q_QSCDMANAGEMENT);
      } else if (typeMatchCert(properties, serviceType)) {
        addCertService(qServiceTypes, properties, asiList);
      } else if (typeMatchVal(properties, serviceType)) {
        addValService(qServiceTypes, properties, asiList);
      } else if (typeMatchGen(properties, serviceType)) {
        addGenService(qServiceTypes, properties, asiList);
      } else if (typeMatchPres(properties, serviceType)) {
        addPresService(qServiceTypes, properties, asiList);
      } else if (typeMatchTimestamp(properties, serviceType)) {
        qServiceTypes.add(ServiceType.NQ_TIMESTAMP);
      } else if (typeMatchERDS(properties, serviceType)) {
        qServiceTypes.add(ServiceType.NQ_ERDS);
      } else if (typeMatchNonRegulatory(properties, serviceType)) {
        qServiceTypes.add(ServiceType.NON_REGULATORY);
      } else {
        qServiceTypes.add(ServiceType.CERT_UNDEFINED);
      }

    }

    return sortServiceTypes(qServiceTypes);
  }

  @Override
  public Set<String> getServiceStatus(List<ServiceType> serviceTypes, TSPServiceType service,
      Set<ServiceType> qServiceTypes) {
    Set<String> serviceStatuses = new LinkedHashSet<>();
    String serviceStatus = service.getServiceInformation().getServiceStatus().getValue();
    if (qServiceTypes.stream().anyMatch(serviceTypes::contains)) {
      serviceStatuses.add(serviceStatus);
    }
    return serviceStatuses;
  }

  @Override
  public boolean isTspActive(RulesProperties properties, TSPType tsp) {
    boolean active = false;
    if (tsp != null && tsp.getTspServices() != null && CollectionUtils
        .isNotEmpty(tsp.getTspServices().getValues())) {
      active = tsp.getTspServices().getValues().stream()
          .anyMatch(service ->
              service.getServiceInformation().getServiceStatus().getValue()
                  .equals(properties.getServiceStatus().getGranted()) || service
                  .getServiceInformation().getServiceStatus().getValue()
                  .equals(properties.getServiceStatus().getRecognisedatnationallevel()) || service
                  .getServiceInformation().getServiceStatus().getValue()
                  .equals(properties.getServiceStatus().getSetbynationallaw())
          );
    }
    return active;
  }

  @Override
  public Set<ServiceType> sortServiceTypes(Set<ServiceType> set) {
    Map<Boolean, List<ServiceType>> mapServiceTypes = set.stream()
        .collect(Collectors.partitioningBy(ServiceType::isQualified));
    mapServiceTypes.get(true).sort(Comparator.comparing(ServiceType::getIdentifier));
    mapServiceTypes.get(false).sort(Comparator.comparing(ServiceType::getIdentifier));
    Set<ServiceType> sortedServiceTypes = new LinkedHashSet<>(mapServiceTypes.get(true));
    sortedServiceTypes.addAll(mapServiceTypes.get(false));
    return sortedServiceTypes;
  }

  private boolean typeMatchQCert(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getCaQC())
        || serviceType.equals(properties.getQualifiedServiceType().getOcspQC())
        || serviceType.equals(properties.getQualifiedServiceType().getCrlQC());
  }

  private boolean typeMatchQVal(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getQesValidationQ());
  }

  private boolean typeMatchQPres(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getPsesQ());
  }

  private boolean typeMatchQTimestamp(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getTsaQTST());
  }

  private boolean typeMatchQERDS(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getEdsQ())
        || serviceType.equals(properties.getQualifiedServiceType().getEdsRemQ());
  }

  private boolean typeMatchQSCDManagement(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getQualifiedServiceType().getRemoteQSCDManagementQ());
  }

  private boolean typeMatchCert(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getCaPKC())
        || serviceType.equals(properties.getNonQualifiedServiceType().getOcsp())
        || serviceType.equals(properties.getNonQualifiedServiceType().getCrl());
  }

  private boolean typeMatchVal(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getAdesValidation());
  }

  private boolean typeMatchGen(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getAdesGeneration());
  }

  private boolean typeMatchPres(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getPses());
  }

  private boolean typeMatchTimestamp(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getTsa())
        || serviceType.equals(properties.getNonQualifiedServiceType().getTsaTSSQC())
        || serviceType.equals(properties.getNonQualifiedServiceType().getTsaTSSAdes());
  }

  private boolean typeMatchERDS(RulesProperties properties, String serviceType) {
    return serviceType.equals(properties.getNonQualifiedServiceType().getEds())
        || serviceType.equals(properties.getNonQualifiedServiceType().getEdsRem());
  }

  private boolean typeMatchNonRegulatory(RulesProperties properties, String serviceType) {
    Set<String> nationalServiceTypes = RulesPropertiesUtils
        .getPropertiesAsSet(properties.getNationalServiceType(), String.class);
    return nationalServiceTypes.contains(serviceType);
  }

  private void addQCertService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean matchForWAC = extensionsMatchForWebSiteAuthentication(properties, asiList);
    boolean matchRootCA = extensionsMatchRootCA(properties, asiList);
    boolean anyMatch =
        matchForeSignatures || matchForeSeals || matchForWAC || matchRootCA;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.Q_CERT_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.Q_CERT_ESEAL);
    }
    if (matchForWAC) {
      qServiceTypes.add(ServiceType.Q_CERT_WAC);
    }
    if (matchRootCA) {
      qServiceTypes.add(ServiceType.Q_CERT_ROOTCA);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addQValService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.Q_VAL_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.Q_VAL_ESEAL);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addQPresService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.Q_PRES_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.Q_PRES_ESEAL);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addCertService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean matchForWAC = extensionsMatchForWebSiteAuthentication(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals || matchForWAC;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.NQ_CERT_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.NQ_CERT_ESEAL);
    }
    if (matchForWAC) {
      qServiceTypes.add(ServiceType.NQ_CERT_WAC);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addValService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.NQ_VAL_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.NQ_VAL_ESEAL);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addGenService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.NQ_GEN_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.NQ_GEN_ESEAL);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private void addPresService(Set<ServiceType> qServiceTypes, RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    boolean matchForeSignatures = extensionsMatchForeSignatures(properties, asiList);
    boolean matchForeSeals = extensionsMatchForeSeals(properties, asiList);
    boolean anyMatch = matchForeSignatures || matchForeSeals;
    if (matchForeSignatures) {
      qServiceTypes.add(ServiceType.NQ_PRES_ESIG);
    }
    if (matchForeSeals) {
      qServiceTypes.add(ServiceType.NQ_PRES_ESEAL);
    }
    if (!anyMatch) {
      qServiceTypes.add(ServiceType.CERT_UNDEFINED);
    }
  }

  private boolean extensionsMatchForeSignatures(RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    return asiList != null && asiList.stream().anyMatch(asi -> asi.getUri().getValue()
        .equals(properties.getAdditionalQualifier().getForeSignatures()));
  }

  private boolean extensionsMatchForeSeals(RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    return asiList != null && asiList.stream().anyMatch(
        asi -> asi.getUri().getValue().equals(properties.getAdditionalQualifier().getForeSeals()));
  }

  private boolean extensionsMatchForWebSiteAuthentication(RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    return asiList != null && asiList.stream().anyMatch(asi -> asi.getUri().getValue()
        .equals(properties.getAdditionalQualifier().getForWebSiteAuthentication()));
  }

  private boolean extensionsMatchRootCA(RulesProperties properties,
      List<AdditionalServiceInformationType> asiList) {
    return asiList != null && asiList.stream().anyMatch(asi -> asi.getUri().getValue()
        .equals(properties.getAdditionalQualifier().getRootCAQC()));
  }

}
