package cn.cherish.blog.dal.entity;

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
@Table(name = "t_contact")
public class Contact implements java.io.Serializable {

    private static final long serialVersionUID = -7607393826531764452L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Column(name = "subject", nullable = false, length = 64)
    private String subject;

    @Column(name = "message", nullable = false, length = 64)
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    @Column(name = "createtime", length = 19)
    private Date createtime;

}