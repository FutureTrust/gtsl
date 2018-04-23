package eu.futuretrust.gtsl.admin.binder;

import java.beans.PropertyEditorSupport;
import org.apache.commons.codec.binary.Base64;

public class ByteArrayBase64Editor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) {
		setValue(text != null && Base64.isBase64(text) ? Base64.decodeBase64(text) : null);
	}

	@Override
	public String getAsText() {
		byte[] value = (byte[]) getValue();
		return (value != null ? Base64.encodeBase64String(value) : "");
	}

}