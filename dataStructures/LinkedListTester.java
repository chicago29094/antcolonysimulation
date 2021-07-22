
package dataStructures;

public class LinkedListTester
{
	public static void main(String[] args) 
	{
      LinkedList numberList=new LinkedList();
      
      System.out.println("1. Start");
      numberList.add("3");
      numberList.add("8");
     numberList.add("23");
     numberList.add("1");
     numberList.add("900");
     numberList.add("45");
     numberList.add("16");
     numberList.add("37");
     numberList.add("54");
     numberList.add("278");
     numberList.add("6");


      for (Iterator itr = numberList.iterator(); itr.hasNext();)
      {
         String tinyInt=(String) itr.getCurrent();
         System.out.println("2. In Loop: " + tinyInt + " hasNext()=" + itr.hasNext() );
         if (tinyInt.equals("37"))
         {
            numberList.add(92, 7);
         }
         itr.next();
      }
      
      System.out.println("3. End");      
      
   }
}
