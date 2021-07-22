


public class ArrayListTester
{
	public static void main(String[] args) 
	{
      ArrayList numberList=new ArrayList();
      
      System.out.println("1. Start");
      numberList.add("1");
      numberList.add("dog");

      for (Iterator itr = numberList.iterator(); itr.hasNext();)
      {
         String tinyInt=(String) itr.getCurrent();
         System.out.println("2. In Loop: " + tinyInt + " hasNext()=" + itr.hasNext() );
         itr.next();
         tinyInt=(String) itr.getCurrent();
         System.out.println("2. In Loop: " + tinyInt + " hasNext()=" + itr.hasNext() );

      }
      
      System.out.println("3. End");      
      
   }
}
