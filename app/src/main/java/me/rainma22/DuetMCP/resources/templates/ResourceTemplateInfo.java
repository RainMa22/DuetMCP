package me.rainma22.DuetMCP.resources.templates;

import org.json.JSONPropertyName;

/**
 *
 */
public class ResourceTemplateInfo {

    private String uriTemplate;
    private String name;
    private String title = null;
    private String description = null;
    private String mimeType = null;
    private Integer sizeInBytes = null; //in bytes

    public ResourceTemplateInfo(String uriTemplate, String name) {
        this(uriTemplate, name, null, null, null, null);
    }

    public ResourceTemplateInfo(String uriTemplate, String name,
            String title, String description, String mimeType, Integer sizeInBytes) {
        setUriTemplate(uriTemplate);
        setName(name);
        setTitle(title);
        setDescription(description);
        setMimeType(mimeType);
        setSizeInBytes(sizeInBytes);
    }

    public String getUriTemplate() {
        return uriTemplate;
    }

    public void setUriTemplate(String template) {
        this.uriTemplate = template;
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
