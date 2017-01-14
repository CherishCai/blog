package cn.cherish.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_weixinUser")
public class WeixinUser implements java.io.Serializable {

    private static final long serialVersionUID = 8802816756419020793L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "openid", nullable = false, unique = true, length = 64)
    private String openid;

    @Column(name = "nickname", length = 64)
    private String nickname;

    @Column(name = "sex")
    private Short sex;

    @Column(name = "city", length = 64)
    private String city;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    @Column(name = "subscribetime", length = 19)
    private Date subscribetime;

    @Column(name = "headimgurl", length = 128)
    private String headimgurl;




}