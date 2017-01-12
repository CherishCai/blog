package cn.cherish.blog.weixinpo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class MusicMessage extends BaseMessage{

	private Music Music;

	@XmlElement(name = "Music")
	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
