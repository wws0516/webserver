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


    @Override
    public void printStackTrace(){
        System.out.println(errMsg);
    }
}
