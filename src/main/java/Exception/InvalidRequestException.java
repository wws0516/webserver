package Exception;

/**
 * @Author: wws
 * @Date: 2020-07-15 17:32
 */
public class InvalidRequestException extends Exception{

    private String errMsg;

    public InvalidRequestException(String errMsg){
        super(errMsg);
    }

    public String getErrMsg() {
        return errMsg;
    }
}
