/**
 *  public doamin
 */
package ids;

/**
 * @author mgdesigner
 *
 */
public class Utils {
    
    public static int unsigbyte2int(byte unsbyte){
	//java的byte是有型別的，所以有這轉換
	//不過好像無此必要，因為JVM裏面最基本的運算單位是32位元的也就是一個int
	return(int)(unsbyte&0xff);	 
    }
    
   
    
}
