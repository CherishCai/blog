package cn.cherish.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicSearchDto implements Serializable {

	private static final long serialVersionUID = -1707407604592617342L;

	//orderColumn=name&orderDir=desc&
	private String orderColumn;
	private String orderDir;
	//startIndex=0&pageSize=10&draw=1
	private Integer startIndex = 0;
	private Integer pageSize = 10;
	private String draw = "1";

	public Integer getPageNumber(){
        return startIndex / pageSize + 1;
    }

}
