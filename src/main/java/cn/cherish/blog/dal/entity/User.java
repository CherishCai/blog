package cn.cherish.blog.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable {

    private static final long serialVersionUID = -3703091209635157421L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Column(name = "password", nullable = false, length = 40)
    private String password;

    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Column(name = "telephone", nullable = false, length = 16)
    private String telephone;

    @Column(name = "position", nullable = false, length = 16)
    private String position;

    @Temporal(TemporalType.DATE)
    @Column(name = "hiredate", nullable = false, length = 19)
    private Date hiredate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", nullable = false, length = 19)
    private Date modifiedTime;

    @Column(name = "is_active", nullable = false)
    private Integer active;

    @Column(name = "description", nullable=false, columnDefinition = "varchar(1024) default '' ")
    private String description;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }
}
