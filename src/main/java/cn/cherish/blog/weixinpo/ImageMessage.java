package cn.cherish.blog.weixinpo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class ImageMessage extends BaseMessage{

	private Image Image;

	@XmlElement(name = "Image")
	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
}
