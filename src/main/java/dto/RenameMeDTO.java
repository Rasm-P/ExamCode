
package dto;

import entities.RenameMe;
import io.swagger.v3.oas.annotations.media.Schema;

public class RenameMeDTO {
    
    @Schema(required = true, example = "String1")
    private String dummyStr1;
    @Schema(required = true, example = "String2")
    private String dummyStr2;
    
    public RenameMeDTO(RenameMe renameMe) {
        this.dummyStr1 = renameMe.getDummyStr1();
        this.dummyStr2 = renameMe.getDummyStr2();
    }

    public String getDummyStr1() {
        return dummyStr1;
    }

    public void setDummyStr1(String dummyStr1) {
        this.dummyStr1 = dummyStr1;
    }

    public String getDummyStr2() {
        return dummyStr2;
    }

    public void setDummyStr2(String dummyStr2) {
        this.dummyStr2 = dummyStr2;
    }
    
}
