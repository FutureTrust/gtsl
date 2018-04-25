package eu.futuretrust.gtsl.web.dto.tsl;

public class TLDifferenceDTO {

  private String id;
  private String publishedValue;
  private String currentValue;
  private String hrLocation;

  public TLDifferenceDTO() {
  }

  public TLDifferenceDTO(String parent, String publishedValue, String currentValue) {
    this.setId(parent);
    this.setPublishedValue(publishedValue);
    this.setCurrentValue(currentValue);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPublishedValue() {
    return publishedValue;
  }

  public void setPublishedValue(String oldValue) {
    this.publishedValue = oldValue;
  }

  public String getHrLocation() {
    return hrLocation;
  }

  public void setHrLocation(String hrLocation) {
    this.hrLocation = hrLocation;
  }

  public String getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(String currentValue) {
    this.currentValue = currentValue;
  }
}
