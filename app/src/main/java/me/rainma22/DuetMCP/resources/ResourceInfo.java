package me.rainma22.DuetMCP.resources;

import java.net.URI;
import org.json.JSONPropertyName;

/**
 *
 */
public class ResourceInfo {

    private URI uri;
    private String name;
    private String title = null;
    private String description = null;
    private String mimeType = null;
    private Integer sizeInBytes = null; //in bytes

    public ResourceInfo(URI uri, String name) {
        this(uri, name, null, null, null, null);
    }

    public ResourceInfo(URI uri, String name,
            String title, String description, String mimeType, Integer sizeInBytes) {
        setUri(uri);
        setName(name);
        setTitle(title);
        setDescription(description);
        setMimeType(mimeType);
        setSizeInBytes(sizeInBytes);
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JSONPropertyName("size")
    public Integer getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

}
