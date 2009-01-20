/**
 * 
 * 1/9/2006新增
 */
package ids;

/**
 * @author mgdesigner
 *
 *這是表示一個合體字子組件的框架組
 *子組件的數量應該跟子組件框的第1欄的index一樣大
 */
public class ComponentFrameGroup {
    public String[] 子組件;
    public int[][] 框;
    public ComponentFrameGroup(String[] 子組件,    int[][] 子組件框){
	this.子組件=子組件;
	this.框=子組件框;
    }
    public ComponentFrameGroup(String[] 子組件){
	
    }
public ComponentFrameGroup(char 字元,int[][] 框){
    子組件=new String[1];
    子組件[0]=""+字元+0;//異體字為0
  this.框=框;
    }
public ComponentFrameGroup(){
    
    }

}
