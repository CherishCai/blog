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
@Table(name = "t_autoresponse")
public class Autoresponse implements java.io.Serializable {

    private static final long serialVersionUID = -7841654796577116909L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "keyword", nullable = false, length = 64)
    private String keyword;
    @Column(name = "message", nullable = false, columnDefinition = "varchar(1024)")
    private String message;
    @Column(name = "msgType", nullable = false, columnDefinition = "varchar(16) default ''")
    private String msgType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    @Column(name = "createtime", length = 19)
    private Date createtime;

}